package com.gestion.inventario.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "facturas_pos")
public class Factura_pos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Identificador único de la factura


    private Date fecha;  // Fecha de la factura

    private BigDecimal monto;  // Monto de la factura

    // Constructor vacío
    public Factura_pos() {
    }

    // Constructor con parámetros
    public Factura_pos(Date fecha, BigDecimal monto) {
        this.fecha = fecha;
        this.monto = monto;
    }

    // Getters y Setters
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

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}