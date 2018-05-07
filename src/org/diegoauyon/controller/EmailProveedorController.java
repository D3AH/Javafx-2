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
import org.diegoauyon.bean.EmailProveedor;

public class EmailProveedorController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<EmailProveedor> listaEmailProveedor;
    private ObservableList<Proveedor> listaProveedor;
    
    @FXML
    private TextField txtDescripcion;
    
    @FXML
    private TextField txtEmail;
    
    @FXML
    private ComboBox cmbProveedor;
    
    @FXML
    private TableView tblEmailProveedor;
    
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
                if(tblEmailProveedor.getSelectionModel().getSelectedItem() != null) {
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
        EmailProveedor registro = new EmailProveedor();
        registro.setDescripcion(txtDescripcion.getText());
        registro.setEmail(txtEmail.getText());
        registro.setCodigoProveedor(((Proveedor)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEmailProveedor(?,?,?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setString(2, registro.getEmail());
            procedimiento.setInt(3, registro.getCodigoProveedor());
            procedimiento.execute();
            listaEmailProveedor.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarEmailProveedor(?,?,?,?)}");
            EmailProveedor registro = ((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setEmail(txtEmail.getText());
            registro.setCodigoProveedor(((Proveedor)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            registro.setCodigoEmailProveedor(((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor());
            procedimiento.setInt(1, registro.getCodigoEmailProveedor());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setString(3, registro.getEmail());
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
                if(tblEmailProveedor.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el registro?","Confirmacion de eliminacion",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if( respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEmailProveedor(?)}");
                            procedimiento.setInt(1, ((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor());
                            procedimiento.execute();
                            listaEmailProveedor.remove(tblEmailProveedor.getSelectionModel().getSelectedIndex());
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
        cmbProveedor.setDisable(true);
    }
    
    public void activarControles() {
        txtDescripcion.setEditable(true);
        txtEmail.setEditable(true);
        cmbProveedor.setDisable(false);
    }
    
    public void limpiarControles() {
        txtDescripcion.setText("");
        txtEmail.setText("");
        cmbProveedor.setValue("");
    }    
        
    /* Funciones de TableView */
    
    public void cargarDatos() {
        tblEmailProveedor.setItems(getEmailProveedor());
        colCodigo.setCellValueFactory(new PropertyValueFactory<EmailProveedor,Integer>("codigoProveedor"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<EmailProveedor,String>("descripcion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<EmailProveedor,String>("email"));
    }
    
    public void seleccionarElemento() {
        cmbProveedor.getSelectionModel().select(buscarProveedor(((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        txtDescripcion.setText(((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getDescripcion());
        txtEmail.setText(((EmailProveedor)tblEmailProveedor.getSelectionModel().getSelectedItem()).getEmail());
    }
    
    public static EmailProveedor buscarEmailProveedor(int codigoEmailProveedor) {
        EmailProveedor objetoEmailProveedor = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_BuscarEmailProveedor(?) }");
            procedimiento.setInt(1, codigoEmailProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()) {
                objetoEmailProveedor = new EmailProveedor(registro.getInt("codigoEmailProveedor"),registro.getString("descripcion"),registro.getString("email"),registro.getInt("codigoProveedor"));
            }
        } catch (SQLException e) {
            
        }
        return objetoEmailProveedor;
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
    
    public ObservableList<EmailProveedor> getEmailProveedor() {
        ArrayList<EmailProveedor> lista = new ArrayList<EmailProveedor>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEmailProveedor}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new EmailProveedor(resultado.getInt("codigoEmailProveedor"),resultado.getString("descripcion"),resultado.getString("email"),resultado.getInt("codigoProveedor")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaEmailProveedor = FXCollections.observableArrayList(lista);
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
