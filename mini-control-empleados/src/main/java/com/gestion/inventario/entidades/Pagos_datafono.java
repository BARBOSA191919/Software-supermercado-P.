package com.gestion.inventario.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "pagos_datafono")
public class Pagos_datafono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String numeroTransaccion;  // Número de la transacción

    @NotNull
    private Date fecha;  // Fecha del pago

    @NotNull
    private BigDecimal monto;  // Monto del pago


    @NotNull
    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = false)  // Relación con la tabla facturas_pos
    private Factura_pos factura;  // Factura asociada al pago

    // Constructor vacío
    public Pagos_datafono() {
    }

    // Constructor con parámetros
    public Pagos_datafono(String numeroTransaccion, Date fecha, BigDecimal monto, Factura_pos factura ) {
        this.numeroTransaccion = numeroTransaccion;
        this.fecha = fecha;
        this.monto = monto;
        this.factura = factura;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
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

    public Factura_pos getFactura() {
        return factura;
    }

    public void setFactura(Factura_pos factura) {
        this.factura = factura;
    }
}