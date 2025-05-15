
package controlador;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.Autor;
import static modelo.Modo.EDITAR;
import static modelo.Modo.VER;
import modelo.OperacionAutor;

public class ControladorAutor implements Initializable{
    
    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnImagen;

    @FXML
    private ImageView imgAutor;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextArea txtBiografia;

    @FXML
    private TextField txtNacionalidad;

    @FXML
    private TextField txtNombre;
    
    Connection conexion;
    Statement st;
    ResultSet rs;
    private ControladorMain cMain;
    private OperacionAutor operacion;
    Autor autorSeleccionado;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @FXML
    void btnAccionAceptar(ActionEvent event) {

    }

    @FXML
    void btnAccionCancelar(ActionEvent event) {
        cerrarVentana(event);
    }

    @FXML
    void btnAccionImagen(ActionEvent event) {

    }
    
    public void initDatos() {
        try {
            conexion = cMain.dameConnection();
            if (conexion != null) {
                this.st = conexion.createStatement();
                
            }
        } catch (SQLException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
        }
    }
    
    public void setControladorMain(ControladorMain cMain) {
        this.cMain = cMain;
        initDatos(); // 🔑 Llama a initDatos() después de asignar cMain
    }
    public void setOperacion(OperacionAutor operacion) {
        this.operacion = operacion;
        autorSeleccionado = operacion.autor();
        configurarInterfazSegunModo();
    }
    
    private void configurarInterfazSegunModo() {
        switch (operacion.modo()) {
            case VER -> deshabilitarCampos();
            case EDITAR -> edicion();
        }
    }
    
    private void deshabilitarCampos(){
        rellenarDatos();
        txtNombre.setEditable(false);
        txtApellidos.setEditable(false);
        txtNacionalidad.setEditable(false);
        txtBiografia.setEditable(false);
        btnImagen.setVisible(false);
        btnAceptar.setVisible(false);
    }
    
    private void edicion(){
        rellenarDatos();
        txtNombre.setEditable(false);
    }
    
    
    private void rellenarDatos(){
        txtNombre.setText(autorSeleccionado.getNombreAutor());
        txtApellidos.setText(autorSeleccionado.getApellidosAutor());
        txtNacionalidad.setText(autorSeleccionado.getNacionalidad());
        txtBiografia.setText(autorSeleccionado.getBiografia());
    }
    
    
    private void cerrarVentana(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        stage.setResizable(false);
        stage.close();
        cMain.resetearBotonesLibros();
    }
    
}
