package com.tiendagenerica.tienda.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.tiendagenerica.tienda.entity.*;

public class VentaDto {

    private float iva = 0.19f;
    private Cliente cliente;
    private ArrayList<ProductoParaVenderDto> detalleVenta;
    private float subTotal;
    private float totalIva;
    private float total;
    private String fechaFactura;

    public VentaDto() {
        detalleVenta = new ArrayList<>();
        cliente = new Cliente();
        Date myDate = new Date();
        this.fechaFactura = new SimpleDateFormat("yyyy-MM-dd").format(myDate);
    }

    public void removerItem(int indice) {
        if (detalleVenta != null && detalleVenta.size() > 0 && detalleVenta.get(indice) != null) {
            detalleVenta.remove(indice);
            calcularTotales();

        }
    }

    public void agregarItem(Producto producto) {
        ProductoParaVenderDto nuevoItem = new ProductoParaVenderDto(producto.getId(), producto.getNombre(),
                producto.getCodigo(), producto.getNitProveedor(), producto.getPrecioCompra(), producto.getPrecioVenta(),
                producto.getIvaCompra(), producto.getCantidad());
        detalleVenta.add(nuevoItem);
        calcularTotales();
    }

    public boolean estaVacio() {
        return detalleVenta.isEmpty();
    }

    public boolean tieneCliente() {
        return cliente.getId() != 0;
    }

    public void calcularTotales() {
        this.subTotal = 0;
        for (ProductoParaVenderDto p : detalleVenta) {
            this.subTotal += p.getTotal();
        }
        this.totalIva = this.subTotal * iva;
        this.total = this.subTotal + this.totalIva;

    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<ProductoParaVenderDto> getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(ArrayList<ProductoParaVenderDto> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float getTotalIva() {
        return totalIva;
    }

    public void setTotalIva(float totalIva) {
        this.totalIva = totalIva;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

}
