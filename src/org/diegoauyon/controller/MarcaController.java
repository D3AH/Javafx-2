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
import org.diegoauyon.bean.Marca;

public class MarcaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Marca> listaMarca;
    
    @FXML
    private TextField txtDescripcion;
    
    @FXML
    private ComboBox cmbMarca;
    
    @FXML
    private TableView tblMarcas;
    
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
                if(tblMarcas.getSelectionModel().getSelectedItem() != null) {
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
        Marca registro = new Marca();
        registro.setDescripcion(txtDescripcion.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMarca(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaMarca.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarMarca(?,?)}");
            Marca registro = ((Marca)tblMarcas.getSelectionModel().getSelectedItem());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setCodigoMarca(((Marca)tblMarcas.getSelectionModel().getSelectedItem()).getCodigoMarca());
            procedimiento.setInt(1, registro.getCodigoMarca());
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
                if(tblMarcas.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el registro?","Confirmacion de eliminacion",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if( respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMarca(?)}");
                            procedimiento.setInt(1, ((Marca)tblMarcas.getSelectionModel().getSelectedItem()).getCodigoMarca());
                            procedimiento.execute();
                            listaMarca.remove(tblMarcas.getSelectionModel().getSelectedIndex());
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
        cmbMarca.setDisable(true);
    }
    
    public void activarControles() {
        txtDescripcion.setEditable(true);
        cmbMarca.setDisable(true);
    }
    
    public void limpiarControles() {
        txtDescripcion.setText("");
        cmbMarca.setValue("");
    }    
        
    /* Funciones de TableView */
    
    public void cargarDatos() {
        tblMarcas.setItems(getMarcas());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Marca,Integer>("codigoMarca"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Marca,String>("descripcion"));
    }
    
    public void seleccionarElemento() {
        cmbMarca.getSelectionModel().select(buscarMarca(((Marca)tblMarcas.getSelectionModel().getSelectedItem()).getCodigoMarca()));
        txtDescripcion.setText(((Marca)tblMarcas.getSelectionModel().getSelectedItem()).getDescripcion());
        cmbMarca.setValue(((Marca)tblMarcas.getSelectionModel().getSelectedItem()).getCodigoMarca());
    }
    
    public Marca buscarMarca(int codigoMarca) {
        Marca objetoMarca = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_BuscarMarca(?) }");
            procedimiento.setInt(1, codigoMarca);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()) {
                objetoMarca = new Marca(registro.getInt("codigoMarca"),registro.getString("descripcion"));
            }
        } catch (SQLException e) {
            
        }
        return objetoMarca;
    }
    
    public ObservableList<Marca> getMarcas() {
        ArrayList<Marca> lista = new ArrayList<Marca>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMarcas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Marca(resultado.getInt("codigoMarca"),resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaMarca = FXCollections.observableArrayList(lista);
    }
}