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
import org.diegoauyon.bean.Cliente;

import org.diegoauyon.sistema.Principal;
import org.diegoauyon.db.Conexion;
import org.diegoauyon.bean.TelefonoCliente;

public class TelefonoClienteController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TelefonoCliente> listaTelefonoCliente;
    private ObservableList<Cliente> listaCliente;
    
    @FXML
    private TextField txtDescripcion;
    
    @FXML
    private TextField txtNumero;
    
    @FXML
    private ComboBox cmbCliente;
    
    @FXML
    private TableView tblTelefonoCliente;
    
    @FXML
    private TableColumn colCodigo;
    
    @FXML
    private TableColumn colDescripcion;
    
    @FXML
    private TableColumn colNumero;
    
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
        cmbCliente.setItems(getClientes());
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
        }
    }
    
    public void editar() {
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTelefonoCliente.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDescripcion.setEditable(true);
                    activarControles();
                } else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una telefono.");
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
        TelefonoCliente registro = new TelefonoCliente();
        registro.setDescripcion(txtDescripcion.getText());
        registro.setNumero(txtNumero.getText());
        registro.setCodigoCliente(((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonoCliente(?,?,?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setString(2, registro.getNumero());
            procedimiento.setInt(3, registro.getCodigoCliente());
            procedimiento.execute();
            listaTelefonoCliente.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarTelefonoCliente(?,?,?,?)}");
            TelefonoCliente registro = ((TelefonoCliente)tblTelefonoCliente.getSelectionModel().getSelectedItem());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setNumero(txtNumero.getText());
            registro.setCodigoCliente(((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
            registro.setCodigoTelefonoCliente(((TelefonoCliente)tblTelefonoCliente.getSelectionModel().getSelectedItem()).getCodigoTelefonoCliente());
            procedimiento.setInt(1, registro.getCodigoTelefonoCliente());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setString(3, registro.getNumero());
            procedimiento.setInt(4, registro.getCodigoCliente());
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
                if(tblTelefonoCliente.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el registro?","Confirmacion de eliminacion",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if( respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoCliente(?)}");
                            procedimiento.setInt(1, ((TelefonoCliente)tblTelefonoCliente.getSelectionModel().getSelectedItem()).getCodigoTelefonoCliente());
                            procedimiento.execute();
                            listaTelefonoCliente.remove(tblTelefonoCliente.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una telefono.");
                }
        }
    }
    
        /* Controles */
    
    public void desactivarControles() {
        txtDescripcion.setEditable(false);
        txtNumero.setEditable(false);
        cmbCliente.setDisable(true);
    }
    
    public void activarControles() {
        txtDescripcion.setEditable(true);
        txtNumero.setEditable(true);
        cmbCliente.setDisable(false);
    }
    
    public void limpiarControles() {
        txtDescripcion.setText("");
        txtNumero.setText("");
        cmbCliente.setValue("");
    }    
        
    /* Funciones de TableView */
    
    public void cargarDatos() {
        tblTelefonoCliente.setItems(getTelefonoCliente());
        colCodigo.setCellValueFactory(new PropertyValueFactory<TelefonoCliente,Integer>("codigoTelefonoCliente"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TelefonoCliente,String>("descripcion"));
        colNumero.setCellValueFactory(new PropertyValueFactory<TelefonoCliente,String>("numero"));
    }
    
    public void seleccionarElemento() {
        cmbCliente.getSelectionModel().select(buscarCliente(((TelefonoCliente)tblTelefonoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        txtDescripcion.setText(((TelefonoCliente)tblTelefonoCliente.getSelectionModel().getSelectedItem()).getDescripcion());
        txtNumero.setText(((TelefonoCliente)tblTelefonoCliente.getSelectionModel().getSelectedItem()).getNumero());
    }
    
    public static TelefonoCliente buscarTelefonoCliente(int codigoTelefonoCliente) {
        TelefonoCliente objetoTelefonoCliente = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_BuscarTelefonoCliente(?) }");
            procedimiento.setInt(1, codigoTelefonoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()) {
                objetoTelefonoCliente = new TelefonoCliente(registro.getInt("codigoTelefonoCliente"),registro.getString("descripcion"),registro.getString("numero"),registro.getInt("codigoCliente"));
            }
        } catch (SQLException e) {
            
        }
        return objetoTelefonoCliente;
    }
    
    public Cliente buscarCliente(int codigoCliente) {
        Cliente objetoCliente = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_BuscarCliente(?) }");
            procedimiento.setInt(1, codigoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()) {
                objetoCliente = new Cliente(registro.getInt("codigoCliente"),registro.getString("nombre"),registro.getString("direccion"),registro.getString("nit"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objetoCliente;
    }
    
    public ObservableList<TelefonoCliente> getTelefonoCliente() {
        ArrayList<TelefonoCliente> lista = new ArrayList<TelefonoCliente>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTelefonosClientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TelefonoCliente(resultado.getInt("codigoTelefonoCliente"),resultado.getString("descripcion"),resultado.getString("numero"),resultado.getInt("codigoCliente")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaTelefonoCliente = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Cliente> getClientes() {
        ArrayList<Cliente> lista = new ArrayList<Cliente>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarClientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cliente(resultado.getInt("codigoCliente"),resultado.getString("nombre"),resultado.getString("direccion"), resultado.getString("nit")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaCliente = FXCollections.observableArrayList(lista);
    }
}
