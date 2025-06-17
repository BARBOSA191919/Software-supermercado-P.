package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Carnes;
import com.gestion.inventario.entidades.Fruvert;
import com.gestion.inventario.repositorios.FruvertRepository;
import com.gestion.inventario.servicio.FruvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FruvertServicelmpl implements FruvertService {

    @Autowired
    private FruvertRepository fruvertRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Fruvert> findAll() {
        return fruvertRepository.findAll(); // Llama al repositorio para obtener todos los productos del fruver
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Fruvert> findAll(Pageable pageable) {
        return fruvertRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Fruvert fruvert) {
        fruvertRepository.save(fruvert);
    }

    @Override
    @Transactional(readOnly = true) // Asegúrate de que este método sea solo de lectura
    public Fruvert findOne(Integer id) {
        // Usar Optional para evitar NullPointerExceptions
        Optional<Fruvert> optionalFruvert = fruvertRepository.findById(id);
        return optionalFruvert.orElse(null); // Retorna el Fruvert si existe, o null si no
    }

    @Override
    public boolean existeCodigo(String codigo) {
        return fruvertRepository.existsByCodigo(codigo);
    }

    @Override
    public Fruvert findByCodigo(String codigo) {
        return fruvertRepository.findByCodigo(codigo);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        fruvertRepository.deleteById(id); // Elimina el Fruvert por ID
    }
}
