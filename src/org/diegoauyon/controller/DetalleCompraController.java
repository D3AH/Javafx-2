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
import org.diegoauyon.bean.Compra;
import org.diegoauyon.bean.Proveedor;
import org.diegoauyon.bean.DetalleCompra;
import org.diegoauyon.bean.Producto;
import org.diegoauyon.db.Conexion;
import org.diegoauyon.sistema.Principal;

public class DetalleCompraController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private Compra compra;
    private ObservableList<DetalleCompra> listaDetalleCompras;
    private ObservableList<Producto> listaProducto;

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }
    
    @FXML private TextField txtNumeroDocumento;
    @FXML private TextField txtCostoUnitario;
    @FXML private TextField txtCantidad;
    @FXML private DatePicker dateFecha;
    @FXML private ComboBox cmbProducto;
    @FXML private TableView tblDetalleCompra;
    @FXML private TableColumn colNumeroDocumento;
    @FXML private TableColumn colProducto;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colPrecio;
    @FXML private TableColumn colSubtotal;
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
    
    public void ventanaCompras() {
        escenarioPrincipal.ventanaCompras();
    }

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void setNumero () {
        txtNumeroDocumento.setText(String.valueOf(this.getCompra().getNumeroDocumento()));
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
                dateFecha.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void editar() {
        switch(tipoDeOperacion) {
            case NINGUNO:
                if(tblDetalleCompra.getSelectionModel().getSelectedItem() != null) {
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtCostoUnitario.setEditable(true);
                    txtCantidad.setEditable(true);
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
        DetalleCompra registro = new DetalleCompra();
        registro.setNumeroDocumento(this.getCompra().getNumeroDocumento());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setCostoUnitario(Integer.parseInt(txtCostoUnitario.getText()));
        registro.setCodigoProducto(((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());

        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarDetalleCompra(?,?,?,?) }");
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setInt(2, registro.getCostoUnitario());
            procedimiento.setInt(3, registro.getCodigoProducto());
            procedimiento.setInt(4, registro.getNumeroDocumento());
            procedimiento.execute();
            listaDetalleCompras.add(registro);
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
                if(tblDetalleCompra.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Confirmacion de eliminacion");
                    alert.setContentText("Esta seguro que desea eliminar el registro?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.get() == ButtonType.OK) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarDetalleCompra(?)");                            
                            procedimiento.setInt(1, ((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra());
                            procedimiento.execute();
                            listaDetalleCompras.remove(tblDetalleCompra.getSelectionModel().getSelectedIndex());
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ActualizarDetalleCompra(?,?,?,?,?)");
            DetalleCompra registro = ((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem());
            registro.setNumeroDocumento(((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getNumeroDocumento());
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /* Controles */
    
    public void activarControles() {
        txtNumeroDocumento.setEditable(true);
        txtCostoUnitario.setEditable(true);
        txtCantidad.setEditable(true);
        cmbProducto.setDisable(false);
    }
    
    public void limpiarControles() {
        txtNumeroDocumento.setText("");
        txtCostoUnitario.setText("");
        txtCantidad.setText("");
        cmbProducto.setValue("");
    }
    
    public void desactivarControles() {
        txtNumeroDocumento.setEditable(false);
        txtCostoUnitario.setEditable(false);
        txtCantidad.setEditable(false);
        cmbProducto.setDisable(true);
    }
    
    /* Funciones de TableView */
    
    public void cargarDatos() {
        tblDetalleCompra.setItems(getDetalleCompras());
        cmbProducto.setItems(getProductos());
        colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("numeroDocumento"));
        colProducto.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("codigoProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Float>("costoUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Float>("subtotal"));
    }
    
    public void seleccionarElemento() {
        txtCostoUnitario.setText(String.valueOf(((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
        txtCantidad.setText(String.valueOf(((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCantidad()));
        cmbProducto.getSelectionModel().select(buscarProducto(((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoProducto()));
    }
    
    public ObservableList<DetalleCompra> getDetalleCompras() {
        ArrayList<DetalleCompra> lista = new ArrayList<DetalleCompra>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{ call sp_ListarDetalleCompra }");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleCompra(resultado.getInt("codigoDetallecompra"), resultado.getInt("cantidad"), resultado.getInt("costoUnitario"), resultado.getInt("subtotal"), resultado.getInt("codigoProducto"), resultado.getInt("numeroDocumento")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaDetalleCompras = FXCollections.observableArrayList(lista);
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
