package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Cliente;
import com.gestion.inventario.repositorios.ClienteRepository;
import com.gestion.inventario.servicio.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServicelmpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll(); // Llama al repositorio para obtener todos los productos
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(int clienteId) {
        return clienteRepository.findById((long) clienteId).orElse(null); // Convertir a Long
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAllBySearch(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            if (pageable == null) {
                List<Cliente> resultados = clienteRepository.findByNombreClienteContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCedulaContainingIgnoreCase(search, search, search);
                return new PageImpl<>(resultados);
            }
            return clienteRepository.findByNombreClienteContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCedulaContainingIgnoreCase(search, search, search, pageable);
        }
        if (pageable == null) {
            return new PageImpl<>(clienteRepository.findAll());
        }
        return clienteRepository.findAll(pageable);
    }
}

