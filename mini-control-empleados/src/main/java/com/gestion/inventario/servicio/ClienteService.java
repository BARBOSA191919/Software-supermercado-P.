package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ClienteService {


    public List<Cliente> findAll();

    public Page<Cliente> findAll(Pageable pageable);

    public void save(Cliente cliente);

    public Cliente findOne(Long id);

    public void delete(Long id);

    Cliente findById(int clienteId);

    Page<Cliente> findAllBySearch(String search, Pageable pageable);

}

