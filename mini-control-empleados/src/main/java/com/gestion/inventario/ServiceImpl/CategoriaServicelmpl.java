package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Categoria;
import com.gestion.inventario.repositorios.CategoriaRepository;
import com.gestion.inventario.servicio.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServicelmpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> findAll() {
        return categoriaRepository.findAll(); // Llama al repositorio para obtener todos los productos
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Categoria> findAll(Pageable pageable) {
        return categoriaRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        categoriaRepository.deleteById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public Categoria findOne(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

}
