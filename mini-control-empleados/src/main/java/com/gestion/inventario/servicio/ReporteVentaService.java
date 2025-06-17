package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.ReporteVenta;
import com.gestion.inventario.entidades.Turno;
import com.gestion.inventario.entidades.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface ReporteVentaService {

    ReporteVenta registrarReporteVenta(Venta venta);
    void generarReportePorTurno(Turno turno);
    void eliminarReportePorVenta(String codigoVenta);
    ReporteVenta obtenerReportePorVenta(Integer ventaId);
    Page<ReporteVenta> buscarReportesConFiltros(
            Date fechaInicio,
            Date fechaFin,
            String vendedorNombre,
            String turnoNombre, // Nuevo parámetro para buscar por nombre del turno
            String codigoVenta,
            Pageable pageable);
}