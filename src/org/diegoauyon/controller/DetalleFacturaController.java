package org.diegoauyon.controller;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
import javafx.stage.WindowEvent;
import org.diegoauyon.bean.Factura;
import org.diegoauyon.bean.Proveedor;
import org.diegoauyon.bean.DetalleFactura;
import org.diegoauyon.bean.Producto;
import org.diegoauyon.bean.Producto;
import org.diegoauyon.db.Conexion;
import org.diegoauyon.sistema.Principal;

public class DetalleFacturaController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private Factura compra;
    private ObservableList<DetalleFactura> listaDetalleFacturas;
    private ObservableList<Producto> listaProducto;

    public Factura getFactura() {
        return compra;
    }

    public void setFactura(Factura compra) {
        this.compra = compra;
    }
    
    @FXML private TextField txtNumeroFactura;
    @FXML private TextField txtCantidad;
    @FXML private ComboBox cmbProducto;
    @FXML private TableView tblDetalleFactura;
    @FXML private TableColumn colNumeroFactura;
    @FXML private TableColumn colProducto;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colPrecio;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private Button btnAtras;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaFacturas() {
        escenarioPrincipal.ventanaFacturas();
    }

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void setNumero () {
        txtNumeroFactura.setText(String.valueOf(this.getFactura().getNumeroFactura()));
    }
    /* Botones */
    
    public void nuevo() {
        switch(tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                agregar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
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
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void editar() {
        switch(tipoDeOperacion) {
            case NINGUNO:
                if(tblDetalleFactura.getSelectionModel().getSelectedItem() != null) {
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtCantidad.setEditable(true);
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
        DetalleFactura registro = new DetalleFactura();
        registro.setNumeroFactura(this.getFactura().getNumeroFactura());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setCodigoProducto(((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());

        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarDetalleFactura(?,?,?) }");
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setInt(2, registro.getCodigoProducto());
            procedimiento.setInt(3, registro.getNumeroFactura());
            procedimiento.execute();
            listaDetalleFacturas.add(registro);
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
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblDetalleFactura.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Confirmacion de eliminacion");
                    alert.setContentText("Esta seguro que desea eliminar el registro?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.get() == ButtonType.OK) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarDetalleFactura(?)");                            
                            procedimiento.setInt(1, ((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura());
                            procedimiento.execute();
                            listaDetalleFacturas.remove(tblDetalleFactura.getSelectionModel().getSelectedIndex());
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ActualizarDetalleFactura(?,?,?,?,?)");
            DetalleFactura registro = ((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem());
            registro.setNumeroFactura(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
            procedimiento.setInt(1, registro.getNumeroFactura());
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /* Controles */
    
    public void activarControles() {
        txtNumeroFactura.setEditable(true);
        txtCantidad.setEditable(true);
        cmbProducto.setDisable(false);
    }
    
    public void limpiarControles() {
        txtNumeroFactura.setText("");
        txtCantidad.setText("");
        cmbProducto.setValue("");
    }
    
    public void desactivarControles() {
        txtNumeroFactura.setEditable(false);
        txtCantidad.setEditable(false);
        cmbProducto.setDisable(true);
    }
    
    /* Funciones de TableView */
    
    public void cargarDatos() {
        tblDetalleFactura.setItems(getDetalleFacturas());
        cmbProducto.setItems(getProductos());
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("numeroFactura"));
        colProducto.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("codigoProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Float>("costoUnitario"));
    }
    
    public void seleccionarElemento() {
        txtCantidad.setText(String.valueOf(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCantidad()));
        cmbProducto.getSelectionModel().select(buscarProducto(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoProducto()));
    }
    
    public ObservableList<DetalleFactura> getDetalleFacturas() {
        ArrayList<DetalleFactura> lista = new ArrayList<DetalleFactura>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_ListarDetalleFacturas }");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleFactura(resultado.getInt("codigoDetalleFactura"), resultado.getInt("cantidad"), resultado.getInt("precio"), resultado.getInt("codigoProducto"), resultado.getInt("numeroFactura")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaDetalleFacturas = FXCollections.observableArrayList(lista);
    }
    
    public Producto buscarProducto(int codigoProducto) {
        Producto objetoProducto = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_BuscarProducto(?) }");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()) {
                objetoProducto = new Producto(registro.getInt("codigoProducto"),registro.getString("descripcion"),registro.getInt("existencia"),registro.getDouble("precioUnitario"),registro.getDouble("precioUnitario"),registro.getDouble("precioUnitario"),registro.getString("imagen"),registro.getInt("codigoCategoria"), registro.getString("categoria"), registro.getInt("codigoMarca"), registro.getString("marca"), registro.getInt("codigoTalla"), registro.getString("talla"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objetoProducto;
    }
    
    public ObservableList<Producto> getProductos() {
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto(resultado.getInt("codigoProducto"),resultado.getString("descripcion"),resultado.getInt("existencia"),resultado.getDouble("precioUnitario"),resultado.getDouble("precioUnitario"),resultado.getDouble("precioUnitario"),resultado.getString("imagen"),resultado.getInt("codigoCategoria"), resultado.getString("categoria"), resultado.getInt("codigoMarca"), resultado.getString("marca"), resultado.getInt("codigoTalla"), resultado.getString("talla")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaProducto = FXCollections.observableArrayList(lista);
    }
}
