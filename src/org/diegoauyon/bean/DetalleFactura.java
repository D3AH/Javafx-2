package org.diegoauyon.bean;

public class DetalleFactura {
    private int codigoDetalleFactura;
    private int cantidad;
    private int precio;
    private int codigoProducto;
    private int numeroFactura;

    public DetalleFactura() {
    }

    public DetalleFactura(int codigoDetalleFactura, int cantidad, int precio, int codigoProducto, int numeroFactura) {
        this.codigoDetalleFactura = codigoDetalleFactura;
        this.cantidad = cantidad;
        this.precio = precio;
        this.codigoProducto = codigoProducto;
        this.numeroFactura = numeroFactura;
    }

    public int getCodigoDetalleFactura() {
        return codigoDetalleFactura;
    }

    public void setCodigoDetalleFactura(int codigoDetalleFactura) {
        this.codigoDetalleFactura = codigoDetalleFactura;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }
    
    @Override
    public String toString() {
        return this.getCodigoDetalleFactura() + " | " + this.getNumeroFactura();
    }
}
