package com.gestion.inventario.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "inventarios")
public class Inventario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // Identificador único del movimiento de inventario

	@NotNull
	private String nombre;  // cambiado de Nombre a nombre

	@NotNull
	private String codigo;  // ya está bien

	@NotNull
	private Float precio;   // ya está bien

	@NotNull
	private Float stock;    // cambiado de Stock a stock

	@NotNull
	private String tipoMovimiento;  // Tipo de movimiento (entrada o salida)

	@NotNull
	private Integer cantidad;  // Cantidad del movimiento

	@NotNull
	private Date fecha;  // Fecha del movimiento

	private String comentario;  // Comentarios adicionales sobre el movimiento

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proveedor_id") // Asegúrate de que el nombre de la columna sea correcto
	private Proveedor proveedor;

	// Constructor vacío
	public Inventario() {
	}

	// Constructor con parámetros
	public Inventario(String tipoMovimiento, Integer cantidad, Date fecha, String comentario, Categoria categoria,Proveedor proveedor) {
		this.tipoMovimiento = tipoMovimiento;
		this.cantidad = cantidad;
		this.fecha = fecha;
		this.comentario = comentario;
		this.categoria = categoria;
		this.proveedor = proveedor;
	}

	// Getters y Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public @NotNull String getNombre() {
		return nombre;
	}

	public void setNombre(@NotNull String nombre) {
		this.nombre = nombre;
	}

	public @NotNull String getCodigo() {
		return codigo;
	}

	public void setCodigo(@NotNull String codigo) {
		this.codigo = codigo;
	}

	public @NotNull Float getPrecio() {
		return precio;
	}

	public void setPrecio(@NotNull Float precio) {
		this.precio = precio;
	}

	public @NotNull Float getStock() {
		return stock;
	}

	public void setStock(@NotNull Float stock) {
		this.stock = stock;
	}

	public @NotNull String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(@NotNull String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public @NotNull Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(@NotNull Integer cantidad) {
		this.cantidad = cantidad;
	}

	public @NotNull Date getFecha() {
		return fecha;
	}

	public void setFecha(@NotNull Date fecha) {
		this.fecha = fecha;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
}