package com.gestion.inventario.servicio;

import java.util.List;
import java.util.Optional;

import com.gestion.inventario.entidades.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gestion.inventario.entidades.Inventario;

public interface InventarioService {

	public List<Inventario> findAll();

	public Page<Inventario> findAll(Pageable pageable);

	public void save(Inventario inventario);

	public Inventario findOne(Long id);

	public void delete(Long id);


    Page<Inventario> findByCategoria(Categoria categoria, Pageable pageable);

    Optional<Inventario> findById(Long id);

	List<Inventario> findByCategoria_Id(Long categoriaId);


}

