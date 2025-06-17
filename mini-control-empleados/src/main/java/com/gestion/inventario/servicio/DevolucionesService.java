package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Devoluciones;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DevolucionesService {

    public List<Devoluciones> findAll();

    public Page<Devoluciones> findAll(Pageable pageable);

    public void save(Devoluciones devoluciones);

    public Devoluciones findOne(Long id);

    public void delete(Long id);

    Devoluciones findById(int devolucionesId);

    Page<Devoluciones> findAllBySearchAndMotivoAndFecha(
            String search, String motivo, String fechaInicio, String fechaFin, Pageable pageable) ;


}