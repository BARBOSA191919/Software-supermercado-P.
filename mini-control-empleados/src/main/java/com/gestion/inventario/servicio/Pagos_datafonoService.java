package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Pagos_datafono;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Pagos_datafonoService {

    public List<Pagos_datafono> findAll();

    public Page<Pagos_datafono> findAll(Pageable pageable);

    public void save(Pagos_datafono pagos_datafono);

    public Pagos_datafono findOne(Long id);

    public void delete(Long id);
}

