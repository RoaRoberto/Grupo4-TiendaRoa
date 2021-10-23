package com.tiendagenerica.tienda.entity;

import javax.persistence.*;

import com.sun.istack.NotNull;

@Entity
public class Venta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Usuario usuario;

	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Cliente cliente;

	private float total;
	private float subTotal;
	private float iva;
	private float totalIva;
	private String fecha;

	public Venta() {
	}

	public Venta(Usuario usuario, Cliente cliente, float total, float subTotal, float iva, float totalIva,
			String fecha) {
		this.id = 0;
		this.usuario = usuario;
		this.cliente = cliente;
		this.total = total;
		this.subTotal = subTotal;
		this.iva = iva;
		this.totalIva = totalIva;
		this.fecha = fecha;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public float getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}

	public float getIva() {
		return iva;
	}

	public void setIva(float iva) {
		this.iva = iva;
	}

	public float getTotalIva() {
		return totalIva;
	}

	public void setTotalIva(float totalIva) {
		this.totalIva = totalIva;
	}

}
