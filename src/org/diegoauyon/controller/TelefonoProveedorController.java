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
import org.diegoauyon.bean.Proveedor;

import org.diegoauyon.sistema.Principal;
import org.diegoauyon.db.Conexion;
import org.diegoauyon.bean.TelefonoProveedor;

public class TelefonoProveedorController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TelefonoProveedor> listaTelefonoProveedor;
    private ObservableList<Proveedor> listaProveedor;
    
    @FXML
    private TextField txtDescripcion;
    
    @FXML
    private TextField txtNumero;
    
    @FXML
    private ComboBox cmbProveedor;
    
    @FXML
    private TableView tblTelefonoProveedor;
    
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
        cmbProveedor.setItems(getProveedores());
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
                if(tblTelefonoProveedor.getSelectionModel().getSelectedItem() != null) {
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
        TelefonoProveedor registro = new TelefonoProveedor();
        registro.setDescripcion(txtDescripcion.getText());
        registro.setNumero(txtNumero.getText());
        registro.setCodigoProveedor(((Proveedor)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonoProveedor(?,?,?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setString(2, registro.getNumero());
            procedimiento.setInt(3, registro.getCodigoProveedor());
            procedimiento.execute();
            listaTelefonoProveedor.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarTelefonoProveedor(?,?,?,?)}");
            TelefonoProveedor registro = ((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setNumero(txtNumero.getText());
            registro.setCodigoProveedor(((Proveedor)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            registro.setCodigoTelefonoProveedor(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor());
            procedimiento.setInt(1, registro.getCodigoTelefonoProveedor());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setString(3, registro.getNumero());
            procedimiento.setInt(4, registro.getCodigoProveedor());
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
                if(tblTelefonoProveedor.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el registro?","Confirmacion de eliminacion",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if( respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoProveedor(?)}");
                            procedimiento.setInt(1, ((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor());
                            procedimiento.execute();
                            listaTelefonoProveedor.remove(tblTelefonoProveedor.getSelectionModel().getSelectedIndex());
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
        cmbProveedor.setDisable(true);
    }
    
    public void activarControles() {
        txtDescripcion.setEditable(true);
        txtNumero.setEditable(true);
        cmbProveedor.setDisable(false);
    }
    
    public void limpiarControles() {
        txtDescripcion.setText("");
        txtNumero.setText("");
        cmbProveedor.setValue("");
    }    
        
    /* Funciones de TableView */
    
    public void cargarDatos() {
        tblTelefonoProveedor.setItems(getTelefonoProveedor());
        colCodigo.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor,Integer>("codigoProveedor"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor,String>("descripcion"));
        colNumero.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor,String>("numero"));
    }
    
    public void seleccionarElemento() {
        cmbProveedor.getSelectionModel().select(buscarProveedor(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        txtDescripcion.setText(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getDescripcion());
        txtNumero.setText(((TelefonoProveedor)tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getNumero());
    }
    
    public static TelefonoProveedor buscarTelefonoProveedor(int codigoTelefonoProveedor) {
        TelefonoProveedor objetoTelefonoProveedor = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_BuscarTelefonoProveedor(?) }");
            procedimiento.setInt(1, codigoTelefonoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()) {
                objetoTelefonoProveedor = new TelefonoProveedor(registro.getInt("codigoTelefonoProveedor"),registro.getString("descripcion"),registro.getString("numero"),registro.getInt("codigoProveedor"));
            }
        } catch (SQLException e) {
            
        }
        return objetoTelefonoProveedor;
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
    
    public ObservableList<TelefonoProveedor> getTelefonoProveedor() {
        ArrayList<TelefonoProveedor> lista = new ArrayList<TelefonoProveedor>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTelefonosProveedores}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TelefonoProveedor(resultado.getInt("codigoTelefonoProveedor"),resultado.getString("descripcion"),resultado.getString("numero"),resultado.getInt("codigoProveedor")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaTelefonoProveedor = FXCollections.observableArrayList(lista);
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
