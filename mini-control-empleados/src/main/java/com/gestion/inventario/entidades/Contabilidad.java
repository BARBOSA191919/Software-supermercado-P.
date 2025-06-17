package com.gestion.inventario.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name = "contabilidad")
public class Contabilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double totalIngresos;

    @NotNull
    private Double totalEgresos;

    private Double ganancias;

    private Date fecha;

    // Constructor vacío
    public Contabilidad() {
    }

    // Constructor con parámetros
    public Contabilidad(Double totalIngresos, Double totalEgresos, Double ganancias, Date fecha) {
        this.totalIngresos = totalIngresos;
        this.totalEgresos = totalEgresos;
        this.ganancias = ganancias;
        this.fecha = fecha;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement
    public @NotNull Double getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(@NotNull Double totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    @XmlElement
    public @NotNull Double getTotalEgresos() {
        return totalEgresos;
    }

    public void setTotalEgresos(@NotNull Double totalEgresos) {
        this.totalEgresos = totalEgresos;
    }

    @XmlElement

    public Double getGanancias() {
        return ganancias;
    }

    public void setGanancias(Double ganancias) {
        this.ganancias = ganancias;
    }

    @XmlElement

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}
