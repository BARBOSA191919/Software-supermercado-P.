package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Factura_Electronica;
import com.gestion.inventario.repositorios.Factura_ElectronicaRepository;
import com.gestion.inventario.servicio.Facturas_ElectronicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Factura_ElectronicaServicelmlp implements Facturas_ElectronicaService {


    @Autowired
    private Factura_ElectronicaRepository facturaelectronica;

    @Override
    @Transactional(readOnly = true)
    public List<Factura_Electronica> findAll() {
        return facturaelectronica.findAll(); // Llama al repositorio para obtener todos los productos
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Factura_Electronica> findAll(Pageable pageable) {
        return facturaelectronica.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Factura_Electronica factura_Electronica) {
        facturaelectronica.save(factura_Electronica);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        facturaelectronica.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Factura_Electronica findOne(Long id) {
        return facturaelectronica.findById(id).orElse(null);
    }
}
