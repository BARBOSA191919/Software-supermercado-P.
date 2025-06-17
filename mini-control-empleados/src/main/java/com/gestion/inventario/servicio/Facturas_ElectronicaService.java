package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Factura_Electronica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Facturas_ElectronicaService {

    public List<Factura_Electronica> findAll();

    public Page<Factura_Electronica> findAll(Pageable pageable);

    public void save(Factura_Electronica factura_Electronica);

    public Factura_Electronica findOne(Long id);

    public void delete(Long id);
}




