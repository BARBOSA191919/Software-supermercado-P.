package com.gestion.inventario.entidades;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Identificador único del reporte

    private String tipoReporte;  // Tipo de reporte (por ejemplo, ventas, inventario, etc.)

    private String descripcion;  // Descripción del reporte

    private Date fechaGeneracion;  // Fecha en que se generó el reporte

    private String rutaArchivo;  // Ruta del archivo del reporte

    // Constructor vacío
    public Reporte() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @JsonBackReference
    private Categoria categoria;

    // Constructor con parámetros
    public Reporte(String tipoReporte, String descripcion, Date fechaGeneracion, String rutaArchivo) {
        this.tipoReporte = tipoReporte;
        this.descripcion = descripcion;
        this.fechaGeneracion = fechaGeneracion;
        this.rutaArchivo = rutaArchivo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}