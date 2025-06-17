package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Empleado;
import com.gestion.inventario.repositorios.EmpleadoRepository;
import com.gestion.inventario.servicio.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> listarEmpleados() {
        return empleadoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empleado> listarEmpleados(Pageable pageable) {
        return empleadoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empleado> findAllBySearch(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            if (pageable == null) {
                List<Empleado> resultados = empleadoRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCedulaContainingIgnoreCaseOrTelefonoContainingIgnoreCase(search, search, search, search);
                return new PageImpl<>(resultados);
            }
            return empleadoRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCedulaContainingIgnoreCaseOrTelefonoContainingIgnoreCase(search, search, search, search, pageable);
        }
        if (pageable == null) {
            return new PageImpl<>(empleadoRepository.findAll());
        }
        return empleadoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empleado> obtenerEmpleadoPorId(Long id) {
        return empleadoRepository.findById(id);
    }

    @Override
    @Transactional
    public Empleado guardarEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    @Transactional
    public void eliminarEmpleado(Long id) {
        empleadoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Empleado findById(Long empleadoId) {
        return empleadoRepository.findById(empleadoId).orElse(null);
    }
}