package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Compras;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ComprasRepository extends JpaRepository<Compras, Long> {

    @Query("SELECT c FROM Compras c WHERE " +
            "(:search IS NULL OR LOWER(c.codigo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.proveedor.nombreProveedor) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:fechaInicio IS NULL OR c.fecha >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR c.fecha <= :fechaFin) AND " +
            "(:proveedorId IS NULL OR c.proveedor.id = :proveedorId)")
    Page<Compras> findByFilters(@Param("search") String search,
                                @Param("fechaInicio") Date fechaInicio,
                                @Param("fechaFin") Date fechaFin,
                                @Param("proveedorId") Long proveedorId,
                                Pageable pageable);

    @Query("SELECT c FROM Compras c WHERE " +
            "(:search IS NULL OR LOWER(c.codigo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.proveedor.nombreProveedor) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Compras> findBySearch(@Param("search") String search, Pageable pageable);

    @Query("SELECT c FROM Compras c WHERE " +
            "(:search IS NULL OR LOWER(c.codigo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.proveedor.nombreProveedor) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Compras> findBySearch(@Param("search") String search);
}