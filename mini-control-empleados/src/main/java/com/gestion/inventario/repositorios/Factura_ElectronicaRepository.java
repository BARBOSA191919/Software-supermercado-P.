package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Factura_Electronica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Factura_ElectronicaRepository extends JpaRepository<Factura_Electronica, Long> {
}
