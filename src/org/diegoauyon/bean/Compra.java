package org.diegoauyon.bean;

import java.time.LocalDate;

public class Compra {
    private int numeroDocumento;
    private String descripcion;
    private float total;
    private LocalDate fecha;
    private int codigoProveedor;
    private String contactoPrincipal;
    private String razonSocial;
    private String nit;
    private String paginaWeb;
    private String direccion;

    public Compra() {
    }

    public Compra(int numeroDocumento, String descripcion, float total, LocalDate fecha, int codigoProveedor, String contactoPrincipal, String razonSocial, String nit, String paginaWeb, String direccion) {
        this.numeroDocumento = numeroDocumento;
        this.descripcion = descripcion;
        this.total = total;
        this.fecha = fecha;
        this.codigoProveedor = codigoProveedor;
        this.contactoPrincipal = contactoPrincipal;
        this.razonSocial = razonSocial;
        this.nit = nit;
        this.paginaWeb = paginaWeb;
        this.direccion = direccion;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public String getContactoPrincipal() {
        return contactoPrincipal;
    }

    public void setContactoPrincipal(String contactoPrincipal) {
        this.contactoPrincipal = contactoPrincipal;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public String getDirreccion() {
        return direccion;
    }

    public void setDirreccion(String direccion) {
        this.direccion = direccion;
    }
    
    @Override
    public String toString() {
        return "dl;fj";
    }
}
