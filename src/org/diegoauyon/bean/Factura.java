package org.diegoauyon.bean;

import java.time.LocalDate;

public class Factura {
    private int numeroFactura;
    private String estado;
    private String nit;
    private int total;
    private LocalDate fecha;
    private int codigoCliente;

    public Factura() {
    }

    public Factura(int numeroFactura, String estado, String nit, int total, LocalDate fecha, int codigoCliente) {
        this.numeroFactura = numeroFactura;
        this.estado = estado;
        this.nit = nit;
        this.total = total;
        this.fecha = fecha;
        this.codigoCliente = codigoCliente;
    }

    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    
}
