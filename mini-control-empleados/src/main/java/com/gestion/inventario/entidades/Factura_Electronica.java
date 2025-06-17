package com.gestion.inventario.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Entity
@Table(name = "facturas")
public class Factura_Electronica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String numeroFactura;  // Número de la factura

    private java.sql.Date fecha;

    @NotEmpty
    private String xmlDocument; // Factura en formato XML


    // Constructor vacío
    // Constructor vacío
    public Factura_Electronica() {
    }


    // Constructor con parámetros
    public Factura_Electronica(String numeroFactura,String xmlDocument, java.sql.Date fecha) {
        this.numeroFactura = numeroFactura;
        this.xmlDocument = xmlDocument;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotEmpty String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(@NotEmpty String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public @NotEmpty String getXmlDocument() {
        return xmlDocument;
    }

    public void setXmlDocument(@NotEmpty String xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    public  java.sql.Date getFecha() {
        return fecha;
    }

    public void setFecha( java.sql.Date fecha) {
        this.fecha = fecha;
    }
}
