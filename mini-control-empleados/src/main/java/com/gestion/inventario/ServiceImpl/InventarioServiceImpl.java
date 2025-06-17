package com.gestion.inventario.ServiceImpl;

import java.util.List;
import java.util.Optional;

import com.gestion.inventario.entidades.Categoria;
import com.gestion.inventario.servicio.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestion.inventario.entidades.Inventario;
import com.gestion.inventario.repositorios.InventarioRepository;

@Service
public class InventarioServiceImpl implements InventarioService {

	@Autowired
	private InventarioRepository inventarioRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Inventario> findAll() {
		return (List<Inventario>) inventarioRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Inventario> findAll(Pageable pageable) {
		return inventarioRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(Inventario inventario) {
		try {
			inventarioRepository.save(inventario);
		} catch (Exception e) {
			// Manejar la excepción o lanzar una excepción personalizada
			throw new RuntimeException("Error al guardar el inventario", e);
		}
	}

	@Override
	@Transactional
	public void delete(Long id) {
		try {
			inventarioRepository.deleteById(id);
		} catch (Exception e) {
			// Manejar la excepción o lanzar una excepción personalizada
			throw new RuntimeException("Error al eliminar el inventario", e);
		}
	}

	@Override
	public Page<Inventario> findByCategoria(Categoria categoria, Pageable pageable) {
		return null;
	}

	@Override
	public Optional<Inventario> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public List<Inventario> findByCategoria_Id(Long categoriaId) {
		return List.of();
	}


	@Override
	@Transactional(readOnly = true)
	public Inventario findOne(Long id) {
		return inventarioRepository.findById(id).orElse(null);
	}

	// Métodos adicionales, si es necesario

	public Inventario obtenerInventario(Long id) {
		return inventarioRepository.findById(id).orElse(null);
	}

	public void guardarInventario(Inventario inventario) {
		inventarioRepository.save(inventario);
	}
}
