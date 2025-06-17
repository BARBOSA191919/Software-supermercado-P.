package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Carnes;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarnesService {

    public List<Carnes> findAll();

    public Page<Carnes> findAll(Pageable pageable);

    public void save (Carnes carnes);

    public Carnes findOne(Integer id);

    public void delete(Integer id);

    boolean existeCodigo(String codigo);

    Carnes findByCodigo(String codigo);
}
