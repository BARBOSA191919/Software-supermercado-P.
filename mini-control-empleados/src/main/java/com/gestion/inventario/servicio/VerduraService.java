package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Carnes;
import com.gestion.inventario.entidades.Fruvert;
import com.gestion.inventario.entidades.Verdura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VerduraService {

    public List<Verdura> findAll();

    public Page<Verdura> findAll(Pageable pageable);

    public void save(Verdura verdura);

    public Verdura findOne(Integer id);

    public void delete(Integer id);

    boolean existeCodigo(String codigo);

    Verdura findByCodigo(String codigo);

}


