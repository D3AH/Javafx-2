package org.diegoauyon.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.diegoauyon.sistema.Principal;

public class ProductoController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    @FXML private TextField txtDescripcion;
    
    @FXML private TextField txtImagen;
    
    @FXML private ComboBox cmbCategoria;
    
    @FXML private ComboBox cmbMarca;
    
    @FXML private ComboBox cmbTalla;
    
    @FXML private TableView tblProductos;
    
    @FXML private TableColumn colCodigo;
    
    @FXML private TableColumn colDescripcion;
    
    @FXML private TableColumn colCategoria;
    
    @FXML private TableColumn colMarca;
    
    @FXML private TableColumn colTalla;
    
    @FXML private TableColumn colPrecioUnitario;
    
    @FXML private TableColumn colPrecioDocena;
    
    @FXML private TableColumn colPrecioMayor;
    
    @FXML private TableColumn colExistencia;
    
    @FXML private Button btnNuevo;
    
    @FXML private Button btnEliminar;
    
    @FXML private Button btnEditar;
    
    @FXML private Button btnReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    /* Control de botones */
    
    public void desactivarControles() {
        
    }
    
    public void activarControles() {
        
    }
    
    public void limpiarControles() {
        
    }
}