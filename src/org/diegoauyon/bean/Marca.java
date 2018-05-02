package org.diegoauyon.bean;

public class Marca {
    private int codigoMarca;
    private String descripcion;

    public Marca(int codigoMarca, String descripcion) {
        this.codigoMarca = codigoMarca;
        this.descripcion = descripcion;
    }

    public Marca() {
    }

    public int getCodigoMarca() {
        return codigoMarca;
    }

    public void setCodigoMarca(int codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String toString() {
        return getCodigoMarca() + " | " + getDescripcion();
    }
}
