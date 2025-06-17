package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoriaService {
    public List<Categoria> findAll();

    public Page<Categoria> findAll(Pageable pageable);

    public void save(Categoria categoria);

    public Categoria findOne(Long id);

    public void delete(Long id);
}