
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
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import modelo.Editorial;
import static modelo.Modo.ADD;
import static modelo.Modo.EDITAR;
import static modelo.Modo.VER;
import modelo.OperacionEditorial;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class ControladorEditorial implements Initializable {

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;
    
    Connection conexion;
    Statement st;
    ResultSet rs;
    private ControladorMain cMain;
    private OperacionEditorial operacion;
    Editorial editorialSeleccionado;
    private ValidationSupport validationSupport;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @FXML
    void btnAccionAceptar(ActionEvent event) {
        switch (operacion.modo()) {
            case ADD -> {
                crearNuevaEditorial();
            }
            case EDITAR -> {
                validationSupport.setErrorDecorationEnabled(false); 
                editarEditorial();
            }
        }
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
                validarCampos();
                
            }
        } catch (SQLException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
        }
    }
    
    private void validarCampos() {
        validationSupport = new ValidationSupport();

        validationSupport.registerValidator(txtNombre, 
            Validator.createEmptyValidator("El nombre es obligatorio"));

        validationSupport.registerValidator(txtDireccion, 
            Validator.createEmptyValidator("La dirección es obligatori"));

        validationSupport.registerValidator(txtTelefono, true, (Control c, String value) -> {
        boolean isValid = value != null && value.matches("\\d{9}");
            ValidationMessage message = isValid
                ? null
                : ValidationMessage.error(c, "El teléfono es obligatorio y debe tener 9 dígitos (ej: 612345678)");
            return ValidationResult.fromMessages(message);
        });
        
        validationSupport.validationResultProperty().addListener((obs, oldResult, newResult) -> {
            btnAceptar.setDisable(newResult.getErrors().size() > 0);
        });
    }
    
    public void setControladorMain(ControladorMain cMain) {
        this.cMain = cMain;
        initDatos();
    }
    public void setOperacion(OperacionEditorial operacion) {
        this.operacion = operacion;
        editorialSeleccionado = operacion.editorial();
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
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        btnAceptar.setVisible(false);
    }
    
    private void edicion(){
        rellenarDatos();
        txtNombre.setEditable(false);
    }
    
    
    private void rellenarDatos(){
        txtNombre.setText(editorialSeleccionado.getNombreEditorial());
        txtDireccion.setText(editorialSeleccionado.getDireccion());
        txtTelefono.setText(editorialSeleccionado.getTelefono());
    }
    private void crearNuevaEditorial() {
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();

        try {
            String queryCheck = "SELECT COUNT(*) FROM editorial WHERE nombreEditorial = ?";
            try (PreparedStatement pstCheck = conexion.prepareStatement(queryCheck)) {
                pstCheck.setString(1, nombre);
                ResultSet rs = pstCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    mostrarAlertaError(
                        "Ya existe esa editorial",
                        "No se ha creado la nueva editorial porque ya existe una con el nombre \"" + nombre + "\"."
                    );
                    return;
                }
            }

            String queryInsert = "INSERT INTO editorial (nombreEditorial, direccion, telefono) VALUES (?, ?, ?)";
            try (PreparedStatement pstIns = conexion.prepareStatement(queryInsert)) {
                pstIns.setString(1, nombre);
                pstIns.setString(2, direccion);
                pstIns.setString(3, telefono);

                int filas = pstIns.executeUpdate();
                if (filas > 0) {
                    mostrarAlertaExito("Éxito", "Editorial creada correctamente");
                } else {
                    mostrarAlertaError("Error", "No se pudo crear la editorial");
                }
            }

        } catch (SQLException e) {
            mostrarAlertaError("Error de base de datos", e.getMessage());
        }

        cMain.tbvEditoriales.setItems(cMain.listaTodasEditoriales());
    }
    
    private void editarEditorial() {

        String nombre    = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono  = txtTelefono.getText().trim();


        int idEditorial = editorialSeleccionado.getIdEditorial();

        try {
            String queryUpdate =
                "UPDATE editorial " +
                "SET nombreEditorial = ?, direccion = ?, telefono = ? " +
                "WHERE idEditorial = ?";

            try (PreparedStatement pst = conexion.prepareStatement(queryUpdate)) {
                pst.setString(1, nombre);
                pst.setString(2, direccion);
                pst.setString(3, telefono);
                pst.setInt(4, idEditorial);

                int filasAfectadas = pst.executeUpdate();
                if (filasAfectadas > 0) {
                    mostrarAlertaExito("Éxito", "Editorial modificada correctamente.");
                } else {
                    mostrarAlertaError(
                        "Error",
                        "No se pudo encontrar la editorial con ID = " + idEditorial + "."
                    );
                }
            }

        } catch (SQLException e) {
            mostrarAlertaError("Error de base de datos", e.getMessage());
        }

        cMain.tbvEditoriales.setItems(cMain.listaTodasEditoriales());
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
