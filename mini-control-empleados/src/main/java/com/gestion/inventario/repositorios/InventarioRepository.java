package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Inventario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    List<Inventario> findByCodigoContainingIgnoreCase(String query);

    List<Inventario> findByNombreContainingIgnoreCase(String query);

    List<Inventario> findByCategoriaId(Long categoriaId);

    Page<Inventario> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(String query, String query1, Pageable pageable);
}