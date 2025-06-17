package com.gestion.inventario.servicio;


import com.gestion.inventario.entidades.Categoria;
import com.gestion.inventario.entidades.Inventario;
import com.gestion.inventario.entidades.Reporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReporteGService {

    Page<Reporte> findByCategoria(Categoria categoria, Pageable pageable);

    Optional<Reporte> findById(Long id);

    List<Reporte> findByCategoria_Id(Long categoriaId);
}