package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Carnes;
import com.gestion.inventario.entidades.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface CarnesRepository extends JpaRepository<Carnes, Integer> {

    Carnes findByCodigo(String codigo);   // Cambia el nombre aquí
    Carnes findByNombre(String nombre);// Cambia el nombre aquí
    List<Carnes> findByCategoria(Categoria categoria);
    List<Carnes> findByNombreContainingIgnoreCase(String nombre);
    List<Carnes> findByCodigoContainingIgnoreCase(String codigo);
    boolean existsByCodigo(String codigo); // Este metodo debe estar correctamente definido

    List<Carnes> findByNombreContainingOrCodigoContaining(String nombre, String codigo);

    // CarnesRepository.java
    Page<Carnes> findByCategoria(Categoria categoria, Pageable pageable);

    Page<Carnes> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(String nombre, String codigo, Pageable pageable);
}
