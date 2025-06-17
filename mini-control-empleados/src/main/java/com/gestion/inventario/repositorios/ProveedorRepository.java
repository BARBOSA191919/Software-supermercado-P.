package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Page<Proveedor> findByNombreProveedorContainingIgnoreCase(String nombre, Pageable pageable);
    List<Proveedor> findByNombreProveedorContainingIgnoreCase(String nombre);
    @Query("SELECT p FROM Proveedor p WHERE LOWER(p.nombreProveedor) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Proveedor> findBySearch(@Param("search") String search, Pageable pageable);
}
