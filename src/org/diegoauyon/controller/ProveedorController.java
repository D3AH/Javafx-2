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
import org.diegoauyon.bean.Proveedor;

public class ProveedorController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Proveedor> listaProveedor;
    
    @FXML private ComboBox cmbProveedor;
    
    @FXML private TextField txtContactoPrincipal;
    
    @FXML private TextField txtRazonSocial;
    
    @FXML private TextField txtNit;
    
    @FXML private TextField txtPaginaWeb;
    
    @FXML private TextField txtDireccion;
    
    @FXML private TableView tblProveedores;
    
    @FXML private TableColumn colCodigo;
    
    @FXML private TableColumn colContactoPrincipal;
    
    @FXML private TableColumn colRazonSocial;
    
    @FXML private TableColumn colNit;
    
    @FXML private TableColumn colPaginaWeb;
    
    @FXML private TableColumn colDireccion;
    
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
        }
    }
    
    public void editar() {
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblProveedores.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtContactoPrincipal.setEditable(true);
                    txtDireccion.setEditable(true);
                    txtNit.setEditable(true);
                    txtDireccion.setEditable(true);
                    txtPaginaWeb.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una proveedor.");
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
        Proveedor registro = new Proveedor();
        registro.setContactoPrincipal(txtContactoPrincipal.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setNit(txtNit.getText());
        registro.setRazonSocial(txtRazonSocial.getText());
        registro.setPaginaWeb(txtPaginaWeb.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarProveedor(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getContactoPrincipal());
            procedimiento.setString(2, registro.getRazonSocial());
            procedimiento.setString(3, registro.getNit());
            procedimiento.setString(4, registro.getPaginaWeb());
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.execute();
            listaProveedor.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarProveedor(?,?,?,?,?,?)}");
            Proveedor registro = ((Proveedor)tblProveedores.getSelectionModel().getSelectedItem());
            registro.setContactoPrincipal(txtContactoPrincipal.getText());
            registro.setDireccion(txtDireccion.getText());
            registro.setRazonSocial(txtRazonSocial.getText());
            registro.setPaginaWeb(txtPaginaWeb.getText());
            registro.setNit(txtNit.getText());
            registro.setCodigoProveedor(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getContactoPrincipal());  
            procedimiento.setString(3, registro.getDireccion());  
            procedimiento.setString(4, registro.getRazonSocial());  
            procedimiento.setString(5, registro.getPaginaWeb());  
            procedimiento.setString(6, registro.getNit());  
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
                if(tblProveedores.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el registro?","Confirmacion de eliminacion",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if( respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProveedor(?)}");
                            procedimiento.setInt(1, ((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listaProveedor.remove(tblProveedores.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una proveedor.");
                }
        }
    }
    
        /* Controles */
    
    public void desactivarControles() {
        txtContactoPrincipal.setEditable(false);
        txtDireccion.setEditable(false);
        txtNit.setEditable(false);
        txtRazonSocial.setEditable(false);
        txtPaginaWeb.setEditable(false);
        cmbProveedor.setDisable(true);
    }
    
    public void activarControles() {
        txtContactoPrincipal.setEditable(true);
        txtDireccion.setEditable(true);
        txtRazonSocial.setEditable(true);
        txtPaginaWeb.setEditable(true);
        txtNit.setEditable(true);
        cmbProveedor.setDisable(true);
    }
    
    public void limpiarControles() {
        txtContactoPrincipal.setText("");
        txtDireccion.setText("");
        txtNit.setText("");
        txtRazonSocial.setText("");
        txtPaginaWeb.setText("");
        cmbProveedor.setValue("");
    }    
        
    /* Funciones de TableView */
    
    public void cargarDatos() {
        tblProveedores.setItems(getProveedores());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Proveedor,Integer>("codigoProveedor"));
        colContactoPrincipal.setCellValueFactory(new PropertyValueFactory<Proveedor,String>("contactoPrincipal"));
        colRazonSocial.setCellValueFactory(new PropertyValueFactory<Proveedor,String>("razonSocial"));
        colPaginaWeb.setCellValueFactory(new PropertyValueFactory<Proveedor,String>("paginaWeb"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedor,String>("direccion"));
        colNit.setCellValueFactory(new PropertyValueFactory<Proveedor,String>("nit"));
    }
    
    public void seleccionarElemento() {
        cmbProveedor.setValue(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        txtContactoPrincipal.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getContactoPrincipal());
        txtPaginaWeb.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getPaginaWeb());
        txtRazonSocial.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getRazonSocial());
        txtDireccion.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getDireccion());
        txtNit.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getNit());
        
    }
    
    public Proveedor buscarProveedor(int codigoProveedor) {
        Proveedor objetoProveedor = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_BuscarProveedor(?) }");
            procedimiento.setInt(1, codigoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()) {
                objetoProveedor = new Proveedor(registro.getInt("codigoProveedor"),registro.getString("contactoPrincipal"),registro.getString("razonSocial"),registro.getString("nit"),registro.getString("paginaWeb"),registro.getString("direccion"));
            }
        } catch (SQLException e) {
            
        }
        return objetoProveedor;
    }
    
    public ObservableList<Proveedor> getProveedores() {
        ArrayList<Proveedor> lista = new ArrayList<Proveedor>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarProveedores}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Proveedor(resultado.getInt("codigoProveedor"),resultado.getString("contactoPrincipal"),resultado.getString("razonSocial"),resultado.getString("nit"),resultado.getString("paginaWeb"),resultado.getString("direccion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaProveedor = FXCollections.observableArrayList(lista);
    }
}