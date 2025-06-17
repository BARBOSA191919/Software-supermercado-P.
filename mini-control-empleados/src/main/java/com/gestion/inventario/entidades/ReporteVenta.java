package com.gestion.inventario.entidades;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;


@Entity
public class ReporteVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    // Additional fields specific to sales reporting
    private Date fechaReporte;
    private Float subtotalVentas;
    private Float totalVentas;
    private Integer cantidadProductosVendidos;


    @OneToOne
    @JoinColumn(name = "venta_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference // Indica que esta es la parte "principal" de la relación
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "turno_id")
    private Turno turno;


    // Default constructor
    public ReporteVenta() {}

    // Constructor with all fields

    public ReporteVenta(Date fechaReporte, Float totalVentas, Integer cantidadProductosVendidos, Float subtotalVentas) {
        this.fechaReporte = fechaReporte;
        this.totalVentas = totalVentas;
        this.cantidadProductosVendidos = cantidadProductosVendidos;
        this.subtotalVentas = subtotalVentas;
    }


    // Additional methods for reporting


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(Date fechaReporte) {
        this.fechaReporte = fechaReporte;
    }


    public Integer getCantidadProductosVendidos() {
        return cantidadProductosVendidos;
    }

    public void setCantidadProductosVendidos(Integer cantidadProductosVendidos) {
        this.cantidadProductosVendidos = cantidadProductosVendidos;
    }

    public Float getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(Float totalVentas) {
        this.totalVentas = totalVentas;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Float getSubtotalVentas() {
        return subtotalVentas;
    }

    public void setSubtotalVentas(Float subtotalVentas) {
        this.subtotalVentas = subtotalVentas;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public void setTotalVentas(float totalVentas) {
        this.totalVentas = totalVentas;
    }

    public void setCantidadProductosVendidos(int cantidadProductosVendidos) {
        this.cantidadProductosVendidos = cantidadProductosVendidos;
    }



}
