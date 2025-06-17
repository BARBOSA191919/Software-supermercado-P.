package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Devoluciones;
import com.gestion.inventario.repositorios.DevolucionesRepository;
import com.gestion.inventario.servicio.DevolucionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DevolucionesServiceImpl implements DevolucionesService {

    @Autowired
    private DevolucionesRepository devolucionesRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Devoluciones> findAll() {
        return devolucionesRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Devoluciones> findAll(Pageable pageable) {
        return devolucionesRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Devoluciones devoluciones) {
        devolucionesRepository.save(devoluciones);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        devolucionesRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Devoluciones findOne(Long id) {
        return devolucionesRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Devoluciones findById(int devolucionesId) {
        return devolucionesRepository.findById((long) devolucionesId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Devoluciones> findAllBySearchAndMotivoAndFecha(
            String search, String motivo, String fechaInicio, String fechaFin, Pageable pageable) {

        // Convertir strings de fecha a java.util.Date si están presentes
        Date fechaDesde = null;
        Date fechaHasta = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (fechaInicio != null && !fechaInicio.isEmpty()) {
                fechaDesde = dateFormat.parse(fechaInicio);
            }

            if (fechaFin != null && !fechaFin.isEmpty()) {
                fechaHasta = dateFormat.parse(fechaFin);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaHasta);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                fechaHasta = calendar.getTime();
            }
        } catch (ParseException e) {
            // Manejar el error de formato de fecha
            return pageable == null ? new PageImpl<>(devolucionesRepository.findAll()) : devolucionesRepository.findAll(pageable);
        }

        // Si Pageable es null, devolver todos los resultados como una lista envuelta en PageImpl
        if (pageable == null) {
            List<Devoluciones> resultados = aplicarFiltrosSinPaginacion(search, motivo, fechaDesde, fechaHasta);
            return new PageImpl<>(resultados);
        }

        // Aplicar filtros con paginación según los parámetros proporcionados
        if (search != null && !search.isEmpty() && motivo != null && !motivo.isEmpty()
                && fechaDesde != null && fechaHasta != null) {
            return devolucionesRepository.findByNombreProductoContainingAndMotivoContainingAndFechaBetween(
                    search, motivo, fechaDesde, fechaHasta, pageable);
        } else if (search != null && !search.isEmpty() && motivo != null && !motivo.isEmpty()) {
            return devolucionesRepository.findByNombreProductoContainingAndMotivoContaining(
                    search, motivo, pageable);
        } else if (search != null && !search.isEmpty() && fechaDesde != null && fechaHasta != null) {
            return devolucionesRepository.findByNombreProductoContainingAndFechaBetween(
                    search, fechaDesde, fechaHasta, pageable);
        } else if (motivo != null && !motivo.isEmpty() && fechaDesde != null && fechaHasta != null) {
            return devolucionesRepository.findByMotivoContainingAndFechaBetween(
                    motivo, fechaDesde, fechaHasta, pageable);
        } else if (search != null && !search.isEmpty()) {
            return devolucionesRepository.findByNombreProductoContaining(search, pageable);
        } else if (motivo != null && !motivo.isEmpty()) {
            return devolucionesRepository.findByMotivoContaining(motivo, pageable);
        } else if (fechaDesde != null && fechaHasta != null) {
            return devolucionesRepository.findByFechaBetween(fechaDesde, fechaHasta, pageable);
        } else {
            return devolucionesRepository.findAll(pageable);
        }
    }

    // Metodo auxiliar para aplicar filtros sin paginación
    private List<Devoluciones> aplicarFiltrosSinPaginacion(String search, String motivo, Date fechaDesde, Date fechaHasta) {
        if (search != null && !search.isEmpty() && motivo != null && !motivo.isEmpty()
                && fechaDesde != null && fechaHasta != null) {
            return devolucionesRepository.findByNombreProductoContainingAndMotivoContainingAndFechaBetween(
                    search, motivo, fechaDesde, fechaHasta);
        } else if (search != null && !search.isEmpty() && motivo != null && !motivo.isEmpty()) {
            return devolucionesRepository.findByNombreProductoContainingAndMotivoContaining(
                    search, motivo);
        } else if (search != null && !search.isEmpty() && fechaDesde != null && fechaHasta != null) {
            return devolucionesRepository.findByNombreProductoContainingAndFechaBetween(
                    search, fechaDesde, fechaHasta);
        } else if (motivo != null && !motivo.isEmpty() && fechaDesde != null && fechaHasta != null) {
            return devolucionesRepository.findByMotivoContainingAndFechaBetween(
                    motivo, fechaDesde, fechaHasta);
        } else if (search != null && !search.isEmpty()) {
            return devolucionesRepository.findByNombreProductoContaining(search);
        } else if (motivo != null && !motivo.isEmpty()) {
            return devolucionesRepository.findByMotivoContaining(motivo);
        } else if (fechaDesde != null && fechaHasta != null) {
            return devolucionesRepository.findByFechaBetween(fechaDesde, fechaHasta);
        } else {
            return devolucionesRepository.findAll();
        }
    }
}