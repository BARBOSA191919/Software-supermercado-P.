package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Turno;
import com.gestion.inventario.entidades.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    // Metodo para obtener un turno por fecha y hora de inicio (puedes agregar más métodos según tus necesidades)
    Turno findByFechaAndHoraInicio(java.time.LocalDate fecha, java.time.LocalTime horaInicio);

    @Query("SELECT t FROM Turno t WHERE t.fecha = CURRENT_DATE " +
            "AND (t.horaInicio <= CURRENT_TIME AND " +
            "(t.horaSalida IS NULL OR t.horaSalida > CURRENT_TIME))")
    Turno findTurnoActivo();

    // Metodo para encontrar el último turno del día
    @Query("SELECT t FROM Turno t WHERE t.fecha = CURRENT_DATE ORDER BY t.horaSalida DESC")
    List<Turno> findUltimoTurnoDia(Pageable pageable);

    // Metodo para encontrar turnos abiertos por fecha
    List<Turno> findByFechaAndHoraInicioBefore(LocalDate fecha, LocalTime horaActual);

    Optional<Turno> findByUsuarioAndFecha(Usuario usuario, Date fecha);

    List<Turno> findByUsuario_Username(String username);


    boolean existsByUsuarioAndFecha(Usuario usuario, java.util.Date fecha);

    Page<Turno> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    List<Turno> findByNombreContainingIgnoreCase(String nombre);
}
