
package controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import modelo.Autor;
import modelo.Categoria;
import modelo.ConexionSingleton;
import modelo.Editorial;
import modelo.Libro;
import modelo.Modo;
import modelo.OperacionAutor;
import modelo.OperacionCategoria;
import modelo.OperacionEditorial;
import modelo.OperacionLibro;
import modelo.OperacionUsuario;
import modelo.RolUsuario;
import modelo.Usuario;

public class ControladorMain implements Initializable{

    @FXML
    public Button btnAddAutores;

    @FXML
    public Button btnAddCategorias;

    @FXML
    public Button btnAddEditoriales;

    @FXML
    public Button btnAddLibros;

    @FXML
    private Button btnAddUsuarios;

    @FXML
    public Button btnBorrarAutores;

    @FXML
    public Button btnBorrarCategorias;

    @FXML
    public Button btnBorrarEditoriales;

    @FXML
    public Button btnBorrarLibros;

    @FXML
    public Button btnBorrarUsuarios;

    @FXML
    public Button btnEditarAutores;

    @FXML
    public Button btnEditarEditoriales;

    @FXML
    public Button btnEditarLibros;

    @FXML
    public Button btnEditarUsuarios;

    @FXML
    private Button btnReservarLibros;

    @FXML
    private Button btnSalir;
    
    @FXML
    private Button btnCancelarReserva;

    @FXML
    public Button btnVerAutores;

    @FXML
    public Button btnVerCategorias;

    @FXML
    public Button btnVerEditoriales;

    @FXML
    public Button btnVerLibros;

    @FXML
    public Button btnVerUsuarios;

    @FXML
    private TableColumn<Autor, String> colApellidoAutores;

    @FXML
    private TableColumn<Libro, Integer> colCopiasDisponiblesLibros;

    @FXML
    private TableColumn<Editorial, String> colDireccionEditoriales;

    @FXML
    private TableColumn<Libro, String> colISBNLibros;

    @FXML
    private TableColumn<Categoria, Integer> colIdCategorias;

    @FXML
    private TableColumn<Editorial, Integer> colIdEditoriales;

    @FXML
    private TableColumn<Autor, String> colNacionalidadAutores;

    @FXML
    private TableColumn<Autor, String> colNombreAutores;

    @FXML
    private TableColumn<Categoria, String> colNombreCategorias;

    @FXML
    private TableColumn<Editorial, String> colNombreEditoriales;

    @FXML
    private TableColumn<Editorial, String> colTelefonoEditoriales;

    @FXML
    private TableColumn<Libro, String> colTituloLibros;

    @FXML
    private TableColumn<Libro, Integer> colTotalCopiasLibros;

    @FXML
    private ImageView imgAddAutor;

    @FXML
    private ImageView imgAddCategoria;

    @FXML
    private ImageView imgAddEditorial;

    @FXML
    private ImageView imgAddLibro;

    @FXML
    private ImageView imgAddUsuario;

    @FXML
    private ImageView imgBorrarAutor;

    @FXML
    private ImageView imgBorrarCategoria;

    @FXML
    private ImageView imgBorrarEditorial;

    @FXML
    private ImageView imgBorrarLibro;

    @FXML
    private ImageView imgBorrarUsuario;

    @FXML
    private ImageView imgEditarAutor;


    @FXML
    private ImageView imgEditarEditorial;

    @FXML
    private ImageView imgEditarLibro;

    @FXML
    private ImageView imgEditarUsuario;

    @FXML
    private ImageView imgLupaAutor;

    @FXML
    private ImageView imgLupaCategoria;

    @FXML
    private ImageView imgLupaEditorial;

    @FXML
    private ImageView imgLupaLibro;

    @FXML
    private ImageView imgLupaUsuario;

    @FXML
    private ImageView imgSalir;

    @FXML
    public ImageView imgUsuario;

    @FXML
    private ImageView imgVerAutor;

    @FXML
    private ImageView imgVerCategoria;

    @FXML
    private ImageView imgVerEditorial;

    @FXML
    private ImageView imgVerLibro;

    @FXML
    private ImageView imgVerUsuario;

    @FXML
    private RadioButton opcDisponibles;

    @FXML
    private RadioButton opcReservas;

    @FXML
    private RadioButton opcTodosLibros;

    @FXML
    private Tab tabUsuarios;
    
    @FXML
    private TableColumn<Usuario, String> colNombreUsuarios;
    
    @FXML
    private TableColumn<Usuario, String> colApellidosUsuarios;
    
    @FXML
    private TableColumn<Usuario, String> colEmailUsuarios;
    
    @FXML
    private TableColumn<Usuario, String> colTelefonoUsuarios;
    
    @FXML
    private TableColumn<Usuario, String> colDireccionUsuarios;

    @FXML
    public TableView<Autor> tbvAutores;

    @FXML
    public TableView<Categoria> tbvCategorias;

    @FXML
    public TableView<Editorial> tbvEditoriales;

    @FXML
    public TableView<Libro> tbvLibros;

    @FXML
    public TableView<Usuario> tbvUsuarios;

    @FXML
    private Label txtEmailUsuario;

    @FXML
    private TextField txtFldAutor;

    @FXML
    private TextField txtFldCategoria;

    @FXML
    private TextField txtFldEditorial;

    @FXML
    private TextField txtFldLibro;

    @FXML
    private TextField txtFldUsuario;

    @FXML
    private Label txtNombreUsuario;
    
    
    @FXML
    private Tab tabReservas;
    
    @FXML
    private TextField txtFldUsuarioReserva;
    @FXML
    private TextField txtFldLibroReserva;
    
    @FXML
    public TableView<Usuario> tbvUsuarioReserva;
    
    @FXML
    public TableView<Libro> tbvLibrosReserva;
    
    @FXML
    private TableColumn<Usuario, String> colNombreUsuarioReserva;
    @FXML
    private TableColumn<Usuario, String> colApellidoUsuarioReserva;
    @FXML
    private TableColumn<Usuario, String> colTelefonoUsuarioReserva;
    @FXML
    private TableColumn<Usuario, String> colDireccionUsuarioReserva;
    
    @FXML
    private TableColumn<Libro, String> colISBNLibroReserva;
    @FXML
    private TableColumn<Libro, String> colTituloLibroReserva;
    @FXML
    private TableColumn<Libro, String> colCopiasLibroReserva;
    @FXML
    private TableColumn<Libro, String> colDescripcionLibroReserva;
    
    @FXML
    private Button btnReservarAdmin;
    
    @FXML
    private Button btnCancelarReservarAdmin;
    
    
    Connection conexion;
    Statement st;
    ResultSet rs;
    
    private ControladorLogin cLogin;
    private Usuario usuarioLog;
    private ToggleGroup tg;
    
