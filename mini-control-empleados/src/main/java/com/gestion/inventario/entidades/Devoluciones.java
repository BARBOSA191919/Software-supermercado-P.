package com.gestion.inventario.entidades;

import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "devoluciones")
public class Devoluciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha;

    private Float precio;


    private Float precio_final;

    private Integer cantidad;

    private String motivo;

    // Nuevos campos para la información del producto
    private String codigoProducto;

    private String nombreProducto;

    @Enumerated(EnumType.STRING)
    private TipoProducto tipoProducto;

    // Enumeración para los tipos de productos
    public enum TipoProducto {
        PRODUCTO,
        FRUTA,
        VERDURA,
        CARNE
    }

    // Constructor vacío
    public Devoluciones() {
    }

    // Constructor actualizado con los nuevos campos
    public Devoluciones(Long id, Date fecha, Float precio, Integer cantidad,
                        String motivo, String codigoProducto, String nombreProducto,Float precio_final,
                        TipoProducto tipoProducto) {
        this.id = id;
        this.fecha = fecha;
        this.precio = precio;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.tipoProducto = tipoProducto;
        this.precio_final = precio_final;
    }

    // Getters y setters existentes
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    // Nuevos getters y setters para los campos añadidos
    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Float getPrecio_final() {
        return precio_final;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio_final(Float precio_final) {
        this.precio_final = precio_final;
    }

    @Override
    public String toString() {
        return "Devoluciones{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", precio=" + precio_final +
                ", cantidad=" + cantidad +
                ", motivo='" + motivo + '\'' +
                ", codigoProducto='" + codigoProducto + '\'' +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", tipoProducto=" + tipoProducto +
                '}';
    }
}