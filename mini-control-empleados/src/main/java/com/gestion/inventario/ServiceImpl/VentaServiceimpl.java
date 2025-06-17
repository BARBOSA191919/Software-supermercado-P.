package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.ProductoVendido;
import com.gestion.inventario.entidades.Venta;
import com.gestion.inventario.repositorios.VentaRepository;
import com.gestion.inventario.servicio.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VentaServiceimpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    // Métodos existentes...
    @Override
    public List<Venta> findAll() {
        List<Venta> ventas = new ArrayList<>();
        ventaRepository.findAll().forEach(ventas::add);
        return ventas;
    }

    @Override
    public void save(Venta venta) {
        ventaRepository.save(venta);
    }

    @Override
    public Venta findOne(Long id) {
        return ventaRepository.findById(id.intValue()).orElse(null);
    }

    @Override
    public void delete(Long id) {
        ventaRepository.deleteById(id.intValue());
    }

    @Override
    public Venta findById(Integer id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Override
    public Float calcularTotalVentas(List<Venta> ventas) {
        return ventas.stream()
                .map(Venta::getTotal)
                .reduce(0f, Float::sum);
    }


    // Nuevos métodos adaptados a tu entidad Venta

    @Override
    public List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Ajustar formato
        Date fechaInicio = Date.from(inicio.atZone(ZoneId.systemDefault()).toInstant());
        Date fechaFin = Date.from(fin.atZone(ZoneId.systemDefault()).toInstant());

        List<Venta> ventasFiltradas = findAll().stream()
                .filter(v -> {
                    try {
                        Date fechaVenta = sdf.parse(v.getFechaYHora());
                        System.out.println("Comparando: " + v.getFechaYHora() + " vs " + sdf.format(fechaInicio) + " - " + sdf.format(fechaFin));
                        return (fechaVenta.after(fechaInicio) || fechaVenta.equals(fechaInicio)) &&
                                (fechaVenta.before(fechaFin) || fechaVenta.equals(fechaFin));
                    } catch (ParseException e) {
                        System.err.println("Error al parsear fechaYHora: " + v.getFechaYHora());
                        return false;
                    }
                })
                .collect(Collectors.toList());

        System.out.println("Ventas filtradas entre " + inicio + " y " + fin + ": " + ventasFiltradas.size());
        ventasFiltradas.forEach(v -> System.out.println("Venta: " + v.getCodigoVenta() + ", Fecha: " + v.getFechaYHora() + ", Total: " + v.getTotal()));
        return ventasFiltradas;
    }

    @Override
    public Map<String, Double> obtenerVentasAgrupadas(LocalDateTime inicio, LocalDateTime fin, String reportType) {
        List<Venta> ventas = findByFechaBetween(inicio, fin);
        Map<String, Double> ventasAgrupadas = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        LocalDate startDate = inicio.toLocalDate();
        LocalDate endDate = fin.toLocalDate();

        try {
            switch (reportType.toLowerCase()) {
                case "diario":
                    SimpleDateFormat labelFormatDaily = new SimpleDateFormat("dd/MM/yyyy");
                    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                        String label = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        ventasAgrupadas.put(label, 0.0);
                    }
                    ventas.forEach(v -> {
                        try {
                            Date fecha = sdf.parse(v.getFechaYHora());
                            String label = labelFormatDaily.format(fecha);
                            ventasAgrupadas.merge(label, v.getTotal().doubleValue(), Double::sum);
                        } catch (ParseException e) {
                            System.err.println("Error al parsear fecha: " + v.getFechaYHora());
                        }
                    });
                    break;

                case "semanal":
                    // Agrupar por mes y semana dentro del mes
                    Map<String, Double> tempVentasAgrupadas = new TreeMap<>();
                    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                        // Obtener el mes y el año
                        String monthYear = date.format(DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es", "ES")));
                        // Obtener la semana del mes
                        int weekOfMonth = (date.getDayOfMonth() - 1) / 7 + 1;
                        String label = monthYear + " Semana " + weekOfMonth;
                        tempVentasAgrupadas.put(label, 0.0);
                    }
                    ventas.forEach(v -> {
                        try {
                            Date fecha = sdf.parse(v.getFechaYHora());
                            LocalDate ventaDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            String monthYear = ventaDate.format(DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es", "ES")));
                            int weekOfMonth = (ventaDate.getDayOfMonth() - 1) / 7 + 1;
                            String label = monthYear + " Semana " + weekOfMonth;
                            tempVentasAgrupadas.merge(label, v.getTotal().doubleValue(), Double::sum);
                        } catch (ParseException e) {
                            System.err.println("Error al parsear fecha: " + v.getFechaYHora());
                        }
                    });
                    // Filtrar etiquetas vacías si hay ventas
                    if (!ventas.isEmpty()) {
                        tempVentasAgrupadas.entrySet().stream()
                                .filter(entry -> entry.getValue() > 0)
                                .forEach(entry -> ventasAgrupadas.put(entry.getKey(), entry.getValue()));
                    } else {
                        ventasAgrupadas.putAll(tempVentasAgrupadas);
                    }
                    break;

                case "mensual":
                    SimpleDateFormat labelFormatMonthly = new SimpleDateFormat("MMM yyyy", new Locale("es", "ES"));
                    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusMonths(1)) {
                        String label = date.format(DateTimeFormatter.ofPattern("MMM yyyy", new Locale("es", "ES")));
                        ventasAgrupadas.put(label, 0.0);
                    }
                    ventas.forEach(v -> {
                        try {
                            Date fecha = sdf.parse(v.getFechaYHora());
                            String label = labelFormatMonthly.format(fecha);
                            ventasAgrupadas.merge(label, v.getTotal().doubleValue(), Double::sum);
                        } catch (ParseException e) {
                            System.err.println("Error al parsear fecha: " + v.getFechaYHora());
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ventasAgrupadas;
    }

    @Override
    public Map<String, Double> obtenerVentasPorCategoria(LocalDateTime inicio, LocalDateTime fin) {
        return Map.of();
    }

    @Override
    public Map<String, Double> obtenerVentasPorVendedor(LocalDateTime inicio, LocalDateTime fin) {
        List<Venta> ventas = findByFechaBetween(inicio, fin);

        return ventas.stream()
                .filter(v -> v.getTurno() != null && v.getTurno().getUsuario() != null)
                .collect(Collectors.groupingBy(
                        v -> v.getTurno().getUsuario().getUsername(),
                        Collectors.summingDouble(Venta::getTotal)
                ));
    }

    @Override
    public Map<String, Object> obtenerResumenVentas(LocalDateTime inicio, LocalDateTime fin) {
        List<Venta> ventas = findByFechaBetween(inicio, fin);

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalVentas", calcularTotalVentas(ventas));
        resumen.put("cantidadVentas", ventas.size());
        resumen.put("promedioVenta", ventas.isEmpty() ? 0 : calcularTotalVentas(ventas) / ventas.size());
        resumen.put("ventasPorVendedor", obtenerVentasPorVendedor(inicio, fin));

        return resumen;
    }
}