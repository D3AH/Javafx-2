package org.diegoauyon.bean;

public class Categoria {
    private int codigoCategoria;
    private String descripcion;

    public Categoria(int codigoCategoria, String descripcion) {
        this.codigoCategoria = codigoCategoria;
        this.descripcion = descripcion;
    }

    public Categoria() {
    }

    public int getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(int codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String toString() {
        return getCodigoCategoria() + " | " + getDescripcion();
    }
    
    
}
    