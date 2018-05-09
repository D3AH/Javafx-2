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
import org.diegoauyon.bean.Cliente;

public class ClienteController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Cliente> listaCliente;
    
    @FXML private ComboBox cmbCliente;
    
    @FXML private TextField txtNombre;
    
    @FXML private TextField txtDireccion;
    
    @FXML private TextField txtNit;
    
    @FXML private TableView tblClientes;
    
    @FXML private TableColumn colCodigo;
    
    @FXML private TableColumn colNombre;
    
    @FXML private TableColumn colDireccion;
    
    @FXML private TableColumn colNit;
    
    @FXML private Button btnNuevo;
    
    @FXML private Button btnEliminar;
    
    @FXML private Button btnEditar;
    
    @FXML private Button btnReporte;
    
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
                if(tblClientes.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtNombre.setEditable(true);
                    txtDireccion.setEditable(true);
                    txtNit.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una cliente.");
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
        Cliente registro = new Cliente();
        registro.setNombre(txtNombre.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setNit(txtNit.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCliente(?,?,?)}");
            procedimiento.setString(1, registro.getNombre());
            procedimiento.setString(2, registro.getDireccion());
            procedimiento.setString(3, registro.getNit());
            procedimiento.execute();
            listaCliente.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarCliente(?,?,?,?)}");
            Cliente registro = ((Cliente)tblClientes.getSelectionModel().getSelectedItem());
            registro.setNombre(txtNombre.getText());
            registro.setDireccion(txtDireccion.getText());
            registro.setNit(txtNit.getText());
            registro.setCodigoCliente(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getNombre());  
            procedimiento.setString(3, registro.getDireccion());  
            procedimiento.setString(4, registro.getNit());  
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
                if(tblClientes.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el registro?","Confirmacion de eliminacion",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if( respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCliente(?)}");
                            procedimiento.setInt(1, ((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedimiento.execute();
                            listaCliente.remove(tblClientes.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una cliente.");
                }
        }
    }
    
        /* Controles */
    
    public void desactivarControles() {
        txtNombre.setEditable(false);
        txtDireccion.setEditable(false);
        txtNit.setEditable(false);
        cmbCliente.setDisable(true);
    }
    
    public void activarControles() {
        txtNombre.setEditable(true);
        txtDireccion.setEditable(true);
        txtNit.setEditable(true);
        cmbCliente.setDisable(true);
    }
    
    public void limpiarControles() {
        txtNombre.setText("");
        txtDireccion.setText("");
        txtNit.setText("");
        cmbCliente.setValue("");
    }    
        
    /* Funciones de TableView */
    
    public void cargarDatos() {
        tblClientes.setItems(getClientes());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Cliente,Integer>("codigoCliente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cliente,String>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Cliente,String>("direccion"));
        colNit.setCellValueFactory(new PropertyValueFactory<Cliente,String>("nit"));
    }
    
    public void seleccionarElemento() {
        cmbCliente.setValue(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
        txtNombre.setText(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getNombre());
        txtDireccion.setText(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getDireccion());
        txtNit.setText(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getNit());
        
    }
    
    public static Cliente buscarCliente(int codigoCliente) {
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