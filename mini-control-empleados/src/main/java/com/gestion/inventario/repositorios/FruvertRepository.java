package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Categoria;
import com.gestion.inventario.entidades.Fruvert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;


@Repository
public interface FruvertRepository extends JpaRepository<Fruvert, Integer> {

    Fruvert findByCodigo(String codigo);
    Fruvert findByNombre(String nombre);
    List<Fruvert> findByCategoria(Categoria categoria);
    List<Fruvert> findByNombreContainingIgnoreCase(String nombre);
    List<Fruvert> findByCodigoContainingIgnoreCase(String codigo);
    boolean existsByCodigo(String codigo); // Este metodo debe estar correctamente definido

    List<Fruvert> findByNombreContainingOrCodigoContaining(String nombre, String codigo);
    // FruvertRepository.java
    Page<Fruvert> findByCategoria(Categoria categoria, Pageable pageable);

    Page<Fruvert> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(String nombre, String codigo, Pageable pageable);
}