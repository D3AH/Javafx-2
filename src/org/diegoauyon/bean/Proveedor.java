package org.diegoauyon.bean;

public class Proveedor {
    private int codigoProveedor;
    private String contactoPrincipal;
    private String razonSocial;
    private String nit;
    private String paginaWeb;
    private String dirreccion;

    public Proveedor() {
    }

    public Proveedor(int codigoProveedor, String contactoPrincipal, String razonSocial, String nit, String paginaWeb, String dirreccion) {
        this.codigoProveedor = codigoProveedor;
        this.contactoPrincipal = contactoPrincipal;
        this.razonSocial = razonSocial;
        this.nit = nit;
        this.paginaWeb = paginaWeb;
        this.dirreccion = dirreccion;
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
        return dirreccion;
    }

    public void setDirreccion(String dirreccion) {
        this.dirreccion = dirreccion;
    }
    
    public String toString() {
        return getCodigoProveedor() + " | " + getContactoPrincipal();
    }
}
