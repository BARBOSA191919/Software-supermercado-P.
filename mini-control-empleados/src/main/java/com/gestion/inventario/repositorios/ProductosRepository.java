package com.gestion.inventario.repositorios;


import com.gestion.inventario.entidades.Categoria;
import com.gestion.inventario.entidades.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductosRepository extends JpaRepository<Producto, Integer> {
    Producto findByCodigo(String codigo);
    Producto findByNombre(String nombre);
    List<Producto> findByCategoria(Categoria categoria);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByCodigoContainingIgnoreCase(String codigo);

    List<Producto> findByNombreContainingOrCodigoContaining(String nombre, String codigo);
    // ProductosRepository.java
    Page<Producto> findByCategoria(Categoria categoria, Pageable pageable);

    Page<Producto> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(String nombre, String codigo, Pageable pageable);
}
