package com.gestion.inventario.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "turnos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Añade esta línea
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Identificador único del turno
    private String caja;
    private String nombre;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha;  // Fecha del turno

    @OneToMany(mappedBy = "turno", cascade = CascadeType.ALL)
    @JsonIgnore // O usa @JsonBackReference para la relación inversa
    private Set<Venta> ventas = new HashSet<>();

    private String horaInicio;  // Hora de inicio del turno

    private String horaSalida;  // Hora de salida del turno

    private Double baseDinero;  // Base de dinero para el turno

        @JsonManagedReference
        @ManyToOne(optional = false) // Ensure user is required
        @JoinColumn(name = "usuario_id")
        private Usuario usuario;


    // Constructor vacío
    public Turno() {
    }

    // Constructor con parámetros
    public Turno(Date fecha, String caja,String horaInicio, String horaSalida, Double baseDinero,String nombre, Usuario usuario) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaSalida = horaSalida;
        this.baseDinero = baseDinero;
        this.nombre = nombre;
        this.caja = caja;
        this.usuario = usuario;
    }

    // Getters y Setters


    public Set<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(Set<Venta> ventas) {
        this.ventas = ventas;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Double getBaseDinero() {
        return baseDinero;
    }

    public void setBaseDinero(Double baseDinero) {
        this.baseDinero = baseDinero;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
