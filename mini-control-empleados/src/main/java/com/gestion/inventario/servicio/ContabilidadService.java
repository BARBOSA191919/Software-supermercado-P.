package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Contabilidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContabilidadService {



    public List<Contabilidad> findAll();

    public Page<Contabilidad> findAll(Pageable pageable);

    public void save(Contabilidad contabilidad);

    public Contabilidad findOne(Long id);

    public void delete(Long id);


    Contabilidad findById(Long id);
}
