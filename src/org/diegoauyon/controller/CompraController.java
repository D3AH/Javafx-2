package org.diegoauyon.controller;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegoauyon.bean.Proveedor;
import org.diegoauyon.bean.Compra;
import org.diegoauyon.db.Conexion;
import org.diegoauyon.reporte.GenerarReporte;
import org.diegoauyon.sistema.Principal;

public class CompraController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Compra> listaCompras;
    private ObservableList<Proveedor> listaProveedores;
    
    @FXML private TextField txtNumeroDocumento;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtContactoPrincipal;
    @FXML private DatePicker dateFecha;
    @FXML private ComboBox cmbProveedor;
    @FXML private TableView tblCompras;
    @FXML private TableColumn colNumeroDocumento;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colContactoPrincipal;
    @FXML private TableColumn colTotal;
    @FXML private TableColumn colFecha;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        cargarDatos();
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
                tipoDeOperacion = CompraController.operaciones.GUARDAR;
                break;
            case GUARDAR:
                agregar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                tipoDeOperacion = CompraController.operaciones.NINGUNO;
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
                tipoDeOperacion = CompraController.operaciones.NINGUNO;
                break;
        }
    }
    
    public void editar() {
        switch(tipoDeOperacion) {
            case NINGUNO:
                if(tblCompras.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = CompraController.operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDescripcion.setEditable(true);
                    txtContactoPrincipal.setEditable(true);
                    dateFecha.setDisable(false);
                    activarControles();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
                tipoDeOperacion = CompraController.operaciones.NINGUNO;
        }
    }
    
    public void detalle() {
        if(tblCompras.getSelectionModel().getSelectedItem() != null) {
            escenarioPrincipal.ventanaDetalleCompra((Compra)tblCompras.getSelectionModel().getSelectedItem());
        } else {
            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
            alertInfo.setHeaderText(null);
            alertInfo.setContentText("Debe seleccionar un registro.");
            alertInfo.showAndWait();
        }
    }    
    
    /* Funciones de botones */
    
    public void agregar() {
        Compra registro = new Compra();
        registro.setNumeroDocumento(Integer.parseInt(txtNumeroDocumento.getText()));
        registro.setDescripcion(txtDescripcion.getText());
        registro.setFecha(dateFecha.getValue());
        registro.setCodigoProveedor(((Proveedor)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCompra(?,?,?,?) }");
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setDate(3, java.sql.Date.valueOf(registro.getFecha()));
            procedimiento.setInt(4, registro.getCodigoProveedor());
            procedimiento.execute();
            listaCompras.add(registro);
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
                tipoDeOperacion = CompraController.operaciones.NINGUNO;
                break;
            default:
                if(tblCompras.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Confirmacion de eliminacion");
                    alert.setContentText("Esta seguro que desea eliminar el registro?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.get() == ButtonType.OK) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarCompra(?)");                            
                            procedimiento.setInt(1, ((Compra)tblCompras.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listaCompras.remove(tblCompras.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
                        alertInfo.setHeaderText(null);
                        alertInfo.setContentText("Debe seleccionar un registro.");
                        alertInfo.showAndWait();
                    }
                }
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ActualizarCompra(?,?,?,?,?)");
            Compra registro = ((Compra)tblCompras.getSelectionModel().getSelectedItem());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setContactoPrincipal(txtDescripcion.getText());
            registro.setFecha(dateFecha.getValue());
            registro.setCodigoProveedor(((Proveedor)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            registro.setNumeroDocumento(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setString(3, registro.getContactoPrincipal());
            procedimiento.setDate(4, java.sql.Date.valueOf(registro.getFecha()));
            procedimiento.setInt(5, registro.getCodigoProveedor());
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /* Controles */
    
    public void activarControles() {
        txtNumeroDocumento.setEditable(true);
        txtDescripcion.setEditable(true);
        dateFecha.setDisable(false);
        cmbProveedor.setDisable(false);
    }
    
    public void limpiarControles() {
        txtNumeroDocumento.setText("");
        txtDescripcion.setText("");
        dateFecha.setValue(null);
        cmbProveedor.setValue("");
    }
    
    public void desactivarControles() {
        txtNumeroDocumento.setEditable(false);
        txtDescripcion.setEditable(false);
        dateFecha.setEditable(false);
        cmbProveedor.setDisable(true);
        dateFecha.setDisable(true);
    }
    
    /* Funciones de TableView */
    
    public void cargarDatos() {
        tblCompras.setItems(getCompras());
        cmbProveedor.setItems(getProveedores());
        colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<Compra, Integer>("numeroDocumento"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Compra, String>("descripcion"));
        colContactoPrincipal.setCellValueFactory(new PropertyValueFactory<Compra, String>("contactoPrincipal"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Compra, Float>("total"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Compra, LocalDate>("fecha"));
    }
    
    public void seleccionarElemento() {
        txtNumeroDocumento.setText(String.valueOf(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
        txtDescripcion.setText(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getDescripcion());
        cmbProveedor.getSelectionModel().select(ProveedorController.buscarProveedor(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        dateFecha.setValue(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getFecha());
        
    }
    
    public ObservableList<Compra> getCompras() {
        ArrayList<Compra> lista = new ArrayList<Compra>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_ListarCompras }");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Compra(resultado.getInt("numeroDocumento"), resultado.getString("descripcion"), resultado.getFloat("total"), (resultado.getDate("fecha").toLocalDate()), resultado.getInt("codigoProveedor"), resultado.getString("contactoPrincipal"), resultado.getString("razonSocial"), resultado.getString("nit"), resultado.getString("paginaWeb"), resultado.getString("direccion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaCompras = FXCollections.observableArrayList(lista);
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
        
        return listaProveedores = FXCollections.observableArrayList(lista);
    }
    
    /* Reporte */
    
    public void generarReporte() {
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                tipoDeOperacion = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                btnEditar.setText("");
                btnReporte.setText("");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                activarControles();
        }
    }
    
    public void imprimirReporte(){
        HashMap parametros = new HashMap();
        parametros.put("_NumeroDocumento", null);
        GenerarReporte.mostrarReporte("ReporteCompra.jasper", "Reporte de Compras", parametros);
        
        
    }
}
