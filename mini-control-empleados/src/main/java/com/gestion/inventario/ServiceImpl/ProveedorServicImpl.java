package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Compras;
import com.gestion.inventario.entidades.Proveedor;
import com.gestion.inventario.repositorios.ProveedorRepository;
import com.gestion.inventario.servicio.ComprasService;
import com.gestion.inventario.servicio.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProveedorServicImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ComprasService comprasService; // Asegúrate de inyectar el servicio de compras

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> findAll() {
        return proveedorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proveedor> findAll(Pageable pageable) {
        return proveedorRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Proveedor proveedor) {
        proveedorRepository.save(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public Proveedor findOne(Long id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        proveedorRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proveedor> findAllBySearch(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            if (pageable == null) {
                List<Proveedor> resultados = proveedorRepository.findByNombreProveedorContainingIgnoreCase(search);
                return new PageImpl<>(resultados);
            }
            return proveedorRepository.findByNombreProveedorContainingIgnoreCase(search, pageable);
        }
        if (pageable == null) {
            return new PageImpl<>(proveedorRepository.findAll());
        }
        return proveedorRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proveedor> findAllBySearchAndDateRange(String search, LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable) {
        List<Proveedor> allProveedores = proveedorRepository.findAll();
        List<Proveedor> filteredProveedores = allProveedores.stream()
                .filter(p -> (search == null || search.isEmpty() ||
                        p.getNombreProveedor().toLowerCase().contains(search.toLowerCase())))
                .filter(p -> {
                    List<Compras> compras = comprasService.findAll().stream()
                            .filter(c -> c.getProveedor().getId().equals(p.getId()))
                            .filter(c -> (fechaInicio == null || !c.getFecha().toLocalDate().isBefore(fechaInicio)))
                            .filter(c -> (fechaFin == null || !c.getFecha().toLocalDate().isAfter(fechaFin)))
                            .collect(Collectors.toList());
                    return !compras.isEmpty();
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredProveedores.size());
        return new PageImpl<>(filteredProveedores.subList(start, end), pageable, filteredProveedores.size());
    }


    @Override
    @Transactional(readOnly = true)
    public Map<Long, Double> calculateTotalComprasByPeriod(List<Proveedor> proveedores, LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            List<Compras> compras = comprasService.findAll().stream()
                    .filter(c -> proveedores.stream().anyMatch(p -> p.getId().equals(c.getProveedor().getId())))
                    .filter(c -> (fechaInicio == null || !c.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(fechaInicio)))
                    .filter(c -> (fechaFin == null || !c.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(fechaFin)))
                    .collect(Collectors.toList());

            Map<Long, Double> result = compras.stream()
                    .collect(Collectors.groupingBy(
                            c -> c.getProveedor().getId(),
                            Collectors.summingDouble(Compras::getTotal)
                    ));

            return result != null ? result : new HashMap<>();
        } catch (Exception e) {
            System.err.println("Error al calcular total de compras: " + e.getMessage());
            return new HashMap<>();
        }
    }
}