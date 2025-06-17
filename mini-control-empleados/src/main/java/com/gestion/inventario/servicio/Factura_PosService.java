package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Factura_pos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Factura_PosService {
    public List<Factura_pos> findAll();

    public Page<Factura_pos> findAll(Pageable pageable);

    public void save(Factura_pos factura_pos);

    public Factura_pos findOne(Long id);

    public void delete(Long id);
}


