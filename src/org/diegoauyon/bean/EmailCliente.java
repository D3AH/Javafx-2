package org.diegoauyon.bean;

public class EmailCliente {
    private int codigoEmailCliente;
    private String email;
    private String descripcion;
    private int codigoCliente;

    public EmailCliente() {
    }

    public EmailCliente(int codigoEmailCliente, String email, String descripcion, int codigoCliente) {
        this.codigoEmailCliente = codigoEmailCliente;
        this.email = email;
        this.descripcion = descripcion;
        this.codigoCliente = codigoCliente;
    }

    public int getCodigoEmailCliente() {
        return codigoEmailCliente;
    }

    public void setCodigoEmailCliente(int codigoEmailCliente) {
        this.codigoEmailCliente = codigoEmailCliente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }
    
    @Override
    public String toString() {
        return getCodigoEmailCliente() + "";
    }
}
