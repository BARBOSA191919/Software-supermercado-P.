package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.*;

import com.gestion.inventario.repositorios.*;
import com.gestion.inventario.servicio.ClienteService;
import com.gestion.inventario.servicio.TurnoService;
import com.gestion.inventario.servicio.VentaService;
import com.gestion.inventario.util.paginacion.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/ventas")
public class VentaController {

    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    VentaService ventaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductosVendidosRepository productosVendidosRepository;

    @Autowired
    private FruvertRepository fruvertRepository;

    @Autowired
    private VerduraRepository verduraRepository;

    @Autowired
    private CarnesRepository carnesRepository;
    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private TurnoService turnoService;


    @Autowired
    private TurnoRepository turnoRepository;

    @GetMapping(value = "/")
    public String mostrarVentas(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username);

        List<Venta> ventas = new ArrayList<>();
        ventaRepository.findAll().forEach(ventas::add);
        Collections.reverse(ventas);

        List<Cliente> clientes = clienteService.findAll();
        List<Turno> listaTurnos = turnoRepository.findByUsuario_Username(username);
        model.addAttribute("turnos", listaTurnos);

        Turno turnoSeleccionado = listaTurnos.stream()
                .findFirst()
                .orElse(null);
        model.addAttribute("turnoSeleccionado", turnoSeleccionado);
        model.addAttribute("clientes", clientes);
        model.addAttribute("ventas", ventas);


        return "ventas/ver_ventas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarVenta(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            // Buscar la venta por su ID
            Venta venta = ventaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

            // Recuperar los productos vendidos en la venta
            Set<ProductoVendido> productosSet = venta.getProductos();
            List<ProductoVendido> productosVendidos = new ArrayList<>(productosSet);

            // Devolver las cantidades al inventario
            for (ProductoVendido productoVendido : productosVendidos) {
                // Buscar el producto en el inventario
                Producto p = productosRepository.findByCodigo(productoVendido.getCodigo());
                Fruvert fruta = fruvertRepository.findByCodigo(productoVendido.getCodigo());
                Verdura verdura = verduraRepository.findByCodigo(productoVendido.getCodigo());
                Carnes carnes = carnesRepository.findByCodigo(productoVendido.getCodigo());

                // Devolver la cantidad al inventario
                if (p != null) {
                    p.sumarExistencia(productoVendido.getCantidad()); // Método para sumar la existencia
                    productosRepository.save(p);
                } else if (fruta != null) {
                    fruta.sumarExistencia(productoVendido.getCantidad());
                    fruvertRepository.save(fruta);
                } else if (verdura != null) {
                    verdura.sumarExistencia(productoVendido.getCantidad());
                    verduraRepository.save(verdura);
                } else if (carnes != null) {
                    carnes.sumarExistencia(productoVendido.getCantidad());
                    carnesRepository.save(carnes);
                }
            }

