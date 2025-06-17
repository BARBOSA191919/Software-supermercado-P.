package com.gestion.inventario.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int codigo;

    @NotNull
    private String nombre;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Producto> productos;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Fruvert> fruverts;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Verdura> verdura;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Carnes> carne;

    // Nuevo: gestionar la relación con Reporte
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Reporte> reportes;

    // Constructor vacío
    public Categoria() {
    }

    // Constructor con parámetros
    public Categoria(String nombre, int codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    // Getters y Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(@NotNull int codigo) {
        this.codigo = codigo;
    }

    public @NotNull String getNombre() {
        return nombre;
    }

    public void setNombre(@NotNull String nombre) {
        this.nombre = nombre;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Fruvert> getFruverts() {
        return fruverts;
    }

    public void setFruverts(List<Fruvert> fruverts) {
        this.fruverts = fruverts;
    }

    public List<Verdura> getVerdura() {
        return verdura;
    }

    public void setVerdura(List<Verdura> verdura) {
        this.verdura = verdura;
    }

    public List<Carnes> getCarne() {
        return carne;
    }

    public void setCarne(List<Carnes> carne) {
        this.carne = carne;
    }
}
