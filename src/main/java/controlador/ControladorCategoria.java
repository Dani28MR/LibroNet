
package controlador;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import modelo.Categoria;
import static modelo.Modo.VER;
import modelo.OperacionCategoria;

public class ControladorCategoria implements Initializable{
    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField txtNombre;
    
    Connection conexion;
    Statement st;
    ResultSet rs;
    private ControladorMain cMain;
    private OperacionCategoria operacion;
    Categoria categoriaSeleccionado;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @FXML
    void btnAccionAceptar(ActionEvent event) {
        crearNuevaCategoria();
    }

    @FXML
    void btnAccionCancelar(ActionEvent event) {
        cerrarVentana(event);
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
        initDatos();
    }
    public void setOperacion(OperacionCategoria operacion) {
        this.operacion = operacion;
        categoriaSeleccionado = operacion.categoria();
        configurarInterfazSegunModo();
    }
    
    private void configurarInterfazSegunModo() {
        switch (operacion.modo()) {
            case VER -> deshabilitarCampos();
        }
    }
    
    
    private void crearNuevaCategoria() {
        String nombreCat = txtNombre.getText().trim();

        if (nombreCat.isEmpty()) {
            mostrarAlertaError("Error", "El nombre de la categoría no puede estar vacío");
            return;
        }

        try {
            String queryCheck = "SELECT COUNT(*) FROM categoria WHERE nombreCategoria = ?";
            try (PreparedStatement pstCheck = conexion.prepareStatement(queryCheck)) {
                pstCheck.setString(1, nombreCat);
                ResultSet rs = pstCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // 4a. Si existe, informar al usuario
                    mostrarAlertaError(
                        "Categoría duplicada",
                        "No se ha podido crear la categoría porque ya existe una con el nombre \"" + nombreCat + "\"."
                    );
                    return;
                }
            }

            String queryInsert = "INSERT INTO categoria (nombreCategoria) VALUES (?)";
            try (PreparedStatement pstIns = conexion.prepareStatement(queryInsert)) {
                pstIns.setString(1, nombreCat);

                int filas = pstIns.executeUpdate();
                if (filas > 0) {
                    mostrarAlertaExito("Éxito", "Categoría creada correctamente");
                } else {
                    mostrarAlertaError("Error", "No se pudo crear la categoría");
                }
            }

        } catch (SQLException e) {
            mostrarAlertaError("Error de base de datos", e.getMessage());
        }

        cMain.tbvCategorias.setItems(cMain.listaTodasCategorias());
    }

    
    
    private void deshabilitarCampos(){
        rellenarDatos();
        txtNombre.setEditable(false);
        btnAceptar.setVisible(false);
    }
    
    
    
    private void rellenarDatos(){
        txtNombre.setText(categoriaSeleccionado.getNombreCategoria());
    }
    
    
    private void mostrarAlertaExito(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
        
        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void cerrarVentana(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        stage.setResizable(false);
        stage.close();
        cMain.resetearBotonesLibros();
    }
    
}
