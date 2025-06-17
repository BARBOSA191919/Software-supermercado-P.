package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Contabilidad;
import com.gestion.inventario.repositorios.ContabilidadRepository;
import com.gestion.inventario.servicio.ContabilidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContabilidadServicelmpl  implements ContabilidadService {

    @Autowired
    private ContabilidadRepository contabilidadRepository;


    public Contabilidad findById(Long id) {
        Optional<Contabilidad> optionalContabilidad = contabilidadRepository.findById(id);
        return optionalContabilidad.orElse(null); // Devuelve null si no se encuentra
    }

    @Override
    public List<Contabilidad> findAll() {
        return contabilidadRepository.findAll();
    }

    @Override
    public Page<Contabilidad> findAll(Pageable pageable) {
        return contabilidadRepository.findAll(pageable);
    }

    @Override
    public void save(Contabilidad contabilidad) {
        contabilidadRepository.save(contabilidad);
    }

    @Override
    public Contabilidad findOne(Long id) {
        return contabilidadRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        contabilidadRepository.deleteById(id);
    }
}


