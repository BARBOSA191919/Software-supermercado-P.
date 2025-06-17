package com.gestion.inventario.entidades;

import java.math.BigDecimal;

public class CarnesParaVender extends ProductoParaVender {
    public CarnesParaVender(String codigo, String nombre, Float precio, Float precio_final, BigDecimal iva,Float descuento, Float cantidad, Integer id, Float existencia) {
        super(codigo, nombre, precio, precio_final, existencia, iva,descuento, id, cantidad);
    }

}

