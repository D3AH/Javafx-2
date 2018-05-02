package org.diegoauyon.controller;

import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.swing.JOptionPane;
import org.diegoauyon.bean.Categoria;
import org.diegoauyon.bean.Marca;
import org.diegoauyon.bean.Producto;
import org.diegoauyon.bean.Talla;
import org.diegoauyon.db.Conexion;
import org.diegoauyon.sistema.Principal;

public class ProductoController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Producto> listaProducto;
    private ObservableList<Categoria> listaCategoria;
    private ObservableList<Marca> listaMarca;
    private ObservableList<Talla> listaTalla;
    
    
    @FXML private TextField txtDescripcion;
    
    @FXML private TextField txtImagen;
    
    @FXML private ComboBox cmbCategoria;
    
    @FXML private ComboBox cmbMarca;
    
    @FXML private ComboBox cmbTalla;
    
    @FXML private TableView tblProducto;
    
    @FXML private TableColumn colCodigo;
    
    @FXML private TableColumn colDescripcion;
    
    @FXML private TableColumn colCategoria;
    
    @FXML private TableColumn colMarca;
    
    @FXML private TableColumn colTalla;
    
    @FXML private TableColumn colPrecioUnitario;
    
    @FXML private TableColumn colPrecioDocena;
    
    @FXML private TableColumn colPrecioMayor;
    
    @FXML private TableColumn colExistencia;
    
    @FXML private Button btnNuevo;
    
    @FXML private Button btnEliminar;
    
    @FXML private Button btnEditar;
    
    @FXML private Button btnReporte;
    
    @FXML private Button btnFilePicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCategoria.setItems(getCategorias());
        cmbTalla.setItems(getTallas());
        cmbMarca.setItems(getMarcas());
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    public void cargarDatos() {
        tblProducto.setItems(getProductos());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Producto,Integer>("codigoProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto,String>("descripcion"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<Producto,String>("categoria"));
        colMarca.setCellValueFactory(new PropertyValueFactory<Producto,String>("marca"));
        colTalla.setCellValueFactory(new PropertyValueFactory<Producto,String>("talla"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<Producto,String>("precioUnitario"));
        colPrecioDocena.setCellValueFactory(new PropertyValueFactory<Producto,String>("precioDocena"));
        colPrecioMayor.setCellValueFactory(new PropertyValueFactory<Producto,String>("precioMayor"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<Producto,String>("existencia"));
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
    
    public ObservableList<Categoria> getCategorias() {
        ArrayList<Categoria> lista = new ArrayList<Categoria>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCategorias}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Categoria(resultado.getInt("codigoCategoria"),resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaCategoria = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Marca> getMarcas() {
        ArrayList<Marca> lista = new ArrayList<Marca>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMarcas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Marca(resultado.getInt("codigoMarca"),resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaMarca = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Talla> getTallas() {
        ArrayList<Talla> lista = new ArrayList<Talla>();
        try {
            PreparedStatement procedimiento;
            procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTallas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Talla(resultado.getInt("codigoTalla"),resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaTalla = FXCollections.observableArrayList(lista);
    }
    
    public Producto buscarProducto(int codigoProducto) {
        Producto objetoProducto = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscarProducto(?)");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()) {
                objetoProducto = new Producto(registro.getInt("codigoProducto"),registro.getString("descripcion"),registro.getInt("existencia"),registro.getDouble("precioUnitario"),registro.getDouble("precioUnitario"),registro.getDouble("precioUnitario"),registro.getString("imagen"),registro.getInt("codigoCategoria"), registro.getString("categoria"), registro.getInt("codigoMarca"), registro.getString("marca"), registro.getInt("codigoTalla"), registro.getString("talla"));
            }
        } catch (SQLException e) {
            
        }
        return objetoProducto;
    }
    
    public void agregar() {
        Producto registro = new Producto();
        registro.setDescripcion(txtDescripcion.getText());
        registro.setCodigoCategoria(((Categoria) cmbCategoria.getSelectionModel().getSelectedItem()).getCodigoCategoria());
        registro.setCodigoMarca(((Marca) cmbMarca.getSelectionModel().getSelectedItem()).getCodigoMarca());
        registro.setCodigoTalla(((Talla) cmbTalla.getSelectionModel().getSelectedItem()).getCodigoTalla());
        registro.setImagen(txtImagen.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarProducto(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setInt(2, registro.getCodigoCategoria());
            procedimiento.setInt(3, registro.getCodigoMarca());
            procedimiento.setInt(4, registro.getCodigoTalla());
            procedimiento.setString(5, registro.getImagen());
            procedimiento.execute();
            listaProducto.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarProducto(?,?,?,?,?,?)}");
            Producto registro = ((Producto)tblProducto.getSelectionModel().getSelectedItem());
            registro.setCodigoProducto(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setCodigoCategoria(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCodigoCategoria());
            registro.setCodigoMarca(((Marca) cmbMarca.getSelectionModel().getSelectedItem()).getCodigoMarca());
            registro.setCodigoTalla(((Talla) cmbTalla.getSelectionModel().getSelectedItem()).getCodigoTalla());
            registro.setImagen(txtImagen.getText());
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcion());  
            procedimiento.setInt(3, registro.getCodigoCategoria());
            procedimiento.setInt(4, registro.getCodigoMarca());
            procedimiento.setInt(5, registro.getCodigoTalla());
            procedimiento.setString(6, registro.getImagen());  
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
    /* Control de botones */
    
    public void desactivarControles() {
        txtDescripcion.setEditable(false);
        txtImagen.setEditable(false);
        cmbCategoria.setDisable(true);
        cmbTalla.setDisable(true);
        cmbMarca.setDisable(true);
        btnFilePicker.setDisable(true);
    }
    
    public void activarControles() {
        txtDescripcion.setEditable(true);
        txtImagen.setEditable(true);
        cmbCategoria.setDisable(false);
        cmbTalla.setDisable(false);
        cmbMarca.setDisable(false);
        btnFilePicker.setDisable(false);
    }
    
    public void limpiarControles() {
        txtDescripcion.setText("");
        txtImagen.setText("");
        cmbCategoria.setValue("");
        cmbMarca.setValue("");
        cmbTalla.setValue("");
    }
    
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
                if(tblProducto.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el registro?","Confirmacion de eliminacion",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if( respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProducto(?)}");
                            procedimiento.setInt(1, ((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaCategoria.remove(tblProducto.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una categoria.");
                }
        }
    }
    
    public void editar() {
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblProducto.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDescripcion.setEditable(true);
                    txtImagen.setEditable(true);
                    activarControles();
                } else {
                    JOptionPane.showMessageDialog(null,"Debe seleccionar una categoria.");
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
    
    public void cancelar() {
        switch (tipoDeOperacion){
            case GUARDAR:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                btnFilePicker.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void seleccionarElemento() {
        txtDescripcion.setText(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getDescripcion());
        cmbCategoria.setValue(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCategoria());
        cmbMarca.setValue(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getMarca());
        cmbTalla.setValue(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getTalla());
    }
    
    /* File Chooser */
    
    public void seleccionarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        Window stage = null;
        File file = fileChooser.showOpenDialog(stage);
        txtImagen.setText(file.getAbsolutePath());
    }
    
}