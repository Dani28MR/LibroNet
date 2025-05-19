
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelo.ConexionSingleton;
import static modelo.Modo.ADD;
import static modelo.Modo.EDITAR;
import static modelo.Modo.VER;
import modelo.OperacionUsuario;
import modelo.RolUsuario;
import modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;

public class ControladorUsuario implements Initializable{
    
    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnImagen;

    @FXML
    private ImageView imgUsuario;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNombre;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtTelefono;
    
    @FXML
    private PasswordField txtPasswordConfirmacion;
    
    
    
    Connection conexion;
    Statement st;
    ResultSet rs;
    private ControladorMain cMain;
    private ControladorLogin cLogin;
    private OperacionUsuario operacion;
    Usuario usuarioSeleccionado;
    File rutaImg;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @FXML
    void btnAccionAceptar(ActionEvent event) {
        switch (operacion.modo()) {
            case ADD -> {
                crearNuevoUsuario();
            }
            case EDITAR -> {
                editarUsuario();
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

        // 5. Guardar la ruta solo si se seleccionó un archivo
        if (archivoSeleccionado != null) {
            try {
                rutaImg = archivoSeleccionado;
            
                Image image = new Image(rutaImg.toURI().toString());
                imgUsuario.setImage(image);
                imgUsuario.setFitWidth(200);
                imgUsuario.setFitHeight(150);
                imgUsuario.setPreserveRatio(true);
            } catch (Exception e) {
                mostrarAlertaError("Error de carga", "Por favor, seleccione un archivo de imagen válido (PNG, JPG, JPEG, GIF).");
            }
        }
    }
    
    private void crearNuevoUsuario() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellidos.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String confirmacionPassword = txtPasswordConfirmacion.getText();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();

        
        
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || rutaImg == null) {
            mostrarAlertaError("Error", "Nombre, apellido, email, contraseña y la imágen del usuario son campos obligatorios");
            return;
        }
        
        String imagen = rutaImg.getAbsolutePath();

        if (password.length() < 8) {
            mostrarAlertaError("Contraseña débil", "La contraseña debe tener al menos 8 caracteres");
            return;
        }

        if (!password.equals(confirmacionPassword)) {
            mostrarAlertaError("Error", "Las contraseñas no coinciden");
            return;
        }

        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            mostrarAlertaError("Email inválido", "Formato de email incorrecto");
            return;
        }

