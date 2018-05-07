package org.diegoauyon.bean;

public class TelefonoCliente {
    private int codigoTelefonoCliente;
    private String descripcion;
    private String numero;
    private int codigoCliente;

    public TelefonoCliente() {
    }

    public TelefonoCliente(int codigoTelefonoCliente, String descripcion, String numero, int codigoCliente) {
        this.codigoTelefonoCliente = codigoTelefonoCliente;
        this.descripcion = descripcion;
        this.numero = numero;
        this.codigoCliente = codigoCliente;
    }

    public int getCodigoTelefonoCliente() {
        return codigoTelefonoCliente;
    }

    public void setCodigoTelefonoCliente(int codigoTelefonoCliente) {
        this.codigoTelefonoCliente = codigoTelefonoCliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }
    
    @Override
    public String toString() {
        return getCodigoCliente() + "";
    }
    
}
