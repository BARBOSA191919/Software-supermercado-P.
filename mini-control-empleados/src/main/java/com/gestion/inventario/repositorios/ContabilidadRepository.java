package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Contabilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContabilidadRepository extends JpaRepository<Contabilidad, Long> {
}
