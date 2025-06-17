package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Venta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public interface VentaService{

    public List<Venta> findAll();

    public void save(Venta venta);

    public Venta findOne(Long id);

    public void delete(Long id);

    Venta findById(Integer id);

    Float calcularTotalVentas(List<Venta> ventas);
    // Nuevos métodos para reportes
    public List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    public Map<String, Double> obtenerVentasAgrupadas(LocalDateTime inicio, LocalDateTime fin, String reportType);

    public Map<String, Double> obtenerVentasPorCategoria(LocalDateTime inicio, LocalDateTime fin);

    public Map<String, Double> obtenerVentasPorVendedor(LocalDateTime inicio, LocalDateTime fin);

    Map<String, Object> obtenerResumenVentas(LocalDateTime inicio, LocalDateTime fin);
}
