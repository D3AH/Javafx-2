package org.diegoauyon.bean;

public class DetalleCompra {
    private int codigoDetalleCompra;
    private int cantidad;
    private int costoUnitario;
    private int subtotal;
    private int codigoProducto;
    private int numeroDocumento;

    public DetalleCompra() {
    }

    public DetalleCompra(int codigoDetalleCompra, int cantidad, int costoUnitario, int subtotal, int codigoProducto, int numeroDocumento) {
        this.codigoDetalleCompra = codigoDetalleCompra;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.subtotal = subtotal;
        this.codigoProducto = codigoProducto;
        this.numeroDocumento = numeroDocumento;
    }

    public int getCodigoDetalleCompra() {
        return codigoDetalleCompra;
    }

    public void setCodigoDetalleCompra(int codigoDetalleCompra) {
        this.codigoDetalleCompra = codigoDetalleCompra;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(int costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    
    @Override
    public String toString() {
        return this.getCodigoDetalleCompra() + " " + this.getCodigoProducto();
    }
}