    private Libro libroSeleccionado;
    private Autor autorSeleccionado;
    private Editorial editorialSeleccionado;
    private Categoria categoriaSeleccionado;
    private Usuario usuarioSeleccionado;
    private Usuario usuarioSeleccionadoReserva;
    private Libro libroSeleccionadoReserva;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        try {
            conexion = ConexionSingleton.obtenerConexion();
            if (conexion != null) {
                st = conexion.createStatement();
                procesarReservasExpiradas();
                eliminarReservasMuyAntiguas();
                configurarFiltros();
                Platform.runLater(() -> {
                    String css = getClass().getResource("/styleMain.css").toExternalForm();
                    btnSalir.getScene().getStylesheets().add(css);
                });
            }

            inicializarImagenes();
            relacionarColumnas();
            
            tg = new ToggleGroup();
            opcTodosLibros.setToggleGroup(tg);
            opcDisponibles .setToggleGroup(tg);
            opcReservas    .setToggleGroup(tg);

            tbvLibros.setItems(listaTodosLibros());
            tbvAutores.setItems(listaTodosAutores());
            tbvEditoriales.setItems(listaTodasEditoriales());
            tbvCategorias.setItems(listaTodasCategorias());
            tbvUsuarios.setItems(listaTodosUsuarios());
            
            tbvLibrosReserva.setItems(listaTodosLibros());
            tbvUsuarioReserva.setItems(listaTodosUsuarios());

            tg.selectedToggleProperty().addListener((obs, oldT, newT) -> {
                if (newT == null) return;
                resetearBotonesLibros(); // Aquí sí
                if (opcTodosLibros.isSelected()) {
                    tbvLibros.setItems(listaTodosLibros());
                } else if (opcDisponibles.isSelected()) {
                    tbvLibros.setItems(listaLibrosDisponibles());
                } else {
                    tbvLibros.setItems(listaLibrosReservados());
                }
            });

            // TABLA LIBROS
            
            tbvLibros.setOnMouseClicked((MouseEvent ev) -> {
                if (ev.getClickCount() == 2) {
                    Libro sel = tbvLibros.getSelectionModel().getSelectedItem();
                    if (sel != null) {
                        libroSeleccionado = sel;
                        btnVerLibros.setDisable(false);

                        // Todos pueden reservar
                        btnReservarLibros.setDisable(false);

                        // Comprobar si ya tiene una reserva activa
                        boolean tiene = false;
                        String sql = "SELECT COUNT(*) FROM reserva "
                                   + "WHERE idUsuario=? AND idLibro=? AND estado='ACTIVO'";
                        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                            ps.setInt(1, usuarioLog.getIdUsuario());
                            ps.setInt(2, sel.getIdLibro());
                            ResultSet rs = ps.executeQuery();
                            if (rs.next() && rs.getInt(1) > 0) {
                                tiene = true;
                            }
                        } catch (SQLException e) {
                            System.err.println("Error comprobando reserva: " + e.getMessage());
                        }
                        btnCancelarReserva.setDisable(!tiene);

                        // Configurar botones según el rol
                        if (usuarioLog != null && usuarioLog.getRol() == RolUsuario.ADMINISTRADOR) {
                            btnEditarLibros.setDisable(false);
                            btnBorrarLibros.setDisable(false);
                        } else {
                            btnEditarLibros.setDisable(true);
                            btnBorrarLibros.setDisable(true);
                        }
                    }
                }
            });

            
            // TABLA AUTORES
            tbvAutores.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Autor selected = tbvAutores.getSelectionModel().getSelectedItem();
                    if (selected != null && usuarioLog != null) {
                        autorSeleccionado = selected;
                        confBotonesAutores();
                    }
                }
            });

            // TABLA CATEGORIAS
            tbvCategorias.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Categoria selected = tbvCategorias.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        categoriaSeleccionado = selected;
                        confBotonesCategorias();
                    }
                }
            });
            
            // TABLA EDITORIALES
            tbvEditoriales.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Editorial selected = tbvEditoriales.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        editorialSeleccionado = selected;
                        confBotonesEditoriales();
                    }
                }
            });
            
            // TABLA USUARIOS
            tbvUsuarios.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Usuario selected = tbvUsuarios.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        usuarioSeleccionado = selected;
                        confBotonesUsuario();
                    }
                }
            });
            
            //Configuracion botones de add
            Platform.runLater(() -> {
                    if(usuarioLog != null && usuarioLog.getRol() == RolUsuario.ADMINISTRADOR) {
                        btnAddLibros.setDisable(false);
                        btnAddAutores.setDisable(false);
                        btnAddCategorias.setDisable(false);
                        btnAddEditoriales.setDisable(false);
                    } else {
                        btnAddLibros.setDisable(true);
                        btnAddAutores.setDisable(true);
                        btnAddCategorias.setDisable(true);
                        btnAddEditoriales.setDisable(true);
                    }
            });
            
            
            tbvUsuarioReserva.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                usuarioSeleccionadoReserva = newSel;
                verificarSeleccion();
            });
            tbvLibrosReserva.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                libroSeleccionadoReserva = newSel;
                verificarSeleccion();
            });

        } catch (SQLException ex) {
            System.err.println("Error en initialize: " + ex.getMessage());
        }
    }
    
    private void verificarSeleccion() {
    boolean usuarioYLibroSeleccionados = usuarioSeleccionadoReserva != null && libroSeleccionadoReserva != null;
        if (usuarioYLibroSeleccionados) {
            boolean existeReservaActiva = existeReservaActiva(usuarioSeleccionadoReserva.getIdUsuario(),
                                                              libroSeleccionadoReserva.getIdLibro());

            btnReservarAdmin.setDisable(!usuarioYLibroSeleccionados);
            btnCancelarReservarAdmin.setDisable(!existeReservaActiva);
        } else {
            btnReservarAdmin.setDisable(true);
            btnCancelarReservarAdmin.setDisable(true);
        }
    }
    private boolean existeReservaActiva(int idUsuario, int idLibro) {
        String sql = "SELECT COUNT(*) FROM reserva WHERE idUsuario = ? AND idLibro = ? AND estado = 'ACTIVO'";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idLibro);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            mostrarAlertaError("Error", "No se pudo comprobar si existe una reserva");
        }
        return false;
    }

       
    /*
        BOTONES APARTADO LIBRO
                                    */
    
    
    @FXML
    void btnAccionEditarLibros(ActionEvent event) throws IOException {        
        abrirVentanaLibro(new OperacionLibro(Modo.EDITAR, libroSeleccionado),"Editar");
    }
    
    @FXML
    void btnAccionVerLibros(ActionEvent event) throws IOException {        
        abrirVentanaLibro(new OperacionLibro(Modo.VER, libroSeleccionado),"Ver");
    }

    @FXML
    void btnAccionAddLibros(ActionEvent event) throws IOException {
        abrirVentanaLibro(new OperacionLibro(Modo.ADD, null),"Añadir");
    }
    
    @FXML
    void btnAccionBorrarLibros(ActionEvent event) {
        eliminarLibro();
    }
    
    public void abrirVentanaLibro(OperacionLibro operacion, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/designerLibro.fxml"));
        Parent root = loader.load();

        ControladorLibro controladorLibro = loader.getController();
        controladorLibro.setControladorMain(this);
        controladorLibro.setOperacion(operacion);
        
        Stage stage = new Stage();
        stage.setTitle(titulo + " libro");
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    
    
    private void eliminarLibro() {
        try {
            if (libroSeleccionado == null) {
                mostrarAlertaError("Error", "No hay ningún libro seleccionado para eliminar");
                return;
            }

            int idLibro = libroSeleccionado.getIdLibro();

            if (tieneReservasActivas(idLibro)) {
                mostrarAlertaError("No permitido", "Este libro no puede ser eliminado porque tiene reservas ACTIVAS asociadas.");
                return;
            }

            if (tieneReservasInactivas(idLibro)) {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                DialogPane dialogPane = alerta.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
                dialogPane.getStyleClass().add("confirmacion");
                
                Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
                
                alerta.setTitle("Reservas Inactivas Encontradas");
                alerta.setHeaderText("Este libro tiene reservas INACTIVAS asociadas.");
                alerta.setContentText("¿Deseas eliminar también estas reservas inactivas y luego el libro?");
                Optional<ButtonType> respuesta = alerta.showAndWait();

                if (!(respuesta.isPresent() && respuesta.get() == ButtonType.OK)) {
                    return;
                }

                eliminarReservasInactivas(idLibro);
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = confirmacion.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
            dialogPane.getStyleClass().add("confirmacion");
            
                Stage alertStage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar el libro?");
            confirmacion.setContentText("Esta acción no se puede deshacer.");
            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                String query = "DELETE FROM libro WHERE idLibro = ?";
                try (PreparedStatement pst = conexion.prepareStatement(query)) {
                    pst.setInt(1, idLibro);

                    int filasAfectadas = pst.executeUpdate();
                    if (filasAfectadas > 0) {
                        mostrarAlertaExito("Éxito", "Libro eliminado correctamente");
                        tbvLibros.setItems(listaTodosLibros());

                        tbvLibros.getSelectionModel().clearSelection();
                        tbvLibros.getFocusModel().focus(null);
                        btnBorrarLibros.setDisable(true);
                        btnEditarLibros.setDisable(true);
                        btnVerLibros.setDisable(true);
                        btnReservarLibros.setDisable(true);

                        tbvLibrosReserva.setItems(listaLibrosDisponibles());
                    } else {
                        mostrarAlertaError("Error", "No se pudo eliminar el libro");
                    }
                }
            }

        } catch (SQLException e) {
            mostrarAlertaError("Error de base de datos", e.getMessage());
        }
    }
    private boolean tieneReservasActivas(int idLibro) throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM reserva WHERE idLibro = ? AND estado = 'ACTIVO'";
        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            pst.setInt(1, idLibro);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt("total") > 0;
            }
        }
    }

    private boolean tieneReservasInactivas(int idLibro) throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM reserva WHERE idLibro = ? AND estado = 'INACTIVO'";
        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            pst.setInt(1, idLibro);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt("total") > 0;
            }
        }
    }

    private void eliminarReservasInactivas(int idLibro) throws SQLException {
        String query = "DELETE FROM reserva WHERE idLibro = ? AND estado = 'INACTIVO'";
        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            pst.setInt(1, idLibro);
            pst.executeUpdate();
        }
    }

    
    public void mostrarAlertaExito(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
        dialogPane.getStyleClass().add("informacion");
            
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
        dialogPane.getStyleClass().add("error");
            
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    
    /*
        BOTONES APARTADO AUTORES
                                    */
    
    @FXML
    void btnAccionEditarAutores(ActionEvent event) throws IOException {
        abrirVentanaAutor(new OperacionAutor(Modo.EDITAR, autorSeleccionado),"Editar");
    }
    
    @FXML
    void btnAccionVerAutores(ActionEvent event) throws IOException {
        abrirVentanaAutor(new OperacionAutor(Modo.VER, autorSeleccionado),"Ver");
    }

    @FXML
    void btnAccionAddAutores(ActionEvent event) throws IOException {
        abrirVentanaAutor(new OperacionAutor(Modo.ADD, null),"Añadir");
    }

    @FXML
    void btnAccionBorrarAutores(ActionEvent event) {
        eliminarAutor();
    }

    public void abrirVentanaAutor(OperacionAutor operacion, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/designerAutor.fxml"));
        Parent root = loader.load();

        ControladorAutor controladorAutor = loader.getController();
        controladorAutor.setControladorMain(this);
        controladorAutor.setOperacion(operacion);
        
        Stage stage = new Stage();
        stage.setTitle(titulo + " autor");
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    private void eliminarAutor() {
        try {
            if (autorSeleccionado == null) {
                mostrarAlertaError("Error", "No hay ningún autor seleccionado para eliminar");
                return;
            }

            int idAutor = autorSeleccionado.getIdAutor();

            // Verificar si hay libros asociados al autor
            String sqlCheck = "SELECT COUNT(*) FROM libro_Autor WHERE idAutor = ?";
            try (PreparedStatement pstCheck = conexion.prepareStatement(sqlCheck)) {
                pstCheck.setInt(1, idAutor);
                ResultSet rs = pstCheck.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    mostrarAlertaError(
                        "No permitido",
                        "Este autor no puede ser eliminado porque tiene libros asociados."
                    );
                    return;
                }
            }

            // Confirmación de eliminación
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = confirmacion.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
            dialogPane.getStyleClass().add("confirmacion");
            
                Stage alertStage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar este autor?");
            confirmacion.setContentText("¡Esta acción no se puede deshacer!");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                String sqlDelete = "DELETE FROM autor WHERE idAutor = ?";
                try (PreparedStatement pstDel = conexion.prepareStatement(sqlDelete)) {
                    pstDel.setInt(1, idAutor);
                    int filasAfectadas = pstDel.executeUpdate();

                    if (filasAfectadas > 0) {
                        mostrarAlertaExito("Éxito", "Autor eliminado correctamente");
                        // Actualizar la tabla de autores
                        tbvAutores.setItems(listaTodosAutores());
                        
                        tbvAutores.getSelectionModel().clearSelection();
                        tbvAutores.getFocusModel().focus(null);
                        btnBorrarAutores.setDisable(true);
                        btnEditarAutores.setDisable(true);
                        btnVerAutores.setDisable(true);
                        
                    } else {
                        mostrarAlertaError("Error", "No se pudo eliminar el autor");
                    }
                }
            }

        } catch (SQLException e) {
            mostrarAlertaError("Error de base de datos", e.getMessage());
        }
    }
    
    /*
        BOTONES APARTADO EDITORIALES
                                    */
    
    
    @FXML
    void btnAccionEditarEditoriales(ActionEvent event) throws IOException {        
        abrirVentanaEditorial(new OperacionEditorial(Modo.EDITAR, editorialSeleccionado),"Editar");
    }
    
    @FXML
    void btnAccionVerEditoriales(ActionEvent event) throws IOException {
        abrirVentanaEditorial(new OperacionEditorial(Modo.VER, editorialSeleccionado),"Ver");
    }
    
    @FXML
    void btnAccionAddEditoriales(ActionEvent event) throws IOException {
        abrirVentanaEditorial(new OperacionEditorial(Modo.ADD, null),"Añadir");
    }

    @FXML
    void btnAccionBorrarEditoriales(ActionEvent event) {
        eliminarEditorial();
    }
    public void abrirVentanaEditorial(OperacionEditorial operacion, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/designerEditorial.fxml"));
        Parent root = loader.load();

        ControladorEditorial controladorEditorial = loader.getController();
        controladorEditorial.setControladorMain(this);
        controladorEditorial.setOperacion(operacion);
        
        Stage stage = new Stage();
        stage.setTitle(titulo + " editorial");
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    
    private void eliminarEditorial() {
        try {
            if (editorialSeleccionado == null) {
                mostrarAlertaError("Error", "No hay ninguna editorial seleccionada para eliminar");
                return;
            }

            int idEditorial = editorialSeleccionado.getIdEditorial();

            String sqlCheck = "SELECT COUNT(*) FROM libro WHERE idEditorial = ?";
            try (PreparedStatement pstCheck = conexion.prepareStatement(sqlCheck)) {
                pstCheck.setInt(1, idEditorial);
                ResultSet rs = pstCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    mostrarAlertaError(
                        "No permitido",
                        "Esta editorial no puede ser eliminada porque hay uno o más libros asociados a ella."
                    );
                    return;
                }
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = confirmacion.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
            dialogPane.getStyleClass().add("confirmacion");
            
                Stage alertStage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar la editorial?");
            confirmacion.setContentText("¡Esta acción no se puede deshacer!");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                String sqlDelete = "DELETE FROM editorial WHERE idEditorial = ?";
                try (PreparedStatement pstDel = conexion.prepareStatement(sqlDelete)) {
                    pstDel.setInt(1, idEditorial);
                    int filasAfectadas = pstDel.executeUpdate();

                    if (filasAfectadas > 0) {
                        mostrarAlertaExito("Éxito", "Editorial eliminada correctamente");
                        tbvEditoriales.setItems(listaTodasEditoriales());
                        
                        tbvEditoriales.getSelectionModel().clearSelection();
                        tbvEditoriales.getFocusModel().focus(null);
                        btnBorrarEditoriales.setDisable(true);
                        btnEditarEditoriales.setDisable(true);
                        btnVerEditoriales.setDisable(true);
                    } else {
                        mostrarAlertaError("Error", "No se pudo eliminar la editorial");
                    }
                }
            }

        } catch (SQLException e) {
            mostrarAlertaError("Error de base de datos", e.getMessage());
        }
    }

    
    /*
        BOTONES APARTADO CATEGORIAS
                                    */
    
    @FXML
    void btnAccionVerCategorias(ActionEvent event) throws IOException {
        abrirVentanaCategoria(new OperacionCategoria(Modo.VER,categoriaSeleccionado), "Ver");
    }
    
    @FXML
    void btnAccionAddCategorias(ActionEvent event)  throws IOException{
        abrirVentanaCategoria(new OperacionCategoria(Modo.ADD,null), "Ver");
    }
    @FXML
    void btnAccionBorrarCategorias(ActionEvent event) {
        eliminarCategoria();
    }
    
    public void abrirVentanaCategoria(OperacionCategoria operacion, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/designerCategoria.fxml"));
        Parent root = loader.load();

        ControladorCategoria controladorCategoria = loader.getController();
        controladorCategoria.setControladorMain(this);
        controladorCategoria.setOperacion(operacion);
        
        Stage stage = new Stage();
        stage.setTitle(titulo + " categoria");
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    
    private void eliminarCategoria() {
        try {
            if (categoriaSeleccionado == null) {
                mostrarAlertaError("Error", "No hay ninguna categoría seleccionada para eliminar");
                return;
            }

            int idCategoria = categoriaSeleccionado.getIdCategoria();

            String sqlCheck = "SELECT COUNT(*) FROM libro WHERE idCategoria = ?";
            try (PreparedStatement pstCheck = conexion.prepareStatement(sqlCheck)) {
                pstCheck.setInt(1, idCategoria);
                ResultSet rs = pstCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    mostrarAlertaError(
                        "No permitido",
                        "Esta categoría no puede ser eliminada porque hay uno o más libros asociados a ella."
                    );
                    return;
                }
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = confirmacion.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
            dialogPane.getStyleClass().add("confirmacion");
            
                Stage alertStage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar la categoría?");
            confirmacion.setContentText("¡Esta acción no se puede deshacer!");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                String sqlDelete = "DELETE FROM categoria WHERE idCategoria = ?";
                try (PreparedStatement pstDel = conexion.prepareStatement(sqlDelete)) {
                    pstDel.setInt(1, idCategoria);
                    int filasAfectadas = pstDel.executeUpdate();

                    if (filasAfectadas > 0) {
                        mostrarAlertaExito("Éxito", "Categoría eliminada correctamente");
                        tbvCategorias.setItems(listaTodasCategorias());
                        tbvCategorias.getSelectionModel().clearSelection();
                        tbvCategorias.getFocusModel().focus(null);
                        btnBorrarCategorias.setDisable(true);
                        btnVerCategorias.setDisable(true);
                    } else {
                        mostrarAlertaError("Error", "No se pudo eliminar la categoría");
                    }
                }
            }

        } catch (SQLException e) {
            mostrarAlertaError("Error de base de datos", e.getMessage());
        }
    }
    
    
    /*
        BOTONES APARTADO USUARIOS
                                    */
    @FXML
    void btnAccionEditarUsuarios(ActionEvent event) throws IOException {
        abrirVentanaUsuario(new OperacionUsuario(Modo.EDITAR, usuarioSeleccionado),"Editar");
    }
    
    @FXML
    void btnAccionVerUsuarios(ActionEvent event) throws IOException {
        abrirVentanaUsuario(new OperacionUsuario(Modo.VER, usuarioSeleccionado),"Ver");
    }
    @FXML
    void btnAccionAddUsuarios(ActionEvent event) throws IOException {
        abrirVentanaUsuario(new OperacionUsuario(Modo.ADD, null),"Añadir");
    }

    @FXML
    void btnAccionBorrarUsuarios(ActionEvent event) {
        eliminarUsuario();
    }
    
    @FXML
    void btnAccionUsuario(ActionEvent event) throws IOException {
        int idUsuarioLog = usuarioLog.getIdUsuario();
        abrirVentanaUsuario(new OperacionUsuario(Modo.EDITAR, obtenerUsuarioPorId(idUsuarioLog)), "Editar");
    }
    public void abrirVentanaUsuario(OperacionUsuario operacion, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/designerUsuario.fxml"));
        Parent root = loader.load();

        ControladorUsuario controladorUsuario = loader.getController();
        controladorUsuario.setControladorMain(this);
        controladorUsuario.setOperacion(operacion);
        
        Stage stage = new Stage();
        stage.setTitle(titulo + " usuario");
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    
    private void eliminarUsuario() {
        try {
            if (usuarioSeleccionado == null) {
                mostrarAlertaError("Error", "No hay ningún usuario seleccionado para eliminar");
                return;
            }

            String emailUsuario = usuarioSeleccionado.getEmail();
            if ("admin@admin.com".equalsIgnoreCase(emailUsuario)) {
                mostrarAlertaError("Acción no permitida", "No se puede eliminar la cuenta administradora.");
                return;
            }

            int idUsuario = usuarioSeleccionado.getIdUsuario();

            // Verificar si tiene reservas activas
            String sqlReservasActivas = "SELECT COUNT(*) FROM reserva WHERE idUsuario = ? AND estado = 'ACTIVO'";
            try (PreparedStatement pstActivas = conexion.prepareStatement(sqlReservasActivas)) {
                pstActivas.setInt(1, idUsuario);
                ResultSet rs = pstActivas.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    mostrarAlertaError("No permitido", "Este usuario tiene reservas activas y no puede ser eliminado.");
                    return;
                }
            }

            // Verificar si tiene reservas inactivas
            String sqlReservasInactivas = "SELECT COUNT(*) FROM reserva WHERE idUsuario = ? AND estado != 'ACTIVO'";
            boolean tieneInactivas = false;
            try (PreparedStatement pstInactivas = conexion.prepareStatement(sqlReservasInactivas)) {
                pstInactivas.setInt(1, idUsuario);
                ResultSet rs = pstInactivas.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    tieneInactivas = true;
                }
            }

            // Confirmación con advertencia de reservas inactivas
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = confirmacion.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
            dialogPane.getStyleClass().add("confirmacion");
            
                Stage alertStage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar el usuario?");
            String mensaje = tieneInactivas
                ? "Este usuario tiene reservas inactivas que serán eliminadas.\n¡Esta acción no se puede deshacer!"
                : "¡Esta acción no se puede deshacer!";
            confirmacion.setContentText(mensaje);

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

                // Eliminar reservas inactivas si las hay
                if (tieneInactivas) {
                    String sqlBorrarInactivas = "DELETE FROM reserva WHERE idUsuario = ? AND estado != 'ACTIVO'";
                    try (PreparedStatement pstDelInactivas = conexion.prepareStatement(sqlBorrarInactivas)) {
                        pstDelInactivas.setInt(1, idUsuario);
                        pstDelInactivas.executeUpdate();
                    }
                }

                // Eliminar el usuario
                String sqlEliminarUsuario = "DELETE FROM usuario WHERE idUsuario = ?";
                try (PreparedStatement pstEliminar = conexion.prepareStatement(sqlEliminarUsuario)) {
                    pstEliminar.setInt(1, idUsuario);
                    int filas = pstEliminar.executeUpdate();
                    if (filas > 0) {
                        mostrarAlertaExito("Éxito", "Usuario eliminado correctamente");
                        tbvUsuarios.setItems(listaTodosUsuarios());
                        tbvUsuarios.getSelectionModel().clearSelection();
                        tbvUsuarios.getFocusModel().focus(null);
                        btnBorrarUsuarios.setDisable(true);
                        btnEditarUsuarios.setDisable(true);
                        btnVerUsuarios.setDisable(true);
                       
                        
                        tbvUsuarioReserva.setItems(listaTodosUsuarios());
                    } else {
                        mostrarAlertaError("Error", "No se pudo eliminar el usuario");
                    }
                }
            }

        } catch (SQLException e) {
            mostrarAlertaError("Error de base de datos", e.getMessage());
        }
    }

    public Usuario obtenerUsuarioPorId(int idUsuario) {
        Usuario usuario = null;

        String sql = "SELECT * FROM usuario WHERE idUsuario = ?";

        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setInt(1, idUsuario);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombreUsuario(rs.getString("nombreUsuario"));
                usuario.setApellidoUsuario(rs.getString("apellidoUsuario"));
                usuario.setImagenUsuario(rs.getString("imagenUsuario")); // Asumiendo que es una URL o base64
                usuario.setEmail(rs.getString("email"));
                usuario.setContraseña(rs.getString("contraseña"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setRol(RolUsuario.valueOf(rs.getString("rol")));

            }

        } catch (SQLException e) {
            mostrarAlertaError("Error al obtener usuario", e.getMessage());
        }

        return usuario;
    }

    
    //BOTONES RESERVA

    @FXML
    void btnAccionReservarLibros(ActionEvent event) {
        reservarLibroConConfirmacion(libroSeleccionado);
    }
    
            
    @FXML
    void btnAccionCancelarReserva(ActionEvent event) {
        cancelarReservaDeLibroSeleccionado();
    }
    
    public void reservarLibroConConfirmacion(Libro libroSeleccionado) {
        if (libroSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
            dialogPane.getStyleClass().add("warning");
            
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
            alert.setTitle("Reserva");
            alert.setHeaderText(null);
            alert.setContentText("Debes seleccionar un libro para reservar.");
            alert.showAndWait();
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = confirmacion.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
            dialogPane.getStyleClass().add("confirmacion");
            
                Stage alertStage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        confirmacion.setTitle("Confirmar reserva");
        confirmacion.setHeaderText("¿Deseas reservar el libro?");
        confirmacion.setContentText("Libro: " + libroSeleccionado.getTitulo());

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            String checkReservaSQL = "SELECT COUNT(*) FROM reserva "
                                   + "WHERE idUsuario = ? AND idLibro = ? AND estado = 'ACTIVO'";

            try {
                conexion.setAutoCommit(false);

                try (PreparedStatement psCheck = conexion.prepareStatement(checkReservaSQL)) {
                    psCheck.setInt(1, usuarioLog.getIdUsuario());
                    psCheck.setInt(2, libroSeleccionado.getIdLibro());
                    ResultSet rs = psCheck.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        conexion.rollback();
                        mostrarAlertaError("Reserva duplicada", "Ya hay una reserva activa para este libro");
                        return;
                    }
                }

                if (libroSeleccionado.getCopiasDisponibles() <= 0) {
                    conexion.rollback();
                    mostrarAlertaError("Sin copias disponibles", "No hay copias disponibles de: " + libroSeleccionado.getTitulo());
                    return;
                }

                String insertReservaSQL = "INSERT INTO reserva "
                                       + "(idUsuario, idLibro, fechaReserva, fechaExpiracion, estado) "
                                       + "VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 MONTH), 'ACTIVO')";
                String updateLibroSQL = "UPDATE libro "
                                      + "SET copiasDisponibles = copiasDisponibles - 1 "
                                      + "WHERE idLibro = ? AND copiasDisponibles > 0";

                try (PreparedStatement psInsert = conexion.prepareStatement(insertReservaSQL);
                     PreparedStatement psUpdate = conexion.prepareStatement(updateLibroSQL)) {

                    psInsert.setInt(1, usuarioLog.getIdUsuario());
                    psInsert.setInt(2, libroSeleccionado.getIdLibro());
                    psInsert.executeUpdate();

                    psUpdate.setInt(1, libroSeleccionado.getIdLibro());
                    int rowsAffected = psUpdate.executeUpdate();
                    if (rowsAffected == 0) {
                        conexion.rollback();
                        mostrarAlertaError("No se pudo actualizar las copias.", "Intenta nuevamente más tarde.");
                        return;
                    }

                    conexion.commit();

                    LocalDate expira = LocalDate.now().plusMonths(1);
                    
                    mostrarAlertaExito("Reserva exitosa", "Reserva realizada con éxito para: " 
                                         + libroSeleccionado.getTitulo() 
                                         + "\nLa reserva expirará el: " 
                                         + expira.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                         + "\n(Duración: 1 mes)");

                    // Refrescar tabla
                    if (opcDisponibles.isSelected()) {
                        tbvLibros.setItems(listaLibrosDisponibles());
                    } else if (opcReservas.isSelected()) {
                        tbvLibros.setItems(listaLibrosReservados());
                    } else {
                        tbvLibros.setItems(listaTodosLibros());
                    }
                    tbvLibrosReserva.setItems(listaTodosLibros());
                    tbvLibros.getSelectionModel().clearSelection();
                    tbvLibros.refresh();
                }

            } catch (SQLException e) {
                try { conexion.rollback(); } catch (SQLException ex) { /* Ignorar */ }
                Alert error = new Alert(Alert.AlertType.ERROR);
                
                mostrarAlertaError("Fallo en la reserva.", "Detalles: " + e.getMessage());

            } finally {
                try { conexion.setAutoCommit(true); } catch (SQLException e) { /* Ignorar */ }
            }
        }
    }


    private void cancelarReservaDeLibroSeleccionado() {
        if (libroSeleccionado == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = confirm.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
            dialogPane.getStyleClass().add("confirmacion");
            
                Stage alertStage = (Stage) confirm.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        confirm.setTitle("Anular reserva");
        confirm.setHeaderText("¿Seguro que quieres anular la reserva de:");
        confirm.setContentText(libroSeleccionado.getTitulo());

        Optional<ButtonType> opt = confirm.showAndWait();
        if (opt.isEmpty() || opt.get() != ButtonType.OK) return;

        String sqlAnular = "UPDATE reserva SET estado = 'INACTIVO' "
                         + "WHERE idUsuario = ? AND idLibro = ? AND estado = 'ACTIVO'";

        String sqlDevolver = "UPDATE libro SET copiasDisponibles = copiasDisponibles + 1 "
                          + "WHERE idLibro = ?";

        try {
            conexion.setAutoCommit(false);

            try (PreparedStatement ps1 = conexion.prepareStatement(sqlAnular);
                 PreparedStatement ps2 = conexion.prepareStatement(sqlDevolver)) {

                ps1.setInt(1, usuarioLog.getIdUsuario());
                ps1.setInt(2, libroSeleccionado.getIdLibro());
                int mod1 = ps1.executeUpdate();

                ps2.setInt(1, libroSeleccionado.getIdLibro());
                int mod2 = ps2.executeUpdate();

                if (mod1 == 0 || mod2 == 0) {
                    conexion.rollback();
                    mostrarAlertaError("Error", "No se pudo anular la reserva correctamente.");
                    return;
                }

                conexion.commit();
                mostrarAlertaExito("Perfecto", "Reserva anulada y copia recuperada.");

                // Refrescar tablas
                if (opcDisponibles.isSelected()) {
                    tbvLibros.setItems(listaLibrosDisponibles());
                } else if (opcReservas.isSelected()) {
                    tbvLibros.setItems(listaLibrosReservados());
                } else {
                    tbvLibros.setItems(listaTodosLibros());
                }
                tbvLibrosReserva.setItems(listaTodosLibros());

                btnReservarLibros.setDisable(true);
            }
        } catch (SQLException ex) {
            try { conexion.rollback(); } catch (SQLException __) { }
            mostrarAlertaError("Error", "Error al anular la reserva: " + ex.getMessage());
            
        } finally {
            try { conexion.setAutoCommit(true); } catch (SQLException __) { }
        }
    }
    
    
    //BOTONES RESERVA ADMINISTRADOR
    @FXML
    void btnAccionReservarAdmin(ActionEvent event) {
        reservarLibroAUsuarioPorAdministrador(libroSeleccionadoReserva);
        tbvLibrosReserva.getSelectionModel().clearSelection();
        tbvUsuarioReserva.getSelectionModel().clearSelection();
    }
    
    
    @FXML
    void btnCancelarAccionReservaAdmin(ActionEvent event) {
        cancelarReservaDeUsuarioPorAdministrador();
        tbvLibrosReserva.getSelectionModel().clearSelection();
        tbvUsuarioReserva.getSelectionModel().clearSelection();
    }
    
    public void cancelarReservaDeUsuarioPorAdministrador() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = confirm.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
            dialogPane.getStyleClass().add("confirmacion");
            
                Stage alertStage = (Stage) confirm.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        confirm.setTitle("Anular reserva");
        confirm.setHeaderText("¿Seguro que deseas anular la reserva del libro para el usuario seleccionado?");
        confirm.setContentText("Libro: " + libroSeleccionadoReserva.getTitulo() + 
                               "\nUsuario: " + usuarioSeleccionadoReserva.getNombreUsuario());

        Optional<ButtonType> opt = confirm.showAndWait();
        if (opt.isEmpty() || opt.get() != ButtonType.OK) return;

        String sqlAnular = "UPDATE reserva SET estado = 'INACTIVO' "
                         + "WHERE idUsuario = ? AND idLibro = ? AND estado = 'ACTIVO'";

        String sqlDevolver = "UPDATE libro SET copiasDisponibles = copiasDisponibles + 1 "
                           + "WHERE idLibro = ?";

        try {
            conexion.setAutoCommit(false);

            try (PreparedStatement ps1 = conexion.prepareStatement(sqlAnular);
                 PreparedStatement ps2 = conexion.prepareStatement(sqlDevolver)) {

                ps1.setInt(1, usuarioSeleccionadoReserva.getIdUsuario());
                ps1.setInt(2, libroSeleccionadoReserva.getIdLibro());
                int mod1 = ps1.executeUpdate();

                ps2.setInt(1, libroSeleccionadoReserva.getIdLibro());
                int mod2 = ps2.executeUpdate();

                if (mod1 == 0 || mod2 == 0) {
                    conexion.rollback();
                    mostrarAlertaError("Error", sqlAnular);
                    return;
                }

                conexion.commit();
                mostrarAlertaExito("Anular", "Reserva anulada y copia devuelta al inventario.");

                if (opcDisponibles.isSelected()) {
                    tbvLibros.setItems(listaLibrosDisponibles());
                } else if (opcReservas.isSelected()) {
                    tbvLibros.setItems(listaLibrosReservados());
                } else {
                    tbvLibros.setItems(listaTodosLibros());
                }
                tbvLibrosReserva.setItems(listaTodosLibros());
                btnReservarAdmin.setDisable(true);
                btnCancelarReservarAdmin.setDisable(true);

            }

        } catch (SQLException ex) {
            try { conexion.rollback(); } catch (SQLException __) { }
            mostrarAlertaError("Error", "Error al anular la reserva: " + ex.getMessage());
        } finally {
            try { conexion.setAutoCommit(true); } catch (SQLException __) { }
        }
    }

    

    public void reservarLibroAUsuarioPorAdministrador(Libro libroSeleccionado) {
        if (libroSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
            dialogPane.getStyleClass().add("warning");
            
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
            
            alert.setTitle("Reserva");
            alert.setHeaderText(null);
            alert.setContentText("Debes seleccionar un libro para reservar.");
            alert.showAndWait();
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = confirmacion.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/alerta.css").toExternalForm());
            dialogPane.getStyleClass().add("confirmacion");
            
                Stage alertStage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        confirmacion.setTitle("Confirmar reserva");
        confirmacion.setHeaderText("¿Deseas reservar el libro?");
        confirmacion.setContentText("Libro: " + libroSeleccionado.getTitulo());

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            String checkReservaSQL = "SELECT COUNT(*) FROM reserva "
                                   + "WHERE idUsuario = ? AND idLibro = ? AND estado = 'ACTIVO'";

            try {
                conexion.setAutoCommit(false);

                try (PreparedStatement psCheck = conexion.prepareStatement(checkReservaSQL)) {
                    psCheck.setInt(1, usuarioSeleccionadoReserva.getIdUsuario());
                    psCheck.setInt(2, libroSeleccionado.getIdLibro());
                    ResultSet rs = psCheck.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        conexion.rollback();
                        mostrarAlertaError("Error en la reserva", "Ya tienes una reserva activa para este libro");
                        return;
                    }
                }

                if (libroSeleccionado.getCopiasDisponibles() <= 0) {
                    conexion.rollback();
                    mostrarAlertaError("Sin copias disponibles", "No hay copias disponibles de: " + libroSeleccionado.getTitulo());
                    return;
                }

                String insertReservaSQL = "INSERT INTO reserva "
                                       + "(idUsuario, idLibro, fechaReserva, fechaExpiracion, estado) "
                                       + "VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 MONTH), 'ACTIVO')";
                String updateLibroSQL = "UPDATE libro "
                                      + "SET copiasDisponibles = copiasDisponibles - 1 "
                                      + "WHERE idLibro = ? AND copiasDisponibles > 0";

                try (PreparedStatement psInsert = conexion.prepareStatement(insertReservaSQL);
                     PreparedStatement psUpdate = conexion.prepareStatement(updateLibroSQL)) {

                    psInsert.setInt(1, usuarioSeleccionadoReserva.getIdUsuario());
                    psInsert.setInt(2, libroSeleccionado.getIdLibro());
                    psInsert.executeUpdate();

                    psUpdate.setInt(1, libroSeleccionado.getIdLibro());
                    int rowsAffected = psUpdate.executeUpdate();
                    if (rowsAffected == 0) {
                        conexion.rollback();
                        mostrarAlertaError("Error", "No se pudo actualizar las copias, intentalo nuevamente más tarde.");
                        return;
                    }

                    conexion.commit();

                    LocalDate expira = LocalDate.now().plusMonths(1);
                    mostrarAlertaExito("Reserva exitosa", "Reserva realizada con éxito para: " 
                                         + libroSeleccionado.getTitulo() 
                                         + "\nLa reserva expirará el: " 
                                         + expira.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                         + "\n(Duración: 1 meses)");
                    
                    if (opcDisponibles.isSelected()) {
                        tbvLibros.setItems(listaLibrosDisponibles());
                    } else if (opcReservas.isSelected()) {
                        tbvLibros.setItems(listaLibrosReservados());
                    } else {
                        tbvLibros.setItems(listaTodosLibros());
                    }
                    
                    tbvLibros.setItems(listaLibrosDisponibles());

                } 

            } catch (SQLException e) {
                try { conexion.rollback(); } catch (SQLException ex) { /*…*/ }
                mostrarAlertaError("Error", "Fallo en la reserva");

            } finally {
                try { conexion.setAutoCommit(true); } catch (SQLException e) { /*…*/ }
            }
        }
    }


    @FXML
    void btnAccionSalir(ActionEvent event) {
        salir();
    }
    
    
    public ObservableList<Libro> listaTodosLibros() {
        ObservableList<Libro> listaLibros = FXCollections.observableArrayList();

        if (conexion != null) {
            String query = "SELECT * FROM libro";
            try {
                rs = st.executeQuery(query);
                while (rs.next()) {
                    Libro libro = new Libro(
                        rs.getInt("idLibro"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getString("ISBN"),
                        rs.getInt("totalCopias"),
                        rs.getInt("copiasDisponibles"),
                        rs.getInt("idCategoria"),
                        rs.getInt("idEditorial")
                    );
                    listaLibros.add(libro);
                }
            } catch (SQLException e) {
                System.out.println("Excepción SQL: " + e.getMessage());
            }
        }

        return listaLibros;
    }

    public Connection dameConnection() { 
        return ConexionSingleton.obtenerConexion();
    }  
    
    
    public ObservableList<Categoria> listaTodasCategorias() {
        ObservableList<Categoria> lista = FXCollections.observableArrayList();
        String query = "SELECT idCategoria, nombreCategoria FROM categoria";
        try (PreparedStatement pst = conexion.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(new Categoria(
                    rs.getInt("idCategoria"),
                    rs.getString("nombreCategoria")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener categorías: " + e.getMessage());
        }
        return lista;
    }

    public ObservableList<Editorial> listaTodasEditoriales() {
        ObservableList<Editorial> lista = FXCollections.observableArrayList();
        String query = "SELECT idEditorial, nombreEditorial, direccion, telefono FROM editorial";
        try (PreparedStatement pst = conexion.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(new Editorial(
                    rs.getInt("idEditorial"),
                    rs.getString("nombreEditorial"),
                    rs.getString("direccion"),
                    rs.getString("telefono")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener editoriales: " + e.getMessage());
        }
        return lista;
    }

    
    public ObservableList<Libro> listaLibrosDisponibles() {
        ObservableList<Libro> listaLibros = FXCollections.observableArrayList();

        String query = "SELECT * FROM libro WHERE copiasDisponibles > 0";

        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Libro libro = new Libro(
                    rs.getInt("idLibro"),
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getString("ISBN"),
                    rs.getInt("totalCopias"),
                    rs.getInt("copiasDisponibles"),
                    rs.getInt("idCategoria"),
                    rs.getInt("idEditorial")
                );
                listaLibros.add(libro);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar libros disponibles: " + e.getMessage());
        }

        return listaLibros;
    }

    
    public ObservableList<Libro> listaLibrosReservados() {
        ObservableList<Libro> listaLibros = FXCollections.observableArrayList();

        String query = "SELECT DISTINCT l.* FROM libro l " +
                       "JOIN reserva r ON l.idLibro = r.idLibro " +
                       "WHERE r.estado = 'ACTIVO' AND r.idUsuario = ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, usuarioLog.getIdUsuario());
            rs = ps.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro(
                    rs.getInt("idLibro"),
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getString("ISBN"),
                    rs.getInt("totalCopias"),
                    rs.getInt("copiasDisponibles"),
                    rs.getInt("idCategoria"),
                    rs.getInt("idEditorial")
                );
                listaLibros.add(libro);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar libros reservados por el usuario: " + e.getMessage());
        }

        return listaLibros;
    }

    public ObservableList<Autor> listaTodosAutores() {
        ObservableList<Autor> listaAutores = FXCollections.observableArrayList();

        String query = "SELECT * FROM autor";

        try (PreparedStatement pst = conexion.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Autor autor = new Autor(
                    rs.getInt("idAutor"),
                    rs.getString("nombreAutor"),
                    rs.getString("apellidoAutor"),
                    rs.getString("nacionalidad"),
                    rs.getString("imagenAutor"),
                    rs.getString("biografia")
                );
                listaAutores.add(autor);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de autores: " + e.getMessage());
        }

        return listaAutores;
    }


    public ObservableList<Usuario> listaTodosUsuarios() {
        ObservableList<Usuario> lista = FXCollections.observableArrayList();
        String query = "SELECT idUsuario, nombreUsuario, apellidoUsuario, imagenUsuario, "
                     + "email, `contraseña`, telefono, direccion, rol "
                     + "FROM usuario";

        try (PreparedStatement pst = conexion.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String rolStr = rs.getString("rol");
                RolUsuario rolEnum = RolUsuario.valueOf(rolStr);

                Usuario usuario = new Usuario(
                    rs.getInt("idUsuario"),
                    rs.getString("nombreUsuario"),
                    rs.getString("apellidoUsuario"),
                    rs.getString("imagenUsuario"),
                    rs.getString("email"),
                    rs.getString("contraseña"),
                    rs.getString("telefono"),
                    rs.getString("direccion"),
                    rolEnum
                );
                lista.add(usuario);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }

        return lista;
    }


    
    private void actualizarInfo(){
        txtNombreUsuario.setText(usuarioLog.getNombreUsuario());
        txtEmailUsuario.setText(usuarioLog.getEmail());
        imgUsuario.setImage(base64ToImage(usuarioLog.getImagenUsuario()));
        imgUsuario.setFitWidth(50);
        imgUsuario.setFitHeight(50);
        imgUsuario.setPreserveRatio(true);
        
        if (usuarioLog.getRol() != RolUsuario.ADMINISTRADOR) {
            tabUsuarios.setDisable(true);
            tabReservas.setDisable(true);
        }
        
    }

    public void procesarReservasExpiradas() {
        String sql = 
            "UPDATE libro l " +
            "JOIN reserva r ON l.idLibro = r.idLibro " +
            "SET " +
            "  l.copiasDisponibles = l.copiasDisponibles + 1, " +
            "  r.estado = 'INACTIVO' " +
            "WHERE " +
            "  r.estado = 'ACTIVO' " +
            "  AND r.fechaExpiracion < CURDATE()";
        try (Statement st = conexion.createStatement()) {
            int filas = st.executeUpdate(sql);
            System.out.println("Reservas expiradas procesadas: " + filas);
        } catch (SQLException e) {
            System.err.println("Error procesando expiración de reservas: " + e.getMessage());
        }
    }
 
    public void eliminarReservasMuyAntiguas() {
        String sql = 
            "DELETE FROM reserva " +
            "WHERE estado = 'INACTIVO' " +
            "  AND fechaExpiracion < DATE_SUB(CURDATE(), INTERVAL 1 MONTH)";
        try (Statement st = conexion.createStatement()) {
            int filasBorradas = st.executeUpdate(sql);
            System.out.println("Reservas muy antiguas eliminadas: " + filasBorradas);
        } catch (SQLException e) {
            System.err.println("Error eliminando reservas antiguas: " + e.getMessage());
        }
    }
    
    public void confBotonesAutores(){
        btnVerAutores.setDisable(false);

        if (usuarioLog.getRol() == RolUsuario.ADMINISTRADOR) {
            btnEditarAutores.setDisable(false);
            btnBorrarAutores.setDisable(false);
        } else {
            btnEditarAutores.setDisable(true);
            btnBorrarAutores.setDisable(true);
        }
    }
    public void confBotonesCategorias() {
        btnVerCategorias.setDisable(false);

        if (usuarioLog.getRol() == RolUsuario.ADMINISTRADOR) {
            btnBorrarCategorias.setDisable(false);
        } else {
            btnBorrarCategorias.setDisable(true);
        }
    }

    public void confBotonesEditoriales() {
        btnVerEditoriales.setDisable(false);

        if (usuarioLog.getRol() == RolUsuario.ADMINISTRADOR) {
            btnEditarEditoriales.setDisable(false);
            btnBorrarEditoriales.setDisable(false);
        } else {
            btnEditarEditoriales.setDisable(true);
            btnBorrarEditoriales.setDisable(true);
        }
    }

    public void confBotonesUsuario(){
        btnEditarUsuarios.setDisable(false);
        btnVerUsuarios.setDisable(false);
        btnBorrarUsuarios.setDisable(false);
        
    }

    public void resetearBotonesLibros() {
        boolean isAdmin = usuarioLog != null && usuarioLog.getRol() == RolUsuario.ADMINISTRADOR;
        
        btnEditarLibros   .setDisable(true);
        btnBorrarLibros   .setDisable(true);
        btnVerLibros      .setDisable(true);
        btnReservarLibros .setDisable(true);
        btnCancelarReserva.setDisable(true);
    }

    public void setUsuarioLog(Usuario usuario) {
        this.usuarioLog = usuario;

        opcTodosLibros.setSelected(true);
        tbvLibros.setItems(listaTodosLibros());

        resetearBotonesLibros();
    }

    
    public void recibirUsuarioLogin(Usuario us){
        usuarioLog = us;
        actualizarInfo();
    }
    
    public void configurarFiltros(){
        txtFldLibro.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarLibros(newValue);
        });
        
        txtFldAutor.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarAutor(newValue);
        });
        
        txtFldEditorial.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarEditorial(newValue);
        });
        
        txtFldCategoria.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarCategoria(newValue);
        });
        
        txtFldUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarUsuario(newValue);
        });
        
        txtFldUsuarioReserva.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarUsuarioReserva(newValue);
        });
        
        txtFldLibroReserva.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarLibroReserva(newValue);
        });
    }
    
    
    private void filtrarUsuarioReserva(String criterio) {
        ObservableList<Usuario> listaCompleta = listaTodosUsuarios();

        FilteredList<Usuario> listaFiltrada = new FilteredList<>(listaCompleta, usuario -> {
            if (criterio == null || criterio.isEmpty()) {
                return true; 
            }
            String criterioEnMinusculas = criterio.toLowerCase();

            return usuario.getNombreUsuario().toLowerCase().contains(criterioEnMinusculas);
        });

        tbvUsuarioReserva.setItems(listaFiltrada);
    }
    
    private void filtrarLibroReserva(String criterio) {
        ObservableList<Libro> listaCompleta = listaLibrosDisponibles();

        FilteredList<Libro> listaFiltrada = new FilteredList<>(listaCompleta, libro -> {
            if (criterio == null || criterio.isEmpty()) {
                return true; 
            }
            String criterioEnMinusculas = criterio.toLowerCase();

            return libro.getTitulo().toLowerCase().contains(criterioEnMinusculas);
        });

        tbvLibrosReserva.setItems(listaFiltrada);
    }
    
    private void filtrarLibros(String criterio) {
        opcTodosLibros.setSelected(true);
        ObservableList<Libro> listaCompleta = listaTodosLibros();

        FilteredList<Libro> listaFiltrada = new FilteredList<>(listaCompleta, libro -> {
            if (criterio == null || criterio.isEmpty()) {
                return true; 
            }
            String criterioEnMinusculas = criterio.toLowerCase();

            return libro.getTitulo().toLowerCase().contains(criterioEnMinusculas);
        });

        tbvLibros.setItems(listaFiltrada);
    }
    
    private void filtrarAutor(String criterio) {
        ObservableList<Autor> listaCompleta = listaTodosAutores();

        FilteredList<Autor> listaFiltrada = new FilteredList<>(listaCompleta, autor -> {
            if (criterio == null || criterio.isEmpty()) {
                return true; 
            }
            String criterioEnMinusculas = criterio.toLowerCase();

            return autor.getNombreAutor().toLowerCase().contains(criterioEnMinusculas);
        });

        tbvAutores.setItems(listaFiltrada);
    }
    
    private void filtrarEditorial(String criterio) {
        ObservableList<Editorial> listaCompleta = listaTodasEditoriales();

        FilteredList<Editorial> listaFiltrada = new FilteredList<>(listaCompleta, editorial -> {
            if (criterio == null || criterio.isEmpty()) {
                return true; 
            }
            String criterioEnMinusculas = criterio.toLowerCase();

            return editorial.getNombreEditorial().toLowerCase().contains(criterioEnMinusculas);
        });

        tbvEditoriales.setItems(listaFiltrada);
    }
    
    private void filtrarCategoria(String criterio) {
        ObservableList<Categoria> listaCompleta = listaTodasCategorias();

        FilteredList<Categoria> listaFiltrada = new FilteredList<>(listaCompleta, categoria -> {
            if (criterio == null || criterio.isEmpty()) {
                return true; 
            }
            String criterioEnMinusculas = criterio.toLowerCase();

            return categoria.getNombreCategoria().toLowerCase().contains(criterioEnMinusculas);
        });

        tbvCategorias.setItems(listaFiltrada);
    }
    
    private void filtrarUsuario(String criterio) {
        ObservableList<Usuario> listaCompleta = listaTodosUsuarios();

        FilteredList<Usuario> listaFiltrada = new FilteredList<>(listaCompleta, usuario -> {
            if (criterio == null || criterio.isEmpty()) {
                return true; 
            }
            String criterioEnMinusculas = criterio.toLowerCase();

            return usuario.getNombreUsuario().toLowerCase().contains(criterioEnMinusculas);
        });

        tbvUsuarios.setItems(listaFiltrada);
    }
    
    private void relacionarColumnas(){
        //Tabla Libros
        colISBNLibros.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colTituloLibros.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colCopiasDisponiblesLibros.setCellValueFactory(new PropertyValueFactory<>("copiasDisponibles"));
        colTotalCopiasLibros.setCellValueFactory(new PropertyValueFactory<>("totalCopias"));
        
        //Tabla Autor
        colNombreAutores.setCellValueFactory(new PropertyValueFactory<>("nombreAutor"));
        colApellidoAutores.setCellValueFactory(new PropertyValueFactory<>("apellidosAutor"));
        colNacionalidadAutores.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        
        //Tabla Editorial
        colIdEditoriales.setCellValueFactory(new PropertyValueFactory<>("idEditorial"));
        colNombreEditoriales.setCellValueFactory(new PropertyValueFactory<>("nombreEditorial"));
        colDireccionEditoriales.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTelefonoEditoriales.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        
        //Tabla Categoria
        colIdCategorias.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        colNombreCategorias.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria"));
        
        //Tabla Usuarios
        colNombreUsuarios.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colApellidosUsuarios.setCellValueFactory(new PropertyValueFactory<>("apellidoUsuario"));
        colEmailUsuarios.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefonoUsuarios.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccionUsuarios.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        
        //TABLAS DEL APARTADO DE RESERVAS ADMINISTRADOR
        colNombreUsuarioReserva.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colApellidoUsuarioReserva.setCellValueFactory(new PropertyValueFactory<>("apellidoUsuario"));
        colTelefonoUsuarioReserva.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccionUsuarioReserva.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        
        
        colISBNLibroReserva.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colTituloLibroReserva.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colCopiasLibroReserva.setCellValueFactory(new PropertyValueFactory<>("copiasDisponibles"));
        colDescripcionLibroReserva.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        
        
    }
    
    private void inicializarImagenes(){
        //Img Salida
        imgSalir.setImage(new Image(getClass().getClassLoader().getResourceAsStream("salir.png")));
        
        //Img apartado Libros:        
        imgLupaLibro.setImage(new Image(getClass().getClassLoader().getResourceAsStream("lupa.png")));
        imgVerLibro.setImage(new Image(getClass().getClassLoader().getResourceAsStream("ver.png")));
        imgEditarLibro.setImage(new Image(getClass().getClassLoader().getResourceAsStream("editar.png")));
        imgAddLibro.setImage(new Image(getClass().getClassLoader().getResourceAsStream("add.png")));
        imgBorrarLibro.setImage(new Image(getClass().getClassLoader().getResourceAsStream("borrar.png")));


        //Img apartado Autores
        imgLupaAutor.setImage(new Image(getClass().getClassLoader().getResourceAsStream("lupa.png")));
        imgVerAutor.setImage(new Image(getClass().getClassLoader().getResourceAsStream("ver.png")));
        imgEditarAutor.setImage(new Image(getClass().getClassLoader().getResourceAsStream("editar.png")));
        imgAddAutor.setImage(new Image(getClass().getClassLoader().getResourceAsStream("add.png")));
        imgBorrarAutor.setImage(new Image(getClass().getClassLoader().getResourceAsStream("borrar.png")));
        
        
        //Img apartado Editoriales
        imgLupaEditorial.setImage(new Image(getClass().getClassLoader().getResourceAsStream("lupa.png")));
        imgVerEditorial.setImage(new Image(getClass().getClassLoader().getResourceAsStream("ver.png")));
        imgEditarEditorial.setImage(new Image(getClass().getClassLoader().getResourceAsStream("editar.png")));
        imgAddEditorial.setImage(new Image(getClass().getClassLoader().getResourceAsStream("add.png")));
        imgBorrarEditorial.setImage(new Image(getClass().getClassLoader().getResourceAsStream("borrar.png")));
        
        
        //Img apartado Categorias
        imgLupaCategoria.setImage(new Image(getClass().getClassLoader().getResourceAsStream("lupa.png")));
        imgVerCategoria.setImage(new Image(getClass().getClassLoader().getResourceAsStream("ver.png")));
        imgAddCategoria.setImage(new Image(getClass().getClassLoader().getResourceAsStream("add.png")));
        imgBorrarCategoria.setImage(new Image(getClass().getClassLoader().getResourceAsStream("borrar.png")));
        
        
        //Img apartado Usuarios
        imgLupaUsuario.setImage(new Image(getClass().getClassLoader().getResourceAsStream("lupa.png")));
        imgVerUsuario.setImage(new Image(getClass().getClassLoader().getResourceAsStream("ver.png")));
        imgEditarUsuario.setImage(new Image(getClass().getClassLoader().getResourceAsStream("editar.png")));
        imgAddUsuario.setImage(new Image(getClass().getClassLoader().getResourceAsStream("add.png")));
        imgBorrarUsuario.setImage(new Image(getClass().getClassLoader().getResourceAsStream("borrar.png")));
        
    }
    
    public String convertirImagenA64(File archivoImagen) {
        try (
            FileInputStream fileInputStream = new FileInputStream(archivoImagen);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes); 
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public Image base64ToImage(String base64Image) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(bis);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            return image;
            
	} catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
	}
    }
    
    
    public void salir() {
        Platform.runLater(() -> {
            System.exit(0);
        });
    }
    
}
