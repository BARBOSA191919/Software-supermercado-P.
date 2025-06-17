package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.ReporteVenta;
import com.gestion.inventario.entidades.Turno;
import com.gestion.inventario.entidades.Venta;
import com.gestion.inventario.repositorios.ReportesRepository;
import com.gestion.inventario.repositorios.VentaRepository;
import com.gestion.inventario.servicio.ReporteVentaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ReporteVentaServiceImpl implements ReporteVentaService {

    private static final Logger log = LoggerFactory.getLogger(ReporteVentaServiceImpl.class);

    @Autowired
    private ReportesRepository reportesRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    @Transactional
    public ReporteVenta registrarReporteVenta(Venta venta) {
        ReporteVenta reporteVenta = new ReporteVenta();
        reporteVenta.setFechaReporte(new Date());
        reporteVenta.setTotalVentas(venta.getTotal());
        reporteVenta.setCantidadProductosVendidos(venta.getProductos().size());
        reporteVenta.setVenta(venta);
        reporteVenta.setTurno(venta.getTurno());
        return reportesRepository.save(reporteVenta);
    }

    @Override
    @Transactional
    public void generarReportePorTurno(Turno turno) {
        double totalVentas = turno.getVentas().stream().mapToDouble(Venta::getTotal).sum();
        int cantidadProductosVendidos = turno.getVentas().stream()
                .mapToInt(venta -> venta.getProductos().size()).sum();

        ReporteVenta reporteVenta = new ReporteVenta();
        reporteVenta.setFechaReporte(new Date());
        reporteVenta.setTotalVentas((float) totalVentas);
        reporteVenta.setCantidadProductosVendidos(cantidadProductosVendidos);
        reporteVenta.setTurno(turno);
        reportesRepository.save(reporteVenta);
    }

    @Override
    public Page<ReporteVenta> buscarReportesConFiltros(Date fechaInicio, Date fechaFin, String vendedorNombre, String turnoNombre, String codigoVenta, Pageable pageable) {
        String codigoVentaSafe = codigoVenta != null ? codigoVenta : "";
        String turnoNombreSafe = turnoNombre != null ? turnoNombre : "";
        String vendedorNombreSafe = vendedorNombre != null ? vendedorNombre : "";

        log.info("Buscando reportes con filtros - Fecha Inicio: {}, Fecha Fin: {}, Código: {}, Vendedor: {}, Turno: {}",
                fechaInicio, fechaFin, codigoVentaSafe, vendedorNombreSafe, turnoNombreSafe);

        Page<ReporteVenta> result = reportesRepository.findByFechaYHoraAndFilters(
                fechaInicio, fechaFin, codigoVentaSafe, vendedorNombreSafe, turnoNombreSafe, pageable);

        log.info("Resultados encontrados: {}", result.getTotalElements());
        return result;
    }

    @Override
    public ReporteVenta obtenerReportePorVenta(Integer ventaId) {
        return reportesRepository.findByVentaId(ventaId).orElse(null);
    }



    @Override
    public void eliminarReportePorVenta(String codigoVenta) {
        ReporteVenta reporte = reportesRepository.findByVentaCodigoVenta(codigoVenta)
                .orElseThrow(() -> new IllegalArgumentException("Reporte no encontrado para código: " + codigoVenta));
        reportesRepository.delete(reporte);
    }
}