package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Pagos_datafono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Pagos_datafonoRepository extends JpaRepository<Pagos_datafono,Long> {
}
