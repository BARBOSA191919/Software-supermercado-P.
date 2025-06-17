package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ProveedorService {

    public List<Proveedor> findAll();

    public Page<Proveedor> findAll(Pageable pageable);

    public void save(Proveedor proveedor);

    public Proveedor findOne(Long id);

    public void delete(Long id);

    Page<Proveedor> findAllBySearch(String search, Pageable pageable);

    Map<Long, Double> calculateTotalComprasByPeriod(List<Proveedor> proveedores, LocalDate fechaInicio, LocalDate fechaFin);

    Page<Proveedor> findAllBySearchAndDateRange(String search, LocalDate fechaInicio, LocalDate fechaFin, Pageable pageRequest);
}

