package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Categoria;
import com.gestion.inventario.entidades.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReporteGRepository extends JpaRepository<Reporte, Long> {
    List<Categoria> findByCategoria(Categoria categoria);

}