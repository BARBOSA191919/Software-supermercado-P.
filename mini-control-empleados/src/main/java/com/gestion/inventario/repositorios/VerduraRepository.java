package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Categoria;
import com.gestion.inventario.entidades.Verdura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface VerduraRepository extends JpaRepository<Verdura, Integer> {
    Verdura findByCodigo(String codigo);
    Verdura findByNombre(String nombre);

    List<Verdura> findByCategoria(Categoria categoria);

    List<Verdura> findByNombreContainingIgnoreCase(String nombre);
    List<Verdura> findByCodigoContainingIgnoreCase(String codigo);
    boolean existsByCodigo(String codigo);
    List<Verdura> findByNombreContainingOrCodigoContaining(String nombre, String codigo);
    // VerduraRepository.java
    Page<Verdura> findByCategoria(Categoria categoria, Pageable pageable);

    Page<Verdura> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(String nombre, String codigo, Pageable pageable);
}
