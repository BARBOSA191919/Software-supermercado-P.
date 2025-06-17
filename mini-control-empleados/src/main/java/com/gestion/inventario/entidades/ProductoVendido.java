package com.gestion.inventario.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class ProductoVendido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Integer id;


    private Float precio;        // Precio sin IVA
    private Float precio_final;  // Default to 0.0
    private Float cantidad = 0.0f;  // Precio con IVA
    private BigDecimal iva;
    private Float descuento;

    private String nombre, codigo;

    @ManyToOne
    @JoinColumn
    @JsonBackReference // Evita serializar la referencia inversa a Venta
    private Venta venta;

    // Constructor principal
    public ProductoVendido(Float cantidad, Float precio, Float precio_final,BigDecimal iva,Float descuento,
                           String nombre, String codigo, Venta venta) {
        this.cantidad = cantidad;
        this.precio = precio;
        this.precio_final = precio_final;
        this.iva = iva;
        this.descuento = descuento;
        this.nombre = nombre;
        this.codigo = codigo;
        this.venta = venta;
    }

    public ProductoVendido() {
    }

    public Float getTotal() {
        if (this.precio_final == null || this.cantidad == null) {
            return 0.0f;  // o podrías retornar null si prefieres
        }
        return this.precio_final * this.cantidad;
    }

    // Métodos de cálculo
    public Float getSubtotal() {
        if (this.precio == null || this.cantidad == null) {
            return 0.0f;  // o podrías retornar null si prefieres
        }
        return this.precio * this.cantidad;
    }

    public Float getDescuento() {
        return descuento;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
    }

    public Venta getVenta() {
        return venta;
    }

    public Float getPrecio() {
        return precio;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Float getPrecio_final() {
        return precio_final;
    }

    public void setPrecio_final(Float precio_final) {
        this.precio_final = precio_final;
    }

    public Float getCantidad() {
        return cantidad;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


}