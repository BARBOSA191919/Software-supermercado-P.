package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Turno;
import com.gestion.inventario.entidades.Usuario;
import com.gestion.inventario.repositorios.TurnoRepository;
import com.gestion.inventario.servicio.ReporteVentaService;
import com.gestion.inventario.servicio.TurnoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class TurnoServiceImpl implements TurnoService {

    private static final Logger log = LoggerFactory.getLogger(TurnoServiceImpl.class);

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private ReporteVentaService reporteVentaService;

    @Override
    public List<Turno> listarTurnos() {
        return turnoRepository.findAll();
    }

    @Override
    public Turno guardarTurno(Turno turno) {
        return turnoRepository.save(turno);
    }

    @Override
    public Turno obtenerTurnoPorId(Long id) {
        Optional<Turno> turno = turnoRepository.findById(id);
        return turno.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Turno> listarTurnos(Pageable pageable) {
        return turnoRepository.findAll(pageable);
    }

    @Override
    public void eliminarTurno(Long id) {
        turnoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Turno findById(int turnoId) {
        return turnoRepository.findById((long) turnoId).orElse(null); // Convertir a Long
    }

    public List<Turno> findAll() {
        return List.of();
    }

    //de aqui para abajo

    @Transactional(readOnly = true)
    public Turno obtenerTurnoActivo() {
        Turno turnoActivo = turnoRepository.findTurnoActivo();

        if (turnoActivo == null) {
            // Use one of these approaches:

            // Approach 1 with Pageable:
            List<Turno> ultimosTurnos = turnoRepository.findUltimoTurnoDia(PageRequest.of(0, 1));
            turnoActivo = ultimosTurnos.isEmpty() ? null : ultimosTurnos.get(0);

            // Or Approach 2 with method name convention:
            // turnoActivo = turnoRepository.findFirstByFechaOrderByHoraSalidaDesc(LocalDate.now());
        }

        return turnoActivo;
    }

    // Metodo para verificar si hay un turno abierto
    @Transactional(readOnly = true)
    public boolean existeTurnoActivo() {
        return turnoRepository.findTurnoActivo() != null;
    }

    // Metodo para encontrar turnos abiertos en una fecha específica
    @Transactional(readOnly = true)
    public List<Turno> encontrarTurnosAbiertos(LocalDate fecha) {
        return turnoRepository.findByFechaAndHoraInicioBefore(
                fecha,
                LocalTime.now()
        );
    }

    // Metodo adicional para actualizar el estado del turno
    @Transactional
    public Turno cerrarTurno(Turno turno) {
        turno.setHoraSalida(LocalTime.now().toString());

        // Generar y guardar el reporte para este turno
        reporteVentaService.generarReportePorTurno(turno);

        return turnoRepository.save(turno);
    }

    @Override
    public boolean existeTurnoParaUsuarioYFecha(Usuario usuario, java.util.Date fecha) {
        return turnoRepository.findByUsuarioAndFecha(usuario, fecha).isPresent();
    }


    @Transactional
    public Turno abrirNuevoTurno(Turno nuevoTurno) {
        Turno turnoActivo = obtenerTurnoActivo();

        if (turnoActivo != null) {
            cerrarTurno(turnoActivo);
        }

        nuevoTurno.setHoraInicio(LocalTime.now().toString());
        nuevoTurno.setFecha(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        return turnoRepository.save(nuevoTurno);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Turno> findAllBySearch(String search, Pageable pageable) {
        try {
            if (pageable == null) {
                List<Turno> content = search != null && !search.trim().isEmpty() ?
                        turnoRepository.findByNombreContainingIgnoreCase(search) :
                        turnoRepository.findAll();
                return new PageImpl<>(content);
            }
            return search != null && !search.trim().isEmpty() ?
                    turnoRepository.findByNombreContainingIgnoreCase(search, pageable) :
                    turnoRepository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error en findAllBySearch", e);
            return Page.empty();
        }
    }

}
