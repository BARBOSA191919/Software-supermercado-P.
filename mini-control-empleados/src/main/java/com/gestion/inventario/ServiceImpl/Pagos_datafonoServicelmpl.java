package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Pagos_datafono;
import com.gestion.inventario.repositorios.Pagos_datafonoRepository;
import com.gestion.inventario.servicio.Pagos_datafonoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Pagos_datafonoServicelmpl implements Pagos_datafonoService {

    @Autowired
    private Pagos_datafonoRepository pagos_datafonoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Pagos_datafono> findAll() {
        return pagos_datafonoRepository.findAll(); // Llama al repositorio para obtener todos los productos
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pagos_datafono> findAll(Pageable pageable) {
        return pagos_datafonoRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Pagos_datafono pagos_datafono) {
        pagos_datafonoRepository.save(pagos_datafono);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        pagos_datafonoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagos_datafono findOne(Long id) {
        return pagos_datafonoRepository.findById(id).orElse(null);
    }
}
