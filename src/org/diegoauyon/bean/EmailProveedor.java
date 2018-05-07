package org.diegoauyon.bean;

public class EmailProveedor {
    private int codigoEmailProveedor;
    private String email;
    private String descripcion;
    private int codigoProveedor;

    public EmailProveedor() {
    }

    public EmailProveedor(int codigoEmailProveedor, String email, String descripcion, int codigoProveedor) {
        this.codigoEmailProveedor = codigoEmailProveedor;
        this.email = email;
        this.descripcion = descripcion;
        this.codigoProveedor = codigoProveedor;
    }

    public int getCodigoEmailProveedor() {
        return codigoEmailProveedor;
    }

    public void setCodigoEmailProveedor(int codigoEmailProveedor) {
        this.codigoEmailProveedor = codigoEmailProveedor;
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

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }
    
    @Override
    public String toString() {
        return getCodigoEmailProveedor() + "";
    }
}
