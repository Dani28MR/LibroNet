
package controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.ConexionSingleton;
import modelo.RolUsuario;
import modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;

public class ControladorLogin implements Initializable{
    @FXML
    private Button btnCrearCuenta;

    @FXML
    private Button btnEntrar;

    @FXML
    private ImageView imgLogo;

    @FXML
    private ImageView imgPassword;

    @FXML
    private ImageView imgUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsuario;

    Connection conexion;
    Statement st;
    ResultSet rs;
    ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
    Usuario usuarioLogin;
    private ControladorMain cMain;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            conexion = ConexionSingleton.obtenerConexion();
            if (conexion != null) {
                this.st = conexion.createStatement();
                cargarImg();
                
            }
        } catch (SQLException e) {
            System.out.println("Error en la conexión");
        }
        
        String password = "admin";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(hashedPassword);
        
        String pass = "123";
        String has = BCrypt.hashpw(pass, BCrypt.gensalt());
        System.out.println(has);
        obtenerListaUsuarios();
        System.out.println(listaUsuarios.size());
        
    }
    
    
    @FXML
    void btnAccionCrearCuenta(ActionEvent event) throws IOException {
        
        FXMLLoader loader = new FXMLLoader(ControladorMain.class.getResource("/vista/designerUsuario.fxml"));
        Parent root = loader.load();
            
        Stage stage = new Stage();
        stage.setTitle("LibroNet");
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    void btnAccionEntrar(ActionEvent event){
        String email = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();
        
        if (!consultarUsuario(email)) {
            System.out.println("Usuario no encontrado");
            return;
        }else{
            System.out.println("usuario encontrado y se llama: "+usuarioLogin.getNombreUsuario());
            System.out.println(""+BCrypt.checkpw(password, usuarioLogin.getContraseña()));
            //abrirVentanaVenta();
        }
        try {
            // 2) Cargar FXML de la ventana principal
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vista/designerMain.fxml")
            );
            Parent root = loader.load();

            // 3) Obtener su controlador y pasarle el usuario
            cMain = loader.getController();
            cMain.recibirUsuarioLogin(usuarioLogin);

            // 4) Cerrar la ventana de login
            Stage loginStage = (Stage) btnEntrar.getScene().getWindow();
            loginStage.close();

            // 5) Mostrar la ventana principal
            Stage mainStage = new Stage();
            mainStage.setTitle("LibroNet");
            mainStage.getIcons().add(new Image(
                getClass().getClassLoader().getResourceAsStream("icono.png")
            ));
            mainStage.setScene(new Scene(root));
            mainStage.setResizable(false);
            mainStage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar ventana principal: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void cargarImg(){
        imgLogo.setImage(new Image(getClass().getClassLoader().getResourceAsStream("icono_SinFondo.png")));
        imgUsuario.setImage(new Image(getClass().getClassLoader().getResourceAsStream("logo_Persona.png")));
        imgPassword.setImage(new Image(getClass().getClassLoader().getResourceAsStream("logo_Candado.png")));
    }
    
    private ObservableList obtenerListaUsuarios(){
        if (conexion != null) {
            listaUsuarios.clear();
            String query = "SELECT * FROM usuario";
            try {
                rs = st.executeQuery(query);
                Usuario user;
                while (rs.next()) { 
                    user = new Usuario(
                            rs.getInt("idUsuario"), 
                            rs.getString("nombreUsuario"),
                            rs.getString("apellidoUsuario"),
                            rs.getString("imagenUsuario"), 
                            rs.getString("email"),
                            rs.getString("contraseña"),
                            rs.getString("telefono"),
                            rs.getString("direccion"),
                            RolUsuario.valueOf(rs.getString("rol")));
                    listaUsuarios.add(user);
                }
            } catch (SQLException e) {
                System.out.println("Excepción SQL: "+e.getMessage());
            }
            return listaUsuarios;
        }
        return null;
    }
    
    private boolean consultarUsuario(String email){
        if (conexion == null) {
            System.out.println("No hay conexión a la base de datos");
            return false;
        }
        
        String query = "SELECT * "
                + "FROM usuario "
                + "WHERE email = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuarioLogin = new Usuario(
                        rs.getInt("idUsuario"), 
                        rs.getString("nombreUsuario"),
                        rs.getString("apellidoUsuario"),
                        rs.getString("imagenUsuario"), 
                        rs.getString("email"),
                        rs.getString("contraseña"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        RolUsuario.valueOf(rs.getString("rol")));
                    return true;
                }

            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuario: " + e.getMessage());
        }
        
        //usuarioLogin = null;
        return false;
    }
    
    public void abrirVentanaMain() {
        try {
            FXMLLoader loader = new FXMLLoader(ControladorMain.class.getResource("/vista/designerMain.fxml"));
            Parent root = loader.load();
            cMain = loader.getController();

            
            cMain.setUsuarioLog(usuarioLogin);

            
            Stage escenarioActual = (Stage) txtUsuario.getScene().getWindow();
            escenarioActual.close();

            Stage stage = new Stage();
            stage.setTitle("LibroNet");
            stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar el archivo FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
