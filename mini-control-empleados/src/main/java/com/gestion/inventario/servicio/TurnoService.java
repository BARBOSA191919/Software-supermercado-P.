package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Turno;
import com.gestion.inventario.entidades.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TurnoService {

    // Metodo para listar todos los turnos
    List<Turno> listarTurnos();

    // Metodo para guardar un turno
    Turno guardarTurno(Turno turno);

    // Metodo para obtener un turno por su ID
    Turno obtenerTurnoPorId(Long id);

    Page<Turno> listarTurnos(Pageable pageable); // Metodo con Pageable

    // Metodo para eliminar un turno
    void eliminarTurno(Long id);

    Turno findById(int turnoId);
    //nuevos metodos
    Turno obtenerTurnoActivo();

    boolean existeTurnoActivo();

    List<Turno> encontrarTurnosAbiertos(LocalDate fecha);

    Turno abrirNuevoTurno(Turno turno);

    Turno cerrarTurno(Turno turno);

    boolean existeTurnoParaUsuarioYFecha(Usuario usuario, Date fecha);

    Page<Turno> findAllBySearch(String search, Pageable pageable);

}
