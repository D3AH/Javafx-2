package org.diegoauyon.bean;

public class EmailProveedor {
    private int codigoEmailProveedor;
    private String descripcion;
    private String email;
    private int codigoProveedor;

    public EmailProveedor() {
    }

    public EmailProveedor(int codigoEmailProveedor, String descripcion, String email, int codigoProveedor) {
        this.codigoEmailProveedor = codigoEmailProveedor;
        this.descripcion = descripcion;
        this.email = email;
        this.codigoProveedor = codigoProveedor;
    }

    public int getCodigoEmailProveedor() {
        return codigoEmailProveedor;
    }

    public void setCodigoEmailProveedor(int codigoEmailProveedor) {
        this.codigoEmailProveedor = codigoEmailProveedor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