            // Eliminar la venta
            ventaRepository.delete(venta);

            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("mensaje", "Venta eliminada correctamente y productos devueltos al inventario.");
            // Redirigir con un parámetro de éxito
            return "redirect:/ventas/?eliminado=true";
        } catch (Exception e) {
            // Manejo de errores
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar la venta: " + e.getMessage());
        }

        return "redirect:/ventas/";
    }


    @GetMapping("/editar/{id}")
    public String editarVenta(@PathVariable Integer id, Model model, HttpSession session, Principal principal) {
        String username = principal.getName(); // Obtén el nombre del usuario autenticado
        model.addAttribute("username", username);

        try {
            Venta venta = ventaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

            // Convertir productos vendidos a productos para vender
            List<ProductoParaVender> carrito = venta.getProductos().stream()
                    .map(productoVendido -> {
                        ProductoParaVender producto = new ProductoParaVender(
                                productoVendido.getNombre(),
                                productoVendido.getCodigo(),
                                productoVendido.getPrecio(),
                                productoVendido.getPrecio_final(),
                                productoVendido.getCantidad(),
                                productoVendido.getIva(),
                                productoVendido.getDescuento(),
                                productoVendido.getId(),
                                productoVendido.getCantidad()
                        );
                        return producto;
                    })
                    .collect(Collectors.toList());

            // Limpiar la sesión anterior
            session.removeAttribute("carrito");
            session.removeAttribute("ventaId");

            // Establecer nuevos valores en la sesión
            session.setAttribute("carrito", carrito);
            session.setAttribute("ventaId", id);


            // Obtener turnos del usuario
            List<Turno> listaTurnos = turnoRepository.findByUsuario_Username(username);
            model.addAttribute("turnos", listaTurnos);

            // Seleccionar turno del usuario
            Turno turnoSeleccionado = listaTurnos.stream()
                    .findFirst()
                    .orElse(null);
            model.addAttribute("turnoSeleccionado", turnoSeleccionado); // Agregar el turno seleccionado al modelo


            // Obtener la lista de clientes
            // Agregar atributos al modelo
            model.addAttribute("producto", new Producto());
            model.addAttribute("carrito", carrito);
            model.addAttribute("subtotal", venta.getSubtotal());
            model.addAttribute("total", venta.getTotal());
            model.addAttribute("clientes", clienteRepository.findAll());
            model.addAttribute("venta", venta);
            model.addAttribute("ventaId", id);

            return "vender/vender";

        } catch (Exception e) {
            // Log el error
            e.printStackTrace();
            // Redirigir en caso de error
            return "redirect:/ventas?error=true";
        }
    }

    private LocalDate[] configurarFechasReporte(String reportType) {
        LocalDate hoy = LocalDate.now(); // 02/06/2025
        switch (reportType.toLowerCase()) {
            case "semanal":
                return new LocalDate[]{hoy.minusDays(14), hoy}; // Últimas 2 semanas
            case "mensual":
                return new LocalDate[]{hoy.minusMonths(1), hoy}; // Último mes completo
            default: // diario
                return new LocalDate[]{LocalDate.of(2025, 1, 1), hoy}; // Desde 01/01/2025 hasta hoy
        }
    }

    @GetMapping("/grafico")
    @ResponseBody
    public Map<String, Object> obtenerDatosGrafico(
            @RequestParam(name = "fechaInicio", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
            @RequestParam(name = "reportType", defaultValue = "diario") String reportType) {

        LocalDate[] fechas = configurarFechasReporte(reportType);
        if (fechaInicio == null) fechaInicio = fechas[0];
        if (fechaFin == null) fechaFin = fechas[1];

        System.out.println("Rango de fechas: " + fechaInicio + " a " + fechaFin + " (Tipo: " + reportType + ")");

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        Map<String, Double> ventasAgrupadas = ventaService.obtenerVentasAgrupadas(inicio, fin, reportType);
        System.out.println("Ventas agrupadas: " + ventasAgrupadas);

        Map<String, Object> response = new HashMap<>();
        String[] labels = ventasAgrupadas.keySet().toArray(new String[0]);
        Double[] data = ventasAgrupadas.values().toArray(new Double[0]);
        response.put("labels", labels);
        response.put("data", data);
        response.put("reportType", reportType);

        System.out.println("Respuesta del gráfico: labels=" + Arrays.toString(labels) + ", data=" + Arrays.toString(data));
        return response;
    }


    @GetMapping("/resumen")
    @ResponseBody
    public Map<String, Object> obtenerResumenVentas(
            @RequestParam(name = "fechaInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam(name = "fechaFin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin) {

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        return ventaService.obtenerResumenVentas(inicio, fin);
    }


    @GetMapping("/Contabilidad")
    public String mostrarContabilidadVentas(
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String reportType,
            Model model,
            Principal principal) {

        String username = principal.getName();
        model.addAttribute("username", username);

        // Obtener todas las ventas
        List<Venta> ventas = ventaService.findAll();
        System.out.println("Ventas encontradas: " + ventas.size()); // Depuración

        // Aplicar filtros
        if (fecha != null && !fecha.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date fechaDate = sdf.parse(fecha);
                ventas = ventas.stream()
                        .filter(v -> {
                            try {
                                return sdf.parse(v.getFechaYHora().split(" ")[0]).equals(fechaDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (fechaInicio != null && fechaFin != null && !fechaInicio.isEmpty() && !fechaFin.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date startDate = sdf.parse(fechaInicio);
                Date endDate = sdf.parse(fechaFin);
                ventas = ventas.stream()
                        .filter(v -> {
                            try {
                                Date ventaDate = sdf.parse(v.getFechaYHora().split(" ")[0]);
                                return !ventaDate.before(startDate) && !ventaDate.after(endDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (search != null && !search.isEmpty()) {
            ventas = ventas.stream()
                    .filter(v -> v.getCodigoVenta().contains(search) ||
                            (v.getTurno() != null && v.getTurno().getUsuario() != null && v.getTurno().getUsuario().getUsername().contains(search)))
                    .collect(Collectors.toList());
        }

        Collections.reverse(ventas);

        // Paginación
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), ventas.size());
        List<Venta> pagedVentas = ventas.isEmpty() ? ventas : ventas.subList(start, end);
        int totalPages = (int) Math.ceil((double) ventas.size() / size);

        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(totalPages - 1, page + 2);
        if (endPage - startPage < 4) {
            if (startPage == 0) endPage = Math.min(4, totalPages - 1);
            else if (endPage == totalPages - 1) startPage = Math.max(0, totalPages - 5);
        }

        Float totalVentas = ventaService.calcularTotalVentas(ventas);
        List<Cliente> clientes = clienteService.findAll();
        List<Turno> listaTurnos = turnoRepository.findByUsuario_Username(username);
        Turno turnoSeleccionado = listaTurnos.stream().findFirst().orElse(null);

        model.addAttribute("ventas", pagedVentas);
        model.addAttribute("totalVentas", totalVentas);
        model.addAttribute("clientes", clientes);
        model.addAttribute("turnos", listaTurnos);
        model.addAttribute("turnoSeleccionado", turnoSeleccionado);
        model.addAttribute("fecha", fecha);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("reportType", reportType != null ? reportType : "diario");

        return "Contabilidad";
    }
}