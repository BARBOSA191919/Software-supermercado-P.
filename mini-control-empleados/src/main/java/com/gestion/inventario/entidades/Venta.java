package com.gestion.inventario.entidades;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Venta {
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;  // Nuevo campo para facilitar las consultas
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String fechaYHora;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    @JsonManagedReference // Controla la serialización de productos
    private Set<ProductoVendido> productos = new HashSet<>();

    @Column(unique = true, nullable = false)
    private String codigoVenta; // Unique sale code

    @ManyToOne // Relación muchos a uno con Cliente
    @JoinColumn(name = "cliente_id") // Nombre de la columna en la tabla Venta
    private Cliente cliente;

    @ManyToOne // Relación muchos a uno con Cliente
    @JoinColumn(name = "turno_id") // Nombre de la columna en la tabla Venta
    private Turno turno;

    private Float efectivo; // Monto pagado por el cliente
    private Float cambio;   // Cambio a devolver al cliente

    public Venta() {
        this.fechaYHora = Utiles.obtenerFechaYHoraActual();
        this.cambio = 0.0f;   // Inicializar en 0
        this.codigoVenta = "VEN-" + System.currentTimeMillis(); // Example: VEN-1698771234567
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getTotal() {
        Float total = 0f;
        if (this.productos != null) {
            for (ProductoVendido productoVendido : this.productos) {
                total += productoVendido.getTotal();
            }
        }
        return total;
    }

    public Float getSubtotal() {
        Float subtotal = 0f;
        if (this.productos != null) {
            for (ProductoVendido productoVendido : this.productos) {
                subtotal += productoVendido.getSubtotal(); // Suma cada subtotal de ProductoVendido
            }
        }
        return subtotal;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public String getFechaYHora() {
        return fechaYHora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setFechaYHora(String fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    public Set<ProductoVendido> getProductos() {
        return productos;
    }

    public void setProductos(Set<ProductoVendido> productos) {
        this.productos = productos;
    }

    public Float getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(Float efectivo) {
        this.efectivo = efectivo;
        calcularCambio(); // Calcular cambio automáticamente al establecer efectivo
    }

    public Float getCambio() {
        return cambio;
    }

    public void setCambio(Float cambio) {
        this.cambio = cambio;
    }

    // Metodo para calcular el cambio a devolver
    private void calcularCambio() {
        if (efectivo != null && getTotal() != null) {
            this.cambio = efectivo - getTotal();
        } else {
            this.cambio = 0.0f; // Si no hay efectivo o total, el cambio es 0
        }
    }

    public void setFechaYHora(Date date) {

    }

    public String getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(String codigoVenta) {
        this.codigoVenta = codigoVenta;
    }
}