
package controlador;

import java.io.File;
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
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelo.Autor;
import static modelo.Modo.EDITAR;
import static modelo.Modo.VER;
import modelo.OperacionAutor;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

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
    File rutaImg;
    private ValidationSupport validationSupport;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @FXML
    void btnAccionAceptar(ActionEvent event) {
        switch (operacion.modo()) {
            case ADD -> {
                crearAutor();
            }
            case EDITAR -> {
                validationSupport.setErrorDecorationEnabled(false); 
                editarAutor();
            }
        }
    }

    @FXML
    void btnAccionCancelar(ActionEvent event) {
        cerrarVentana(event);
    }

    @FXML
    void btnAccionImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
            "Archivos de imagen", 
            "*.png", "*.jpg", "*.jpeg", "*.gif"
        );
        fileChooser.getExtensionFilters().add(extFilter);

        File archivoSeleccionado = fileChooser.showOpenDialog(
            ((Node) event.getSource()).getScene().getWindow()
        );

        if (archivoSeleccionado != null) {
            try {
                rutaImg = archivoSeleccionado;
            
                Image image = new Image(rutaImg.toURI().toString());
                imgAutor.setImage(image);
                imgAutor.setFitWidth(200);
                imgAutor.setFitHeight(150);
                imgAutor.setPreserveRatio(true);
            } catch (Exception e) {
                cMain.mostrarAlertaError("Error de carga", "Por favor, seleccione un archivo de imagen válido (PNG, JPG, JPEG, GIF).");
            }
        }
    }
    
    private void crearAutor() {
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String nacionalidad = txtNacionalidad.getText().trim();
        String biografia = txtBiografia.getText().trim();

        if (rutaImg == null) {
            cMain.mostrarAlertaError("Error", "La imagen es obligatoria");
            return;
        }
        
        String imagen = cMain.convertirImagenA64(rutaImg);

        try {
            String sqlCheck = "SELECT COUNT(*) FROM autor WHERE nombreAutor = ? AND apellidoAutor = ?";
            try (PreparedStatement pstCheck = conexion.prepareStatement(sqlCheck)) {
                pstCheck.setString(1, nombre);
                pstCheck.setString(2, apellidos);

                ResultSet rs = pstCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    cMain.mostrarAlertaError("Autor existente", "Ya existe un autor con ese nombre y apellidos");
                    return;
                }
            }

            String sqlInsert = "INSERT INTO autor (nombreAutor, apellidoAutor, nacionalidad, imagenAutor, biografia) "
                             + "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstInsert = conexion.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                pstInsert.setString(1, nombre);
                pstInsert.setString(2, apellidos);
                pstInsert.setString(3, nacionalidad);
                pstInsert.setString(4, imagen);
                pstInsert.setString(5, biografia);

                int filasAfectadas = pstInsert.executeUpdate();

                if (filasAfectadas > 0) {
                    cMain.mostrarAlertaExito("Éxito", "Autor creado correctamente");
                    Stage stage = (Stage) txtNombre.getScene().getWindow();
                    stage.close();
                    // Actualizar la tabla de autores
                    cMain.tbvAutores.setItems(cMain.listaTodosAutores());
                } else {
                    cMain.mostrarAlertaError("Error", "No se pudo crear el autor");
                }
            }
        } catch (SQLException e) {
            cMain.mostrarAlertaError("Error de base de datos", e.getMessage());
        }
    }
    
    private void editarAutor() {
        try {
            if (autorSeleccionado == null) {
                cMain.mostrarAlertaError("Error", "No hay ningún autor seleccionado para editar");
                return;
            }

            String nombre = txtNombre.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String nacionalidad = txtNacionalidad.getText().trim();
            String biografia = txtBiografia.getText().trim();
            String imagen;

            if (rutaImg != null) {
                // El usuario ha seleccionado una nueva imagen
                imagen = cMain.convertirImagenA64(rutaImg);
            } else {
                // El usuario no ha seleccionado nueva imagen → mantener la existente
                imagen = autorSeleccionado.getImagenAutor();
            }

            // Verificar duplicados excluyendo el autor actual
            String sqlCheck = "SELECT COUNT(*) FROM autor WHERE nombreAutor = ? AND apellidoAutor = ? AND idAutor != ?";
            try (PreparedStatement pstCheck = conexion.prepareStatement(sqlCheck)) {
                pstCheck.setString(1, nombre);
                pstCheck.setString(2, apellidos);
                pstCheck.setInt(3, autorSeleccionado.getIdAutor());

                ResultSet rs = pstCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    cMain.mostrarAlertaError("Autor existente", "Ya existe otro autor con ese nombre y apellidos");
                    return;
                }
            }

            // Actualización en base de datos
            String sqlUpdate = "UPDATE autor SET "
                    + "nombreAutor = ?, "
                    + "apellidoAutor = ?, "
                    + "nacionalidad = ?, "
                    + "imagenAutor = ?, "
                    + "biografia = ? "
                    + "WHERE idAutor = ?";

            try (PreparedStatement pstUpdate = conexion.prepareStatement(sqlUpdate)) {
                pstUpdate.setString(1, nombre);
                pstUpdate.setString(2, apellidos);
                pstUpdate.setString(3, nacionalidad);
                pstUpdate.setString(4, imagen);
                pstUpdate.setString(5, biografia);
                pstUpdate.setInt(6, autorSeleccionado.getIdAutor());

                int filasAfectadas = pstUpdate.executeUpdate();
                if (filasAfectadas > 0) {
                    cMain.mostrarAlertaExito("Éxito", "Autor actualizado correctamente");

                    // Actualizar tabla y cerrar ventana
                    cMain.tbvAutores.setItems(cMain.listaTodosAutores());
                    Stage stage = (Stage) txtNombre.getScene().getWindow();
                    stage.close();
                } else {
                    cMain.mostrarAlertaError("Error", "No se pudo actualizar el autor");
                }
            }
        } catch (SQLException e) {
            cMain.mostrarAlertaError("Error de base de datos", e.getMessage());
        }
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

        validationSupport.registerValidator(txtApellidos, 
            Validator.createEmptyValidator("Los apellidos es obligatorio"));

        validationSupport.registerValidator(txtNacionalidad, 
            Validator.createEmptyValidator("La nacionalidad es obligatoria"));

        
        validationSupport.registerValidator(txtBiografia, 
            Validator.createEmptyValidator("Debe seleccionar una categoría"));
        
        validationSupport.validationResultProperty().addListener((obs, oldResult, newResult) -> {
            btnAceptar.setDisable(newResult.getErrors().size() > 0);
        });
    }
    
    public void setControladorMain(ControladorMain cMain) {
        this.cMain = cMain;
        initDatos();
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
        
        String imagenBase64 = autorSeleccionado.getImagenAutor();
        if (imagenBase64 != null) {
            imgAutor.setImage(cMain.base64ToImage(imagenBase64));
            imgAutor.setFitWidth(200);
            imgAutor.setFitHeight(150);
            imgAutor.setPreserveRatio(true);
        }
        
    }
    
    
    private void cerrarVentana(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        stage.setResizable(false);
        stage.close();
        cMain.resetearBotonesLibros();
    }
    
}
