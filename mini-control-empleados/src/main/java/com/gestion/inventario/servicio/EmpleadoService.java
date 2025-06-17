package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {

    List<Empleado> listarEmpleados();

    Page<Empleado> listarEmpleados(Pageable pageable);

    Page<Empleado> findAllBySearch(String search, Pageable pageable);

    Optional<Empleado> obtenerEmpleadoPorId(Long id);

    Empleado guardarEmpleado(Empleado empleado);

    void eliminarEmpleado(Long id);

    List<Empleado> findAll();

    Empleado findById(Long empleadoId);
}