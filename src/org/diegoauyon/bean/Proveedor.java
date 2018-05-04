package org.diegoauyon.bean;

public class Proveedor {
    private int codigoProveedor;
    private String contactoPrincipal;
    private String razonSocial;
    private String nit;
    private String paginaWeb;
    private String direccion;

    public Proveedor() {
    }

    public Proveedor(int codigoProveedor, String contactoPrincipal, String razonSocial, String nit, String paginaWeb, String direccion) {
        this.codigoProveedor = codigoProveedor;
        this.contactoPrincipal = contactoPrincipal;
        this.razonSocial = razonSocial;
        this.nit = nit;
        this.paginaWeb = paginaWeb;
        this.direccion = direccion;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String toString() {
        return getCodigoProveedor() + " | " + getContactoPrincipal();
    }
}
