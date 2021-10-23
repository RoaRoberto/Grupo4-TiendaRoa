package com.tiendagenerica.tienda.dto;

import com.tiendagenerica.tienda.entity.Producto;

public class ProductoParaVenderDto extends Producto {

    private int cantidadVenta;

    public ProductoParaVenderDto(int id, String nombre, String codigo, String nitProveedor, float precioCompra,
            float precioVenta, float ivaCompra, int cantidadVenta) {
        super(id, nombre, codigo, nitProveedor, precioCompra, precioVenta, ivaCompra, 0);
        this.cantidadVenta = cantidadVenta;
    }

    public ProductoParaVenderDto() {

    }

    public void aumentarCantidad() {
        this.cantidadVenta++;
    }

    public Float getTotal() {
        return this.getPrecioVenta() * this.cantidadVenta;
    }

    public int getCantidadVenta() {
        return cantidadVenta;
    }

    public void setCantidadVenta(int cantidadVenta) {
        this.cantidadVenta = cantidadVenta;
    }

}
