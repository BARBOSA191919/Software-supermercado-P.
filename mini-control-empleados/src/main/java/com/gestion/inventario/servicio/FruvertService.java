package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Carnes;
import com.gestion.inventario.entidades.Fruvert;
import com.gestion.inventario.entidades.Verdura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FruvertService {

    public List<Fruvert> findAll();

    public Page<Fruvert> findAll(Pageable pageable);

    public void save(Fruvert fruvert);

    public Fruvert findOne(Integer id);

    public void delete(Integer id);

    boolean existeCodigo(String codigo);

    Fruvert findByCodigo(String codigo);


}
