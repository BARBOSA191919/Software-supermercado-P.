package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Factura_pos;
import com.gestion.inventario.repositorios.Factura_PosRepository;
import com.gestion.inventario.servicio.Factura_PosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class Factura_PosServiceimpl implements Factura_PosService {

    @Autowired
    private Factura_PosRepository factura_posRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Factura_pos> findAll() {
        return factura_posRepository.findAll(); // Llama al repositorio para obtener todos los productos
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Factura_pos> findAll(Pageable pageable) {
        return factura_posRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Factura_pos factura_pos) {
        factura_posRepository.save(factura_pos);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        factura_posRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Factura_pos findOne(Long id) {
        return factura_posRepository.findById(id).orElse(null);
    }
}