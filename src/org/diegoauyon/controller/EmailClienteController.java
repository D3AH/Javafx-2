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
import org.diegoauyon.bean.EmailCliente;

public class EmailClienteController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<EmailCliente> listaEmailCliente;
    private ObservableList<Cliente> listaCliente;
    
    @FXML
    private TextField txtDescripcion;
    
    @FXML
    private TextField txtEmail;
    
    @FXML
    private ComboBox cmbCliente;
    
    @FXML
    private TableView tblEmailCliente;
    
    @FXML
    private TableColumn colCodigo;
    
    @FXML
    private TableColumn colDescripcion;
    
    @FXML
    private TableColumn colEmail;
    
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
                if(tblEmailCliente.getSelectionModel().getSelectedItem() != null) {
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
        EmailCliente registro = new EmailCliente();
        registro.setDescripcion(txtDescripcion.getText());
        registro.setEmail(txtEmail.getText());
        registro.setCodigoCliente(((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEmailCliente(?,?,?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setString(2, registro.getEmail());
            procedimiento.setInt(3, registro.getCodigoCliente());
            procedimiento.execute();
            listaEmailCliente.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarEmailCliente(?,?,?,?)}");
            EmailCliente registro = ((EmailCliente)tblEmailCliente.getSelectionModel().getSelectedItem());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setEmail(txtEmail.getText());
            registro.setCodigoCliente(((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
            registro.setCodigoEmailCliente(((EmailCliente)tblEmailCliente.getSelectionModel().getSelectedItem()).getCodigoEmailCliente());
            procedimiento.setInt(1, registro.getCodigoEmailCliente());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setString(3, registro.getEmail());
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
                if(tblEmailCliente.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el registro?","Confirmacion de eliminacion",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if( respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEmailCliente(?)}");
                            procedimiento.setInt(1, ((EmailCliente)tblEmailCliente.getSelectionModel().getSelectedItem()).getCodigoEmailCliente());
                            procedimiento.execute();
                            listaEmailCliente.remove(tblEmailCliente.getSelectionModel().getSelectedIndex());
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
        txtEmail.setEditable(false);
        cmbCliente.setDisable(true);
    }
    
    public void activarControles() {
        txtDescripcion.setEditable(true);
        txtEmail.setEditable(true);
        cmbCliente.setDisable(false);
    }
    
    public void limpiarControles() {
        txtDescripcion.setText("");
        txtEmail.setText("");
        cmbCliente.setValue("");
    }    
        
    /* Funciones de TableView */
    
    public void cargarDatos() {
        tblEmailCliente.setItems(getEmailCliente());
        colCodigo.setCellValueFactory(new PropertyValueFactory<EmailCliente,Integer>("codigoEmailCliente"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<EmailCliente,String>("descripcion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<EmailCliente,String>("email"));
    }
    
    public void seleccionarElemento() {
        cmbCliente.getSelectionModel().select(buscarCliente(((EmailCliente)tblEmailCliente.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        txtDescripcion.setText(((EmailCliente)tblEmailCliente.getSelectionModel().getSelectedItem()).getDescripcion());
        txtEmail.setText(((EmailCliente)tblEmailCliente.getSelectionModel().getSelectedItem()).getEmail());
    }
    
    public static EmailCliente buscarEmailCliente(int codigoEmailCliente) {
        EmailCliente objetoEmailCliente = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_BuscarEmailCliente(?) }");
            procedimiento.setInt(1, codigoEmailCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()) {
                objetoEmailCliente = new EmailCliente(registro.getInt("codigoEmailCliente"),registro.getString("descripcion"),registro.getString("email"),registro.getInt("codigoCliente"));
            }
        } catch (SQLException e) {
            
        }
        return objetoEmailCliente;
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
    
    public ObservableList<EmailCliente> getEmailCliente() {
        ArrayList<EmailCliente> lista = new ArrayList<EmailCliente>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEmailCliente}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new EmailCliente(resultado.getInt("codigoEmailCliente"),resultado.getString("descripcion"),resultado.getString("email"),resultado.getInt("codigoCliente")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaEmailCliente = FXCollections.observableArrayList(lista);
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
