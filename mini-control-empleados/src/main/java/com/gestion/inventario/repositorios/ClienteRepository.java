package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Page<Cliente> findByNombreClienteContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCedulaContainingIgnoreCase(
            String nombre, String apellido, String cedula, Pageable pageable);

    List<Cliente> findByNombreClienteContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCedulaContainingIgnoreCase(
            String nombre, String apellido, String cedula);

}
