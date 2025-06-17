package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.ProductoVendido;
import com.gestion.inventario.entidades.Venta;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface ProductosVendidosRepository extends CrudRepository<ProductoVendido, Integer> {

}