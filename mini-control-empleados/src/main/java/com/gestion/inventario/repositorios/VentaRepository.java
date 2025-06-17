package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Venta;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VentaRepository extends CrudRepository<Venta, Integer> {

    Optional<Venta> findByCodigoVenta(String codigoVenta);

    List<Venta> findByFechaBetween(Date inicio, Date fin);

    @Modifying
    @Query("DELETE FROM Venta v WHERE v.codigoVenta = :codigoVenta")
    void deleteByCodigoVenta(@Param("codigoVenta") String codigoVenta);

    // Metodo para buscar ventas entre fechas
    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    // Metodo adicional para búsqueda por código
    List<Venta> findByCodigoVentaContaining(String codigo);

    // Metodo para buscar ventas por vendedor
    @Query("SELECT v FROM Venta v WHERE v.turno.usuario.username LIKE %:username%")
    List<Venta> findByVendedor(@Param("username") String username);



}
