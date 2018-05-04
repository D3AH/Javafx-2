package org.diegoauyon.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

import org.diegoauyon.sistema.Principal;
import org.diegoauyon.db.Conexion;
import org.diegoauyon.bean.Categoria;

public class CategoriaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Categoria> listaCategoria;
    
    @FXML
    private TextField txtDescripcion;
    
    @FXML
    private ComboBox cmbCategoria;
    
    @FXML
    private TableView tblCategorias;
    
    @FXML
    private TableColumn colCodigo;
    
    @FXML
    private TableColumn colDescripcion;
    
    @FXML
    private Button btnNuevo;
    
    @FXML
    private Button btnEliminar;
    
    @FXML
    private Button btnEditar;
    
    @FXML
    private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    /* Botones */
    
    public void nuevo() {
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                agregar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void cancelar() {
        switch (tipoDeOperacion){
            case GUARDAR:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void editar() {
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblCategorias.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDescripcion.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una categoria.");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                limpiarControles();
                desactivarControles();
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
        }
    }
    
    /* Funciones de botones */

    public void agregar() {
        Categoria registro = new Categoria();
        registro.setDescripcion(txtDescripcion.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCategoria(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaCategoria.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarCategoria(?,?)}");
            Categoria registro = ((Categoria)tblCategorias.getSelectionModel().getSelectedItem());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setCodigoCategoria(((Categoria)tblCategorias.getSelectionModel().getSelectedItem()).getCodigoCategoria());
            procedimiento.setInt(1, registro.getCodigoCategoria());
            procedimiento.setString(2, registro.getDescripcion());  
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar() {
        switch(tipoDeOperacion) {
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblCategorias.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el registro?","Confirmacion de eliminacion",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if( respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCategoria(?)}");
                            procedimiento.setInt(1, ((Categoria)tblCategorias.getSelectionModel().getSelectedItem()).getCodigoCategoria());
                            procedimiento.execute();
                            listaCategoria.remove(tblCategorias.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una categoria.");
                }
        }
    }
    
        /* Controles */
    
    public void desactivarControles() {
        txtDescripcion.setEditable(false);
        cmbCategoria.setDisable(true);
    }
    
    public void activarControles() {
        txtDescripcion.setEditable(true);
        cmbCategoria.setDisable(true);
    }
    
    public void limpiarControles() {
        txtDescripcion.setText("");
        cmbCategoria.setValue("");
    }    
        
    /* Funciones de TableView */
    
    public void cargarDatos() {
        tblCategorias.setItems(getCategorias());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Categoria,Integer>("codigoCategoria"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Categoria,String>("descripcion"));
    }
    
    public void seleccionarElemento() {
        cmbCategoria.getSelectionModel().select(buscarCategoria(((Categoria)tblCategorias.getSelectionModel().getSelectedItem()).getCodigoCategoria()));
        txtDescripcion.setText(((Categoria)tblCategorias.getSelectionModel().getSelectedItem()).getDescripcion());
        cmbCategoria.setValue(((Categoria)tblCategorias.getSelectionModel().getSelectedItem()).getCodigoCategoria());
    }
    
    public static Categoria buscarCategoria(int codigoCategoria) {
        Categoria objetoCategoria = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_BuscarCategoria(?) }");
            procedimiento.setInt(1, codigoCategoria);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()) {
                objetoCategoria = new Categoria(registro.getInt("codigoCategoria"),registro.getString("descripcion"));
            }
        } catch (SQLException e) {
            
        }
        return objetoCategoria;
    }
    
    public ObservableList<Categoria> getCategorias() {
        ArrayList<Categoria> lista = new ArrayList<Categoria>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCategorias}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Categoria(resultado.getInt("codigoCategoria"),resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaCategoria = FXCollections.observableArrayList(lista);
    }
}