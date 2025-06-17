package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.ReporteVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ReportesRepository extends JpaRepository<ReporteVenta, Integer> {

    @Query(value = "SELECT DISTINCT r FROM ReporteVenta r " +
            "LEFT JOIN FETCH r.venta v " +
            "LEFT JOIN FETCH v.productos " +
            "LEFT JOIN FETCH r.turno t " +
            "LEFT JOIN FETCH t.usuario u " +
            "WHERE (:fechaInicio IS NULL OR v.fechaYHora >= :fechaInicio) " +
            "AND (:fechaFin IS NULL OR v.fechaYHora <= :fechaFin) " +
            "AND (:vendedorNombre IS NULL OR u.username LIKE :vendedorNombre) " +
            "AND (:turnoNombre IS NULL OR t.nombre LIKE :turnoNombre) " +
            "AND (:codigoVenta IS NULL OR v.codigoVenta LIKE :codigoVenta)",
            countQuery = "SELECT COUNT(DISTINCT r) FROM ReporteVenta r " +
                    "LEFT JOIN r.venta v " +
                    "LEFT JOIN r.turno t " +
                    "LEFT JOIN t.usuario u " +
                    "WHERE (:fechaInicio IS NULL OR v.fechaYHora >= :fechaInicio) " +
                    "AND (:fechaFin IS NULL OR v.fechaYHora <= :fechaFin) " +
                    "AND (:vendedorNombre IS NULL OR u.username LIKE :vendedorNombre) " +
                    "AND (:turnoNombre IS NULL OR t.nombre LIKE :turnoNombre) " +
                    "AND (:codigoVenta IS NULL OR v.codigoVenta LIKE :codigoVenta)")
    Page<ReporteVenta> findAllWithFilters(
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin,
            @Param("vendedorNombre") String vendedorNombre,
            @Param("turnoNombre") String turnoNombre,
            @Param("codigoVenta") String codigoVenta,
            Pageable pageable);

    Optional<ReporteVenta> findByVentaId(Integer ventaId);

    Optional<ReporteVenta> findByVentaCodigoVenta(String codigoVenta);

    // Consulta personalizada para comparar solo la fecha (ignorando la hora)
    @Query("SELECT r FROM ReporteVenta r WHERE " +
            "(:fechaInicio IS NULL OR DATE(r.venta.fechaYHora) >= DATE(:fechaInicio)) AND " +
            "(:fechaFin IS NULL OR DATE(r.venta.fechaYHora) <= DATE(:fechaFin)) AND " +
            "(r.venta.codigoVenta LIKE %:codigoVenta% OR " +
            "r.turno.usuario.username LIKE %:vendedor% OR " +
            "r.venta.turno.nombre LIKE %:turno%)")
    Page<ReporteVenta> findByFechaYHoraAndFilters(
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin,
            @Param("codigoVenta") String codigoVenta,
            @Param("vendedor") String vendedor,
            @Param("turno") String turno,
            Pageable pageable);

}