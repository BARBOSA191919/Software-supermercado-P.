package com.gestion.inventario.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
public class Verdura {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "Debes especificar el nombre")
    @Size(min = 1, max = 50, message = "El nombre debe medir entre 1 y 50")
    private String nombre;

    @NotNull(message = "Debes especificar el código")
    @Size(min = 1, max = 50, message = "El código debe medir entre 1 y 50")
    private String codigo;

    @NotNull(message = "Debes especificar el precio")
    @Min(value = 0, message = "El precio mínimo es 0")
    private Float precio;

    //@NotNull(message = "Debes especificar la existencia")
    @Min(value = 0, message = "La existencia mínima es 0")
    private Float existencia;

    @NotNull(message = "Debes especificar el IVA")
    @Min(value = 0, message = "El IVA mínimo es 0")
    private BigDecimal iva; // IVA del producto

    @NotNull(message = "Debes especificar el precio final")
    @Min(value = 0, message = "El precio minimo es 0")
    private Float precio_final; // IVA del producto

        private  Float descuento;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonBackReference
    private Categoria categoria;

    // Constructor vacío
    public Verdura() {
    }

    public Verdura(String nombre, String codigo, Float precio, Float existencia,Float precio_final, Integer id, BigDecimal iva,Float descuento, Categoria categoria) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.precio = precio;
        this.existencia = existencia;
        this.iva = iva;
        this.descuento = descuento;
        this.id = id;
        this.categoria = categoria;
        this.precio_final=precio_final;
    }

    public Verdura(String nombre, String codigo, Float precio, Float existencia,BigDecimal iva, Float precio_final) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.precio = precio;
        this.iva = iva;
        this.existencia = existencia;
        this.precio_final=precio_final;

    }

    public Verdura(@NotNull(message = "Debes especificar el código") @Size(min = 1, max = 50, message = "El código debe medir entre 1 y 50") String codigo) {
        this.codigo = codigo;
    }

    public Float getDescuento() {
        return descuento;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
    }

    public boolean sinExistencia() {
        return this.existencia <= 0;
    }

    public Float getPrecio_final() {
        return precio_final;
    }

    public void setPrecio_final(Float precio_final) {
        this.precio_final = precio_final;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Float getExistencia() {
        return existencia;
    }

    public void setExistencia(Float existencia) {
        this.existencia = existencia;
    }

    public void restarExistencia(Float existencia) {
        this.existencia -= existencia;
    }

    // Metodo para sumar la existencia
    public void sumarExistencia(float cantidad) {
        this.existencia += cantidad;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }


    public void setStock(Float nuevoStock) {

    }
}