        try {
            String queryCheck = "SELECT COUNT(*) FROM usuario WHERE nombreUsuario = ? AND apellidoUsuario = ?";
            try (PreparedStatement pstCheck = conexion.prepareStatement(queryCheck)) {
                pstCheck.setString(1, nombre);
                pstCheck.setString(2, apellido);

                ResultSet rs = pstCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    mostrarAlertaError(
                        "Usuario existente",
                        "Ya existe un usuario con el nombre '" + nombre + " " + apellido + "'"
                    );
                    return;
                }
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            String queryInsert = "INSERT INTO usuario (nombreUsuario, apellidoUsuario, imagenUsuario, "
                               + "email, contraseña, cofirmacionContraseña, telefono, direccion, rol) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstIns = conexion.prepareStatement(queryInsert)) {
                pstIns.setString(1, nombre);
                pstIns.setString(2, apellido);
                pstIns.setString(3, cMain.convertirImagenA64(rutaImg));
                pstIns.setString(4, email);
                pstIns.setString(5, hashedPassword);
                pstIns.setString(6, hashedPassword);
                pstIns.setString(7, telefono);
                pstIns.setString(8, direccion);
                pstIns.setString(9, RolUsuario.LECTOR.name());

                int filas = pstIns.executeUpdate();
                if (filas > 0) {
                    mostrarAlertaExito("Éxito", "Usuario creado correctamente");
                } else {
                    mostrarAlertaError("Error", "No se pudo crear el usuario");
                }
            }

        } catch (SQLException e) {
            mostrarAlertaError("Error de base de datos", e.getMessage());
        }

        cMain.tbvUsuarios.setItems(cMain.listaTodosUsuarios());
    }
    
    private void editarUsuario() {
        try {
            if (usuarioSeleccionado == null) {
                mostrarAlertaError("Error", "No hay ningún usuario seleccionado para editar");
                return;
            }

            int idUsuario = usuarioSeleccionado.getIdUsuario();
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellidos.getText().trim();
            String email = txtEmail.getText().trim();
            String nuevoPassword = txtPassword.getText();
            String confirmacionPassword = txtPasswordConfirmacion.getText();
            String telefono = txtTelefono.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String rol = usuarioSeleccionado.getRol().toString();
            String imagenBase64 = usuarioSeleccionado.getImagenUsuario();

            // Validación de campos obligatorios
            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty()) {
                mostrarAlertaError("Error", "Nombre, apellido y email son campos obligatorios");
                return;
            }

            // Validación formato email
            if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                mostrarAlertaError("Email inválido", "Formato de email incorrecto");
                return;
            }

            // Verificar si el email ya existe en otro usuario
            if (!email.equals(usuarioSeleccionado.getEmail())) {
                String sqlCheckEmail = "SELECT COUNT(*) FROM usuario WHERE email = ? AND idUsuario != ?";
                try (PreparedStatement pst = conexion.prepareStatement(sqlCheckEmail)) {
                    pst.setString(1, email);
                    pst.setInt(2, idUsuario);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        mostrarAlertaError("Email en uso", "Este email ya está registrado por otro usuario");
                        return;
                    }
                }
            }

            // Manejo de contraseña
            String hashedPassword = usuarioSeleccionado.getContraseña();
            if (!nuevoPassword.isEmpty()) {
                if (nuevoPassword.length() < 8) {
                    mostrarAlertaError("Contraseña débil", "La contraseña debe tener al menos 8 caracteres");
                    return;
                }

                if (!nuevoPassword.equals(confirmacionPassword)) {
                    mostrarAlertaError("Error", "Las contraseñas no coinciden");
                    return;
                }

                hashedPassword = BCrypt.hashpw(nuevoPassword, BCrypt.gensalt());
            }

            // Manejo de imagen
            if (rutaImg != null) {
                imagenBase64 = cMain.convertirImagenA64(rutaImg);
            }

            // Actualización en base de datos
            String sqlUpdate = "UPDATE usuario SET "
                    + "nombreUsuario = ?, "
                    + "apellidoUsuario = ?, "
                    + "imagenUsuario = ?, "
                    + "email = ?, "
                    + "contraseña = ?, "
                    + "telefono = ?, "
                    + "direccion = ?, "
                    + "rol = ? "
                    + "WHERE idUsuario = ?";

            try (PreparedStatement pst = conexion.prepareStatement(sqlUpdate)) {
                pst.setString(1, nombre);
                pst.setString(2, apellido);
                pst.setString(3, imagenBase64);
                pst.setString(4, email);
                pst.setString(5, hashedPassword);
                pst.setString(6, telefono.isEmpty() ? null : telefono);
                pst.setString(7, direccion.isEmpty() ? null : direccion);
                pst.setString(8, rol);
                pst.setInt(9, idUsuario);

                int filasAfectadas = pst.executeUpdate();
                if (filasAfectadas > 0) {
                    mostrarAlertaExito("Éxito", "Usuario actualizado correctamente");
                    cMain.tbvUsuarios.setItems(cMain.listaTodosUsuarios());
                } else {
                    mostrarAlertaError("Error", "No se pudo actualizar el usuario");
                }
            }

        } catch (SQLException e) {
            mostrarAlertaError("Error de base de datos", e.getMessage());
        }
    }   
    
    public void initDatos() {
        try {
            conexion = ConexionSingleton.obtenerConexion();
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
    
    public void setControladorLogin(ControladorLogin cLogin) {
        this.cLogin = cLogin;
        initDatos();
    }
    public void setOperacion(OperacionUsuario operacion) {
        this.operacion = operacion;
        usuarioSeleccionado = operacion.usuario();
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
        txtPassword.setEditable(false);
        txtPasswordConfirmacion.setEditable(false);
        btnAceptar.setVisible(false);
        btnImagen.setVisible(false);
    }
    
    private void edicion(){
        rellenarDatos();
        txtNombre.setEditable(false);
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
    
    private void rellenarDatos(){
        txtNombre.setText(usuarioSeleccionado.getNombreUsuario());
        txtApellidos.setText(usuarioSeleccionado.getApellidoUsuario());
        txtEmail.setText(usuarioSeleccionado.getEmail());
        txtTelefono.setText(usuarioSeleccionado.getTelefono());
        txtDireccion.setText(usuarioSeleccionado.getDireccion());
        txtPassword.setText(usuarioSeleccionado.getContraseña());
        txtPasswordConfirmacion.setText(usuarioSeleccionado.getCofirmacionContraseña());
        
        String imagenBase64 = usuarioSeleccionado.getImagenUsuario();
        if (imagenBase64 != null) {
            imgUsuario.setImage(cMain.base64ToImage(imagenBase64));
            imgUsuario.setFitWidth(200);
            imgUsuario.setFitHeight(150);
            imgUsuario.setPreserveRatio(true);
        }
    }
    
    private void cerrarVentana(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        stage.close();
    }
}
