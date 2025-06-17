package com.gestion.inventario.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "compras")
public class Compras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Identificador único de la compra

    @NotNull
    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;  // Proveedor relacionado con la compra

    @NotNull
    private String codigo;  // Fecha de la compra

    @NotNull
    private String nombre;  // Fecha de la compra

    @NotNull
    private Date fecha;  // Fecha de la compra

    @NotNull
    private Integer cantidad;  // Fecha de la compra

    @NotNull
    private Float iva;  // Fecha de la compra

    @NotNull
    private Float descuento;  // Fecha de la compra

    @NotNull
    private Double Precio;  // Fecha de la compra

    @NotNull
    private Double total;  // Total de la compra


    // Constructor vacío
    public Compras() {
    }

    public Compras(Long id, Proveedor proveedor, String codigo, String nombre, Date fecha, Integer cantidad, Float iva, Float descuento, Double precio, Double total) {
        this.id = id;
        this.proveedor = proveedor;
        this.codigo = codigo;
        this.nombre = nombre;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.iva = iva;
        this.descuento = descuento;
        Precio = precio;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Float getIva() {
        return iva;
    }

    public void setIva(Float iva) {
        this.iva = iva;
    }

    public Float getDescuento() {
        return descuento;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
    }

    public Double getPrecio() {
        return Precio;
    }

    public void setPrecio(Double precio) {
        Precio = precio;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}

