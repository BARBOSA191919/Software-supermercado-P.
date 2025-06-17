package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Compras;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ComprasService {

    List<Compras> findAll();

    Page<Compras> findAll(Pageable pageable);

    Page<Compras> findAllBySearch(String search, Pageable pageable);

    Page<Compras> findAllByFilters(String search, Date fechaInicio, Date fechaFin, Long proveedorId, Pageable pageable);

    void save(Compras compras);

    Compras findOne(Long id);

    void delete(Long id);

    Compras findById(int comprasId);

    Page<Compras> filtrarPorFechas(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);

    Double calcularTotalCompras(List<Compras> compras);

    Map<String, Double> calcularCostoPorProveedor(List<Compras> compras);

    Double calculateTotalByPeriod(LocalDate start, LocalDate end);
}