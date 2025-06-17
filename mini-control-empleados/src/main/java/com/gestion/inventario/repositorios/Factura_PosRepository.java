package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Factura_pos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Factura_PosRepository extends JpaRepository<Factura_pos, Long> {
}
