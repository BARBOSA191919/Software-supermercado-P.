package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Carnes;
import com.gestion.inventario.repositorios.CarnesRepository;
import com.gestion.inventario.servicio.CarnesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarnesServiceImpl implements CarnesService {

    @Autowired
    private CarnesRepository carnesRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Carnes> findAll() {
        return carnesRepository.findAll(); // Llama al repositorio para obtener todos los productos de carnes
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Carnes> findAll(Pageable pageable) {
        return carnesRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Carnes carnes) {
        carnesRepository.save(carnes);
    }

    @Override
    @Transactional(readOnly = true) // Asegúrate de que este metodo sea solo de lectura
    public Carnes findOne(Integer id) {
        // Usar Optional para evitar NullPointerExceptions
        Optional<Carnes> optionalCarnes = carnesRepository.findById(id);
        return optionalCarnes.orElse(null); // Retorna la Verdura si existe, o null si no
    }

    @Override
    public boolean existeCodigo(String codigo) {
        return carnesRepository.existsByCodigo(codigo);
    }

    @Override
    public Carnes findByCodigo(String codigo) {
        return carnesRepository.findByCodigo(codigo);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        carnesRepository.deleteById(id); // Elimina la Verdura por ID
    }
}
