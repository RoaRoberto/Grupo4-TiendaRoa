package com.tiendagenerica.tienda.entity;

import javax.persistence.*;

@Entity
public class DetalleVenta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Venta venta;

	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Producto producto;

	private float iva;
	private int cantidad;
	private float valorUnitario;
	private float valorTotal;
	private float valorTotalConIva;
	private float valorIva;

	public DetalleVenta() {
	}

	public DetalleVenta(int id, Venta venta, Producto producto, float iva, int cantidad, float valorUnitario) {
		this.id = id;
		this.venta = venta;
		this.producto = producto;
		this.iva = iva;
		this.cantidad = cantidad;
		this.valorUnitario = valorUnitario;
		this.calcularTotales();
	}

	public void calcularTotales() {
		this.valorTotal = this.cantidad * this.valorUnitario;
		this.valorIva = this.valorTotal * this.iva;
		this.valorTotalConIva = this.valorTotal + this.valorIva;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getIva() {
		return iva;
	}

	public void setIva(float iva) {
		this.iva = iva;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public float getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(float valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public float getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(float valorTotal) {
		this.valorTotal = valorTotal;
	}

	public float getValorTotalConIva() {
		return valorTotalConIva;
	}

	public void setValorTotalConIva(float valorTotalConIva) {
		this.valorTotalConIva = valorTotalConIva;
	}

	public float getValorIva() {
		return valorIva;
	}

	public void setValorIva(float valorIva) {
		this.valorIva = valorIva;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}
