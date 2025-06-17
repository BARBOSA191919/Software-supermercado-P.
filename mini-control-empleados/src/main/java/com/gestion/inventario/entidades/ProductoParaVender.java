package com.gestion.inventario.entidades;

import java.math.BigDecimal;

public class ProductoParaVender extends Producto {
    private Float cantidad;
    private Float total;

    public ProductoParaVender(String codigo, String nombre, Float precio, Float precio_final,
                              Float existencia, BigDecimal iva,Float descuento, Integer id, Float cantidad) {
        super(codigo, nombre, precio, existencia, id, iva,descuento, precio_final);
        this.cantidad = cantidad;
    }

    public void aumentarCantidad() {
        if (this.cantidad == null) {
            this.cantidad = 1.0f;
        } else {
            this.cantidad++;
        }
    }

    public Float getCantidad() {
        return cantidad;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

    // metodo para obtener el subtotal
    public Float getSubtotal() {
        return this.getPrecio() * this.cantidad;
    }

    public Float getTotal() {
        return this.getPrecio_final() * this.cantidad;
    }

    public void disminuirCantidad() {
        this.cantidad -= 1;
        this.total = this.precio * this.cantidad;
    }

    @Override
    public String toString() {
        return "ProductoParaVender{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", codigo='" + getCodigo() + '\'' +
                ", precio=" + getPrecio() +
                ", precio_final=" + getPrecio_final() +
                ", existencia=" + getExistencia() +
                ", iva=" + getIva() +
                ", cantidad=" + getCantidad() +
                '}';
    }
}