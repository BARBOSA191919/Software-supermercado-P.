package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Categoria;
import com.gestion.inventario.entidades.Inventario;
import com.gestion.inventario.entidades.Reporte;
import com.gestion.inventario.repositorios.CategoriaRepository;
import com.gestion.inventario.repositorios.ReporteGRepository;
import com.gestion.inventario.servicio.CategoriaService;
import com.gestion.inventario.servicio.ReporteGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ReporteGServiceImpl implements ReporteGService {

    @Override
    public Page<Reporte> findByCategoria(Categoria categoria, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Reporte> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Reporte> findByCategoria_Id(Long categoriaId) {
        return List.of();
    }
}