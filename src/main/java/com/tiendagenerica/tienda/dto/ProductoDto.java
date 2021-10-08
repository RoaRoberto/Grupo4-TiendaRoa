package com.tiendagenerica.tienda.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class ProductoDto {

    @CsvBindByPosition(position = 0)
    private String codigo;

    @CsvBindByPosition(position = 1)
    private String nombre;

    @CsvBindByPosition(position = 2)
    private String nitProveedor;

    @CsvBindByPosition(position = 3)
    private String precioCompra;

    @CsvBindByPosition(position = 4)
    private String ivaCompra;

    @CsvBindByPosition(position = 5)
    private String precioVenta;

    public ProductoDto(String nombre, String codigo, String nitProveedor, String precioCompra, String precioVenta,
            String ivaCompra) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.nitProveedor = nitProveedor;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.ivaCompra = ivaCompra;
    }

    public ProductoDto() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(String precioCompra) {
        this.precioCompra = precioCompra;
    }

    public String getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(String precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getIvaCompra() {
        return ivaCompra;
    }

    public void setIvaCompra(String ivaCompra) {
        this.ivaCompra = ivaCompra;
    }

}
