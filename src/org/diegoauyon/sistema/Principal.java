package org.diegoauyon.sistema;

import java.io.InputStream;
/* JavaFX */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
/* Proyect's Class */
import org.diegoauyon.controller.MenuPrincipalController;
import org.diegoauyon.controller.CategoriaController;
import org.diegoauyon.controller.ClienteController;
import org.diegoauyon.controller.MarcaController;
import org.diegoauyon.controller.ProductoController;
import org.diegoauyon.controller.ProveedorController;
import org.diegoauyon.controller.TallaController;
import org.diegoauyon.controller.TelefonoProveedorController;
    

public class Principal extends Application {
    
    private final String PAQUETE_VISTA = "/org/diegoauyon/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start (Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Application");
        menuPrincipal();
        escenarioPrincipal.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public void menuPrincipal() {
        try {
            MenuPrincipalController menuPrincipal = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml");
            menuPrincipal.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaCategorias() {
        try {
            CategoriaController categoriaController = (CategoriaController) cambiarEscena("CategoriaView.fxml");
            categoriaController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaMarcas() {
        try {
            MarcaController marcaController = (MarcaController) cambiarEscena("MarcaView.fxml");
            marcaController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaTallas() {
        try {
            TallaController tallaController = (TallaController) cambiarEscena("TallaView.fxml");
            tallaController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaClientes() {
        try {
            ClienteController tallaController = (ClienteController) cambiarEscena("ClienteView.fxml");
            tallaController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaProductos() {
        try {
            ProductoController productoController = (ProductoController) cambiarEscena("ProductoView.fxml");
            productoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaProveedores() {
        try {
            ProveedorController tallaController = (ProveedorController) cambiarEscena("ProveedorView.fxml");
            tallaController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaTelefonoProveedores() {
        try {
            TelefonoProveedorController tallaController = (TelefonoProveedorController) cambiarEscena("TelefonoProveedorView.fxml");
            tallaController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Initializable cambiarEscena(String fxml) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        escena = new Scene(cargadorFXML.load(archivo));
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }    
}
