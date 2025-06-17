package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Compras;
import com.gestion.inventario.repositorios.ComprasRepository;
import com.gestion.inventario.servicio.ComprasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ComprasServicelmpl implements ComprasService {
    private static final Logger log = LoggerFactory.getLogger(ComprasServicelmpl.class);

    @Autowired
    private ComprasRepository comprasRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Compras> findAll() {
        return comprasRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Compras> findAll(Pageable pageable) {
        return comprasRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Compras> findAllBySearch(String search, Pageable pageable) {
        try {
            if (search != null && !search.trim().isEmpty()) {
                if (pageable == null) {
                    return new PageImpl<>(comprasRepository.findBySearch(search));
                }
                return comprasRepository.findBySearch(search, pageable);
            }
            if (pageable == null) {
                return new PageImpl<>(comprasRepository.findAll());
            }
            return comprasRepository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error en findAllBySearch", e);
            return Page.empty(pageable);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Compras> findAllByFilters(String search, Date fechaInicio, Date fechaFin, Long proveedorId, Pageable pageable) {
        try {
            return comprasRepository.findByFilters(search, fechaInicio, fechaFin, proveedorId, pageable);
        } catch (Exception e) {
            log.error("Error en findAllByFilters", e);
            return Page.empty(pageable != null ? pageable : PageRequest.of(0, 10));
        }
    }

    @Override
    @Transactional
    public void save(Compras compras) {
        comprasRepository.save(compras);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        comprasRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Compras findById(int comprasId) {
        return comprasRepository.findById((long) comprasId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Compras findOne(Long id) {
        return comprasRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Compras> filtrarPorFechas(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable) {
        Date startDate = fechaInicio != null ? Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
        Date endDate = fechaFin != null ? Date.from(fechaFin.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()) : null;
        return findAllByFilters(null, startDate, endDate, null, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularTotalCompras(List<Compras> compras) {
        return compras.stream().mapToDouble(Compras::getTotal).sum();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Double> calcularCostoPorProveedor(List<Compras> compras) {
        return compras.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getProveedor().getNombreProveedor(),
                        Collectors.summingDouble(Compras::getTotal)
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateTotalByPeriod(LocalDate start, LocalDate end) {
        Date startDate = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        Page<Compras> compras = findAllByFilters(null, startDate, endDate, null, PageRequest.of(0, Integer.MAX_VALUE));
        return calcularTotalCompras(compras.getContent());
    }
}