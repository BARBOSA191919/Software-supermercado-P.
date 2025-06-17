package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Fruvert;
import com.gestion.inventario.entidades.Verdura;
import com.gestion.inventario.repositorios.VerduraRepository;
import com.gestion.inventario.servicio.VerduraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VerduraServiceImpl implements VerduraService {

    @Autowired
    private VerduraRepository verduraRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Verdura> findAll() {
        return verduraRepository.findAll(); // Llama al repositorio para obtener todos los productos de verduras
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Verdura> findAll(Pageable pageable) {
        return verduraRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Verdura verdura) {
        verduraRepository.save(verdura);
    }

    @Override
    @Transactional(readOnly = true) // Asegúrate de que este método sea solo de lectura
    public Verdura findOne(Integer id) {
        // Usar Optional para evitar NullPointerExceptions
        Optional<Verdura> optionalVerdura = verduraRepository.findById(id);
        return optionalVerdura.orElse(null); // Retorna la Verdura si existe, o null si no
    }

    @Override
    public boolean existeCodigo(String codigo) {
        return verduraRepository.existsByCodigo(codigo);
    }
    @Override
    public Verdura findByCodigo(String codigo) {
        return verduraRepository.findByCodigo(codigo);
    }


    @Override
    @Transactional
    public void delete(Integer id) {
        verduraRepository.deleteById(id); // Elimina la Verdura por ID
    }
}
