package org.diegoauyon.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegoauyon.bean.Cliente;
import org.diegoauyon.bean.Factura;
import org.diegoauyon.db.Conexion;
import org.diegoauyon.sistema.Principal;

public class FacturaController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Factura> listaFactura;
    private ObservableList<Cliente> listaCliente;
   
    @FXML
    private TextField txtEstado;

    @FXML
    private TextField txtNit;

    @FXML
    private DatePicker dateFecha;

    @FXML
    private ComboBox cmbCliente;

    @FXML
    private TableView tblFactura;

    @FXML
    private TableColumn colCodigo;

    @FXML
    private TableColumn colEstado;

    @FXML
    private TableColumn colNit;

    @FXML
    private TableColumn colTotal;

    @FXML
    private TableColumn colFecha;

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
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    
    /* Botones */
    
    public void nuevo() {
        switch(tipoDeOperacion) {
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
        switch(tipoDeOperacion) {
            case GUARDAR:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                dateFecha.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void editar() {
        switch(tipoDeOperacion) {
            case NINGUNO:
                if(tblFactura.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtEstado.setEditable(true);
                    txtNit.setEditable(true);
                    dateFecha.setDisable(false);
                    activarControles();
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Debe seleccionar un registro.");
                    alert.showAndWait();
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
        Factura registro = new Factura();
        registro.setEstado(txtEstado.getText());
        registro.setNit(txtNit.getText());
        registro.setFecha(dateFecha.getValue());
        registro.setCodigoCliente(((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarFactura(?,?,?,?) }");
            procedimiento.setString(1, registro.getEstado());
            procedimiento.setString(2, registro.getNit());
            procedimiento.setDate(3, java.sql.Date.valueOf(registro.getFecha()));
            procedimiento.setInt(4, registro.getCodigoCliente());
            procedimiento.execute();
            listaFactura.add(registro);
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
                if(tblFactura.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setHeaderText("Confirmacion de eliminacion");
                    alert.setContentText("Esta seguro que desea eliminar el registro?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.get() == ButtonType.OK) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarFactura(?)");                            
                            procedimiento.setInt(1, ((Factura)tblFactura.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedimiento.execute();
                            listaFactura.remove(tblFactura.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Alert alertInfo = new Alert(AlertType.INFORMATION);
                        alertInfo.setHeaderText(null);
                        alertInfo.setContentText("Debe seleccionar un registro.");
                        alertInfo.showAndWait();
                    }
                }
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ActualizarFactura(?,?,?,?,?)");
            Factura registro = ((Factura)tblFactura.getSelectionModel().getSelectedItem());
            registro.setEstado(txtEstado.getText());
            registro.setNit(txtEstado.getText());
            registro.setFecha(dateFecha.getValue());
            registro.setCodigoCliente(((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
            registro.setNumeroFactura(((Factura)tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
            procedimiento.setInt(1, registro.getNumeroFactura());
            procedimiento.setString(2, registro.getEstado());
            procedimiento.setString(3, registro.getNit());
            procedimiento.setDate(4, java.sql.Date.valueOf(registro.getFecha()));
            procedimiento.setInt(5, registro.getCodigoCliente());
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /* Controles */
    
    public void activarControles() {
        txtEstado.setEditable(true);
        txtNit.setEditable(true);
        dateFecha.setDisable(false);
        cmbCliente.setDisable(false);
    }
    
    public void limpiarControles() {
        txtEstado.setText("");
        txtNit.setText("");
        dateFecha.setValue(null);
        cmbCliente.setValue("");
    }
    
    public void desactivarControles() {
        txtEstado.setEditable(false);
        txtNit.setEditable(false);
        dateFecha.setEditable(false);
        cmbCliente.setDisable(true);
        dateFecha.setDisable(true);
    }
    
    /* Funciones de TableView */
    
    public void cargarDatos() {
        tblFactura.setItems(getFacturas());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("codigoTelefonoCliente"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Factura, String>("estado"));
        colNit.setCellValueFactory(new PropertyValueFactory<Factura, String>("nit"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("total"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Factura, String>("fecha"));
    }
    
    public void seleccionarElemento() {
        
    }
    
    public ObservableList<Factura> getFacturas() {
        ArrayList<Factura> lista = new ArrayList<Factura>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_ListarFacturas }");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Factura(resultado.getInt("numeroFactura"), resultado.getString("estado"), resultado.getString("nit"), resultado.getInt("total"),(resultado.getDate("fecha").toLocalDate()), resultado.getInt("codigoCliente")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaFactura = FXCollections.observableArrayList(lista);
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
