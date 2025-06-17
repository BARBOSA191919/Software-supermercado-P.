package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT e FROM Empleado e WHERE " +
            "LOWER(e.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.apellido) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.cedula) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.telefono) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Empleado> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCedulaContainingIgnoreCaseOrTelefonoContainingIgnoreCase(
            @Param("search") String search,
            @Param("search") String search2,
            @Param("search") String search3,
            @Param("search") String search4,
            Pageable pageable);

    @Query("SELECT e FROM Empleado e WHERE " +
            "LOWER(e.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.apellido) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.cedula) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.telefono) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Empleado> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCedulaContainingIgnoreCaseOrTelefonoContainingIgnoreCase(
            @Param("search") String search,
            @Param("search") String search2,
            @Param("search") String search3,
            @Param("search") String search4);
}