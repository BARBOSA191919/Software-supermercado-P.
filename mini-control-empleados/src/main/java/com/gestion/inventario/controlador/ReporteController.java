package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.ReporteVenta;
import com.gestion.inventario.servicio.ReporteVentaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private static final Logger log = LoggerFactory.getLogger(ReporteController.class);

    @Autowired
    private ReporteVentaService reporteVentaService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public String mostrarReportes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            Model model) {

        Date fechaInicioDate = null;
        Date fechaFinDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (fechaInicio != null && !fechaInicio.isEmpty()) {
                fechaInicioDate = sdf.parse(fechaInicio);
                log.info("Fecha Inicio recibida: {}", fechaInicioDate);
            }
            if (fechaFin != null && !fechaFin.isEmpty()) {
                fechaFinDate = sdf.parse(fechaFin);
                fechaFinDate = new Date(fechaFinDate.getTime() + 86399000); // Fin del día
                log.info("Fecha Fin recibida: {}", fechaFinDate);
            }
        } catch (Exception e) {
            log.warn("Formato de fecha inválido: inicio={}, fin={}", fechaInicio, fechaFin, e);
            model.addAttribute("error", "Formato de fecha inválido. Use YYYY-MM-DD.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "venta.fechaYHora"));
        Page<ReporteVenta> reportesPage;
        try {
            reportesPage = reporteVentaService.buscarReportesConFiltros(fechaInicioDate, fechaFinDate, search, search, search, pageable);
            log.info("Reportes encontrados: {}", reportesPage.getTotalElements());
        } catch (Exception e) {
            log.error("Error al buscar reportes: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar los reportes: " + e.getMessage());
            reportesPage = Page.empty(pageable);
        }

        model.addAttribute("reportes", reportesPage.getContent());
        model.addAttribute("currentPage", reportesPage.getNumber());
        model.addAttribute("totalPages", reportesPage.getTotalPages());
        model.addAttribute("totalItems", reportesPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("search", search);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("titulo", "Gestión de Reportes de Ventas");

        String acceptHeader = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(acceptHeader)) {
            return "Reportexventa/Reporte :: #tableContent";
        }

        return "Reportexventa/Reporte";
    }

    @GetMapping("/api/listar")
    @ResponseBody
    public ResponseEntity<Page<ReporteVenta>> listarReportes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {
        try {
            Date fechaInicioDate = null;
            Date fechaFinDate = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (fechaInicio != null && !fechaInicio.isEmpty()) {
                fechaInicioDate = sdf.parse(fechaInicio);
                log.info("Fecha Inicio (API): {}", fechaInicioDate);
            }
            if (fechaFin != null && !fechaFin.isEmpty()) {
                fechaFinDate = sdf.parse(fechaFin);
                fechaFinDate = new Date(fechaFinDate.getTime() + 86399000); // Fin del día
                log.info("Fecha Fin (API): {}", fechaFinDate);
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "venta.fechaYHora"));
            Page<ReporteVenta> reportes = reporteVentaService.buscarReportesConFiltros(fechaInicioDate, fechaFinDate, search, search, search, pageable);
            log.info("Reportes obtenidos (API): {}", reportes.getTotalElements());
            return ResponseEntity.ok(reportes);
        } catch (Exception e) {
            log.error("Error al listar reportes: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/factura/{id}")
    @ResponseBody
    public ResponseEntity<ReporteVenta> obtenerFactura(@PathVariable Integer id) {
        try {
            log.info("Obteniendo factura para id: {}", id);
            ReporteVenta reporte = reporteVentaService.obtenerReportePorVenta(id);
            if (reporte == null) {
                log.warn("Reporte no encontrado para id={}", id);
                return ResponseEntity.status(404).body(null);
            }
            log.info("Reporte encontrado: {}", reporte.getVenta().getCodigoVenta());
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            log.error("Error al obtener factura para id={}", id, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/eliminar/{codigoVenta}")
    @ResponseBody
    public ResponseEntity<String> eliminarReporte(@PathVariable String codigoVenta) {
        try {
            reporteVentaService.eliminarReportePorVenta(codigoVenta);
            return ResponseEntity.ok("Venta eliminada exitosamente");
        } catch (IllegalArgumentException e) {
            log.warn("Reporte no encontrado: {}", codigoVenta);
            return ResponseEntity.status(404).body("Reporte no encontrado: " + codigoVenta);
        } catch (Exception e) {
            log.error("Error al eliminar reporte: {}", codigoVenta, e);
            return ResponseEntity.status(500).body("Error interno al eliminar: " + e.getMessage());
        }
    }
    @GetMapping("/byId/{id}")
    @ResponseBody
    public ResponseEntity<ReporteVenta> obtenerReportePorId(@PathVariable Integer id) {
        try {
            ReporteVenta reporte = reporteVentaService.obtenerReportePorVenta(id);
            if (reporte == null) {
                return ResponseEntity.status(404).body(null);
            }
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            log.error("Error al obtener reporte por id={}", id, e);
            return ResponseEntity.status(500).body(null);
        }
    }
}