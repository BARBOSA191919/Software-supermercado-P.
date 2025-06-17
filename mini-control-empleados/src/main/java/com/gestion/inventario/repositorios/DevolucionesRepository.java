package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Devoluciones;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DevolucionesRepository extends JpaRepository<Devoluciones, Long> {

    // Métodos con paginación (ya existentes)
    Page<Devoluciones> findAll(Pageable pageable);

    Page<Devoluciones> findByNombreProductoContaining(String search, Pageable pageable);

    Page<Devoluciones> findByMotivoContaining(String motivo, Pageable pageable);

    Page<Devoluciones> findByFechaBetween(Date fechaInicio, Date fechaFin, Pageable pageable);

    Page<Devoluciones> findByNombreProductoContainingAndMotivoContaining(String search, String motivo, Pageable pageable);

    Page<Devoluciones> findByNombreProductoContainingAndFechaBetween(String search, Date fechaInicio, Date fechaFin, Pageable pageable);

    Page<Devoluciones> findByMotivoContainingAndFechaBetween(String motivo, Date fechaInicio, Date fechaFin, Pageable pageable);

    Page<Devoluciones> findByNombreProductoContainingAndMotivoContainingAndFechaBetween(
            String search, String motivo, Date fechaInicio, Date fechaFin, Pageable pageable);

    // Métodos sin paginación (nuevos)
    List<Devoluciones> findAll();

    List<Devoluciones> findByNombreProductoContaining(String search);

    List<Devoluciones> findByMotivoContaining(String motivo);

    List<Devoluciones> findByFechaBetween(Date fechaInicio, Date fechaFin);

    List<Devoluciones> findByNombreProductoContainingAndMotivoContaining(String search, String motivo);

    List<Devoluciones> findByNombreProductoContainingAndFechaBetween(String search, Date fechaInicio, Date fechaFin);

    List<Devoluciones> findByMotivoContainingAndFechaBetween(String motivo, Date fechaInicio, Date fechaFin);

    List<Devoluciones> findByNombreProductoContainingAndMotivoContainingAndFechaBetween(
            String search, String motivo, Date fechaInicio, Date fechaFin);
}