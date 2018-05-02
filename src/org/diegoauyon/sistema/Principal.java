package org.diegoauyon.sistema;

import java.io.InputStream;
/* JavaFX */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
/* Proyect's Class */
import org.diegoauyon.controller.MenuPrincipalController;
import org.diegoauyon.controller.CategoriaController;
import org.diegoauyon.controller.MarcaController;
import org.diegoauyon.controller.ProductoController;
    

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
    
    public void ventanaProductos() {
        try {
            ProductoController productoController = (ProductoController) cambiarEscena("ProductoView");
            productoController.setEscenarioPrincipal(this);
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
