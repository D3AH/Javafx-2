package org.diegoauyon.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.diegoauyon.sistema.Principal;

public class MenuPrincipalController implements Initializable {
    
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaCategorias(){
        escenarioPrincipal.ventanaCategorias();
    }
    
    public void ventanaMarcas(){
        escenarioPrincipal.ventanaMarcas();
    }
    
    public void ventanaProductos(){
        escenarioPrincipal.ventanaProductos();
    }
    
    public void ventanaTallas(){
        escenarioPrincipal.ventanaTallas();
    }
    
    public void ventanaClientes(){
        escenarioPrincipal.ventanaClientes();
    }
    
    public void ventanaProveedores(){
        escenarioPrincipal.ventanaProveedores();
    }
    
    public void ventanaTelefonoProveedores(){
        escenarioPrincipal.ventanaTelefonoProveedores();
    }
    
}
