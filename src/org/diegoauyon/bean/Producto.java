package org.diegoauyon.bean;

public class Producto {
    private int codigoProducto;
    private String descripcion;
    private int existencia;
    private double precioUnitario;
    private double precioDocena;
    private double precioMayor;
    private String imagen;
    private int codigoCategoria;
    private String categoria;
    private int codigoMarca;
    private String marca;
    private int codigoTalla;
    private String talla;

    public Producto(int codigoProducto, String descripcion, int existencia, double precioUnitario, double precioDocena, double precioMayor, String imagen, int codigoCategoria, String categoria, int codigoMarca, String marca, int codigoTalla, String talla) {
        this.codigoProducto = codigoProducto;
        this.descripcion = descripcion;
        this.existencia = existencia;
        this.precioUnitario = precioUnitario;
        this.precioDocena = precioDocena;
        this.precioMayor = precioMayor;
        this.imagen = imagen;
        this.codigoCategoria = codigoCategoria;
        this.categoria = categoria;
        this.codigoMarca = codigoMarca;
        this.marca = marca;
        this.codigoTalla = codigoTalla;
        this.talla = talla;
    }

    public Producto() {
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getPrecioDocena() {
        return precioDocena;
    }

    public void setPrecioDocena(double precioDocena) {
        this.precioDocena = precioDocena;
    }

    public double getPrecioMayor() {
        return precioMayor;
    }

    public void setPrecioMayor(double precioMayor) {
        this.precioMayor = precioMayor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(int codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCodigoMarca() {
        return codigoMarca;
    }

    public void setCodigoMarca(int codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getCodigoTalla() {
        return codigoTalla;
    }

    public void setCodigoTalla(int codigoTalla) {
        this.codigoTalla = codigoTalla;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }
    
    @Override
    public String toString() {
        return this.getCodigoProducto() + " | "+ this.getDescripcion();
    }
}
