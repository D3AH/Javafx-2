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
import org.diegoauyon.bean.Talla;

public class TallaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Talla> listaTalla;
    
    @FXML
    private TextField txtDescripcion;
    
    @FXML
    private ComboBox cmbTalla;
    
    @FXML
    private TableView tblTallas;
    
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
    
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
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
            case ACTUALIZAR:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void editar() {
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTallas.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDescripcion.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una talla.");
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
        Talla registro = new Talla();
        registro.setDescripcion(txtDescripcion.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTalla(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaTalla.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarTalla(?,?)}");
            Talla registro = ((Talla)tblTallas.getSelectionModel().getSelectedItem());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setCodigoTalla(((Talla)tblTallas.getSelectionModel().getSelectedItem()).getCodigoTalla());
            procedimiento.setInt(1, registro.getCodigoTalla());
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
                if(tblTallas.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el registro?","Confirmacion de eliminacion",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if( respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTalla(?)}");
                            procedimiento.setInt(1, ((Talla)tblTallas.getSelectionModel().getSelectedItem()).getCodigoTalla());
                            procedimiento.execute();
                            listaTalla.remove(tblTallas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una talla.");
                }
        }
    }
    
        /* Controles */
    
    public void desactivarControles() {
        txtDescripcion.setEditable(false);
        cmbTalla.setDisable(true);
    }
    
    public void activarControles() {
        txtDescripcion.setEditable(true);
        cmbTalla.setDisable(true);
    }
    
    public void limpiarControles() {
        txtDescripcion.setText("");
        cmbTalla.setValue("");
    }    
        
    /* Funciones de TableView */
    
    public void cargarDatos() {
        tblTallas.setItems(getTallas());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Talla,Integer>("codigoTalla"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Talla,String>("descripcion"));
    }
    
    public void seleccionarElemento() {
        cmbTalla.getSelectionModel().select(buscarTalla(((Talla)tblTallas.getSelectionModel().getSelectedItem()).getCodigoTalla()));
        txtDescripcion.setText(((Talla)tblTallas.getSelectionModel().getSelectedItem()).getDescripcion());
        cmbTalla.setValue(((Talla)tblTallas.getSelectionModel().getSelectedItem()).getCodigoTalla());
    }
    
    public static Talla buscarTalla(int codigoTalla) {
        Talla objetoTalla = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_BuscarTalla(?) }");
            procedimiento.setInt(1, codigoTalla);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()) {
                objetoTalla = new Talla(registro.getInt("codigoTalla"),registro.getString("descripcion"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objetoTalla;
    }
    
    public ObservableList<Talla> getTallas() {
        ArrayList<Talla> lista = new ArrayList<Talla>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTallas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Talla(resultado.getInt("codigoTalla"),resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaTalla = FXCollections.observableArrayList(lista);
    }
}