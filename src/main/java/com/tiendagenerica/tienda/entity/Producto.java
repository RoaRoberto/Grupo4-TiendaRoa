package com.tiendagenerica.tienda.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(unique = true)
    private String nombre;
    private String codigo;
    private String nitProveedor;
    private float precioCompra;
    private float precioVenta;
    private float ivaCompra;
    private int cantidad;

    public Producto(int id, String nombre, String codigo, String nitProveedor, float precioCompra, float precioVenta,
            float ivaCompra, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.nitProveedor = nitProveedor;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.ivaCompra = ivaCompra;
        this.cantidad = cantidad;
    }

    public Producto() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNitProveedor() {
        return nitProveedor;
    }

    public void setNitProveedor(String nitProveedor) {
        this.nitProveedor = nitProveedor;
    }

    public float getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(float precioCompra) {
        this.precioCompra = precioCompra;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public float getIvaCompra() {
        return ivaCompra;
    }

    public void setIvaCompra(float ivaCompra) {
        this.ivaCompra = ivaCompra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
