
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
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import modelo.Autor;
import modelo.ConexionSingleton;
import modelo.Libro;
import static modelo.Modo.EDITAR;
import static modelo.Modo.VER;
import modelo.OperacionLibro;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class ControladorLibro implements Initializable{

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private ListView<String> lstCategorias;

    @FXML
    private ListView<String> lstEditoriales;
    
    @FXML
    private ListView<String> lstAutores;

    @FXML
    private TextField txtCategoriaElegida;
    
    @FXML
    private TextField txtAutorElegido;

    @FXML
    private TextField txtCategoriaIntroducida;
    
    @FXML
    private TextField txtAutorIntroducido;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtEditorialElegida;

    @FXML
    private TextField txtEditorialIntroducida;

    @FXML
    private TextField txtISBN;

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextField txtTotalCopias;
    
    
    private ObservableList<String> listaNombreCategoria;
    private ObservableList<String> listaNombreEditorial;
    private ObservableList<String> listaNombreAutores;
    FilteredList<String> filteredCategoria;
    FilteredList<String> filteredEditorial;
    FilteredList<String> filteredAutor;
    
    Connection conexion;
    Statement st;
    ResultSet rs;
    private ControladorMain cMain;
    private OperacionLibro operacion;
    Libro libroSeleccionado;
    
    private ValidationSupport validationSupport;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configuración para categorías
        listaNombreCategoria = FXCollections.observableArrayList();
        filteredCategoria = new FilteredList<>(listaNombreCategoria);
        lstCategorias.setItems(filteredCategoria);

        txtCategoriaIntroducida.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredCategoria.setPredicate(item -> 
                newValue == null || newValue.isEmpty() || 
                item.toLowerCase().contains(newValue.toLowerCase())
            );
        });

        // Configuración para editoriales
        listaNombreEditorial = FXCollections.observableArrayList();
        filteredEditorial = new FilteredList<>(listaNombreEditorial);
        lstEditoriales.setItems(filteredEditorial);

        txtEditorialIntroducida.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredEditorial.setPredicate(item ->
                newValue == null || newValue.isEmpty() ||
                item.toLowerCase().contains(newValue.toLowerCase())
            );
        });
        
        //Configuración para autores
        listaNombreAutores = FXCollections.observableArrayList();
        filteredAutor = new FilteredList<>(listaNombreAutores);
        lstAutores.setItems(filteredAutor);

        txtAutorIntroducido.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAutor.setPredicate(item ->
                newValue == null || newValue.isEmpty() ||
                item.toLowerCase().contains(newValue.toLowerCase())
            );
        });
        
        // Manejador para doble clic en categorías
        lstCategorias.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Verifica doble clic
                String seleccionado = lstCategorias.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    txtCategoriaElegida.setText(seleccionado);
                }
            }
        });

        // Manejador para doble clic en editoriales
        lstEditoriales.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Verifica doble clic
                String seleccionado = lstEditoriales.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    txtEditorialElegida.setText(seleccionado);
                }
            }
        });
        
        
        // Manejador para doble clic en autores
        lstAutores.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Verifica doble clic
                String seleccionado = lstAutores.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    txtAutorElegido.setText(seleccionado);
                }
            }
        });
    }
    

    public void initDatos() {
        try {
            conexion = cMain.dameConnection();
            if (conexion != null) {
                this.st = conexion.createStatement();
                listarCategorias();
                listarEditoriales();
                listarAutores();
                validarCampos();
                
            }
        } catch (SQLException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
        }
    }

    public void setControladorMain(ControladorMain cMain) {
        this.cMain = cMain;
        initDatos();
    }
    public void setOperacion(OperacionLibro operacion) {
        this.operacion = operacion;
        libroSeleccionado = operacion.libro();
        configurarInterfazSegunModo();
    }
    
    private void configurarInterfazSegunModo() {
        switch (operacion.modo()) {
            case VER -> deshabilitarCampos();
            case EDITAR -> edicion();
        }
    }
    
    @FXML
    void btnAccionAceptar(ActionEvent event) throws SQLException {
        switch (operacion.modo()) {
            case ADD -> {
                crearNuevoLibro();
            }
            case EDITAR -> {
                validationSupport.setErrorDecorationEnabled(false); 
                editarLibro();
            }
        }
        
    }

    @FXML
    void btnAccionCancelar(ActionEvent event) {
        cerrarVentana(event);
    }
    
    private void deshabilitarCampos(){
        rellenarDatos();
        txtTitulo.setEditable(false);
        txtISBN.setEditable(false);
        txtTotalCopias.setEditable(false);
        txtDescripcion.setEditable(false);
        lstCategorias.setEditable(false);
        lstEditoriales.setEditable(false);
        lstAutores.setEditable(false);
        btnAceptar.setVisible(false);
        lstCategorias.setDisable(true);
        lstEditoriales.setDisable(true);
        lstAutores.setDisable(true);
    }
    private void edicion(){
        rellenarDatos();
        txtTitulo.setEditable(false);
        txtISBN.setEditable(false);
    }
    
    /*private void crearNuevoLibro() {
        try {
            // Validar campos numéricos
            int totalCopias = Integer.parseInt(txtTotalCopias.getText());
            int idCategoria = obtenerIdCategoriaPorNombre(txtCategoriaElegida.getText());
            int idEditorial = obtenerIdEditorialPorNombre(txtEditorialElegida.getText());
            int idAutor = obtenerIdAutorPorNombreCompleto(txtAutorElegido.getText());

            Libro nuevoLibro = new Libro(
                0,
                txtTitulo.getText().trim(),
                txtDescripcion.getText().trim(),
                txtISBN.getText().trim(),
                totalCopias,
                totalCopias,
                idCategoria,
                idEditorial
            );

            // Insertar en BD
            String query = "INSERT INTO libro (titulo, descripcion, ISBN, totalCopias, copiasDisponibles, idCategoria, idEditorial) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pst = conexion.prepareStatement(query)) {

                pst.setString(1, nuevoLibro.getTitulo());
                pst.setString(2, nuevoLibro.getDescripcion());
                pst.setString(3, nuevoLibro.getISBN());
                pst.setInt(4, nuevoLibro.getTotalCopias());
                pst.setInt(5, nuevoLibro.getCopiasDisponibles());
                pst.setInt(6, nuevoLibro.getIdCategoria());
                pst.setInt(7, nuevoLibro.getIdEditorial());

                int filasAfectadas = pst.executeUpdate();

                if (filasAfectadas > 0) {
                    mostrarAlertaExito("Éxito", "Libro creado correctamente");
                } else {
                    mostrarAlertaError("Error", "No se pudo crear el libro");
                }

            } catch (SQLException e) {
                mostrarAlertaError("Error de base de datos", e.getMessage());
            }

        } catch (NumberFormatException e) {
            mostrarAlertaError("Error numérico", "Verifique los campos: Total copias, Categoría y Editorial deben ser números");
        }
        
        cMain.tbvLibros.setItems(cMain.listaTodosLibros());
    }*/
    
    
    
    private void crearNuevoLibro() {
        try {
            
            String titulo = txtTitulo.getText().trim();

            // Validar si ya existe un libro con ese título
            if (existeLibroConTitulo(titulo)) {
                mostrarAlertaError("Error", "Ya existe un libro con el título: " + titulo);
                return;  // Salir del método para no crear otro libro igual
            }
            
            int totalCopias = Integer.parseInt(txtTotalCopias.getText().trim());
            int idCategoria = obtenerIdCategoriaPorNombre(txtCategoriaElegida.getText().trim());
            int idEditorial = obtenerIdEditorialPorNombre(txtEditorialElegida.getText().trim());
            int idAutor = obtenerIdAutorPorNombreCompleto(txtAutorElegido.getText().trim());

            Libro nuevoLibro = new Libro(
                0,
                txtTitulo.getText().trim(),
                txtDescripcion.getText().trim(),
                txtISBN.getText().trim(),
                totalCopias,
                totalCopias,
                idCategoria,
                idEditorial
            );

            // Insertar libro
            String queryLibro = "INSERT INTO libro (titulo, descripcion, ISBN, totalCopias, copiasDisponibles, idCategoria, idEditorial) "
                              + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstLibro = conexion.prepareStatement(queryLibro, Statement.RETURN_GENERATED_KEYS)) {
                pstLibro.setString(1, nuevoLibro.getTitulo());
                pstLibro.setString(2, nuevoLibro.getDescripcion());
                pstLibro.setString(3, nuevoLibro.getISBN());
                pstLibro.setInt(4, nuevoLibro.getTotalCopias());
                pstLibro.setInt(5, nuevoLibro.getCopiasDisponibles());
                pstLibro.setInt(6, nuevoLibro.getIdCategoria());
                pstLibro.setInt(7, nuevoLibro.getIdEditorial());

                int filasAfectadas = pstLibro.executeUpdate();

                if (filasAfectadas > 0) {
                    ResultSet generatedKeys = pstLibro.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int idLibro = generatedKeys.getInt(1);

                        // Insertar relación libro_autor
                        String queryRelacion = "INSERT INTO libro_Autor (idLibro, idAutor) VALUES (?, ?)";
                        try (PreparedStatement pstRelacion = conexion.prepareStatement(queryRelacion)) {
                            pstRelacion.setInt(1, idLibro);
                            pstRelacion.setInt(2, idAutor);
                            pstRelacion.executeUpdate();
                        }

                        mostrarAlertaExito("Éxito", "Libro creado correctamente");
                    }
                } else {
                    mostrarAlertaError("Error", "No se pudo crear el libro");
                }

            } catch (SQLException e) {
                mostrarAlertaError("Error de base de datos", e.getMessage());
            }

        } catch (NumberFormatException e) {
            mostrarAlertaError("Error", "El número de copias no es válido");
        }

        cMain.tbvLibros.setItems(cMain.listaTodosLibros());
        
        cMain.tbvLibrosReserva.setItems(cMain.listaLibrosDisponibles());
    }
    
    public boolean existeLibroConTitulo(String titulo) {
        boolean existe = false;

        if (conexion != null) {
            String query = "SELECT 1 FROM libro WHERE titulo = ? LIMIT 1";

            try (PreparedStatement ps = conexion.prepareStatement(query)) {
                ps.setString(1, titulo.trim());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        existe = true;
                    }
                }

            } catch (SQLException e) {
                System.out.println("Excepción SQL: " + e.getMessage());
            }
        }

        return existe;
    }

    
    private void validarCampos() {
        validationSupport = new ValidationSupport();

        validationSupport.registerValidator(txtTitulo, 
            Validator.createEmptyValidator("El título es obligatorio"));

        validationSupport.registerValidator(txtISBN, 
            Validator.createEmptyValidator("El ISBN es obligatorio"));

        validationSupport.registerValidator(txtDescripcion, 
            Validator.createEmptyValidator("La descripción es obligatoria"));

        validationSupport.registerValidator(txtTotalCopias, true, 
            Validator.createPredicateValidator(
                input -> input != null && !input.toString().trim().isEmpty() && input.toString().matches("\\d+"),
                "Debe ingresar un número en total de copias"));
        
        validationSupport.registerValidator(txtCategoriaElegida, 
            Validator.createEmptyValidator("Debe seleccionar una categoría"));

        validationSupport.registerValidator(txtEditorialElegida, 
            Validator.createEmptyValidator("Debe seleccionar una editorial"));
        
        validationSupport.registerValidator(txtAutorElegido, 
            Validator.createEmptyValidator("Debe seleccionar un autor"));
        
        validationSupport.validationResultProperty().addListener((obs, oldResult, newResult) -> {
            btnAceptar.setDisable(newResult.getErrors().size() > 0);
        });

    }
    
    public void mostrarAlertaExito(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
        
        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void editarLibro() throws SQLException {
        try {
            if (libroSeleccionado == null) {
                mostrarAlertaError("Error", "No hay ningún libro seleccionado");
                return;
            }

            int totalCopias = Integer.parseInt(txtTotalCopias.getText().trim());
            int idCategoria = obtenerIdCategoriaPorNombre(txtCategoriaElegida.getText().trim());
            int idEditorial = obtenerIdEditorialPorNombre(txtEditorialElegida.getText().trim());
            int idAutorNuevo = obtenerIdAutorPorNombreCompleto(txtAutorElegido.getText().trim());

            int copiasReservadas = obtenerCopiasReservadas(libroSeleccionado.getIdLibro());

            if (totalCopias < copiasReservadas) {
                mostrarAlertaError("Error de validación",
                    "No se puede establecer un número de copias menor a las ya reservadas (" + copiasReservadas + ").");
                return;
            }

            libroSeleccionado.setTitulo(txtTitulo.getText().trim());
            libroSeleccionado.setDescripcion(txtDescripcion.getText().trim());
            libroSeleccionado.setISBN(txtISBN.getText().trim());
            libroSeleccionado.setTotalCopias(totalCopias);
            libroSeleccionado.setIdCategoria(idCategoria);
            libroSeleccionado.setIdEditorial(idEditorial);

            // Query de actualización del libro
            String queryLibro = "UPDATE libro SET "
                    + "titulo = ?, "
                    + "descripcion = ?, "
                    + "ISBN = ?, "
                    + "totalCopias = ?, "
                    + "idCategoria = ?, "
                    + "idEditorial = ? "
                    + "WHERE idLibro = ?";

            try (PreparedStatement pstLibro = conexion.prepareStatement(queryLibro)) {
                pstLibro.setString(1, libroSeleccionado.getTitulo());
                pstLibro.setString(2, libroSeleccionado.getDescripcion());
                pstLibro.setString(3, libroSeleccionado.getISBN());
                pstLibro.setInt(4, libroSeleccionado.getTotalCopias());
                pstLibro.setInt(5, libroSeleccionado.getIdCategoria());
                pstLibro.setInt(6, libroSeleccionado.getIdEditorial());
                pstLibro.setInt(7, libroSeleccionado.getIdLibro());

                int filasAfectadasLibro = pstLibro.executeUpdate();

                if (filasAfectadasLibro > 0) {
                    int idAutorActual = obtenerIdAutorPorIdLibro(libroSeleccionado.getIdLibro());

                    if (idAutorActual != idAutorNuevo) {
                        // Actualizamos la tabla intermedia libro_Autor
                        String queryActualizarAutor = "UPDATE libro_Autor SET idAutor = ? WHERE idLibro = ?";
                        try (PreparedStatement pstAutor = conexion.prepareStatement(queryActualizarAutor)) {
                            pstAutor.setInt(1, idAutorNuevo);
                            pstAutor.setInt(2, libroSeleccionado.getIdLibro());

                            pstAutor.executeUpdate();
                        }
                    }

                    mostrarAlertaExito("Éxito", "Libro actualizado correctamente");

                    Stage stage = (Stage) btnAceptar.getScene().getWindow();
                    stage.close();

                    cMain.tbvLibros.setItems(cMain.listaTodosLibros());
                    cMain.tbvLibrosReserva.setItems(cMain.listaLibrosDisponibles());
                } else {
                    mostrarAlertaError("Error", "No se pudo actualizar el libro");
                }
            } catch (SQLException e) {
                mostrarAlertaError("Error de base de datos", e.getMessage());
            }

        } catch (NumberFormatException e) {
            mostrarAlertaError("Error numérico", "Verifique los campos numéricos");
        }
    }

    public int obtenerIdAutorPorIdLibro(int idLibro) {
        int idAutor = -1; // Valor por defecto si no encuentra nada

        if (conexion != null) {
            String query = "SELECT idAutor FROM libro_Autor WHERE idLibro = ? LIMIT 1";

            try (PreparedStatement ps = conexion.prepareStatement(query)) {
                ps.setInt(1, idLibro);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idAutor = rs.getInt("idAutor");
                    }
                }

            } catch (SQLException e) {
                System.out.println("Excepción SQL: " + e.getMessage());
            }
        }

        return idAutor;
    }

    
    private void rellenarDatos(){
        txtTitulo.setText(libroSeleccionado.getTitulo());
        txtISBN.setText(libroSeleccionado.getISBN());
        txtDescripcion.setText(libroSeleccionado.getDescripcion());
        txtTotalCopias.setText(String.valueOf(libroSeleccionado.getTotalCopias()));
        txtCategoriaElegida.setText(obtenerNombreCategoriaPorId(libroSeleccionado.getIdCategoria()));
        txtEditorialElegida.setText(obtenerNombreEditorialPorId(libroSeleccionado.getIdEditorial()));
        txtAutorElegido.setText(obtenerNombreAutorPorIdLibro(libroSeleccionado.getIdLibro()));
    }
    
    private void listarCategorias() {
        listaNombreCategoria.clear();

        if (conexion != null) {
            String query = "SELECT nombreCategoria FROM categoria";
            try {
                rs = st.executeQuery(query);
                while (rs.next()) {
                    listaNombreCategoria.add(rs.getString("nombreCategoria"));
                }
            } catch (SQLException e) {
                System.out.println("Excepción SQL: " + e.getMessage());
            }
        }
    }
    
    private int obtenerCopiasReservadas(int idLibro) throws SQLException {
        String query = "SELECT COUNT(*) AS totalReservadas FROM reserva WHERE idLibro = ?";
        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            pst.setInt(1, idLibro);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("totalReservadas");
                }
            }
        }
        return 0;
    }

    
    public String obtenerNombreCategoriaPorId(int idCategoria) {
        String nombreCategoria = null;

        if (conexion != null) {
            String query = "SELECT nombreCategoria FROM categoria WHERE idCategoria = ?";

            try {
                PreparedStatement ps = conexion.prepareStatement(query);
                ps.setInt(1, idCategoria);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    nombreCategoria = rs.getString("nombreCategoria");
                }

                rs.close();
                ps.close();

            } catch (SQLException e) {
                System.out.println("Excepción SQL: " + e.getMessage());
            }
        }

        return nombreCategoria;
    }
    
    public String obtenerNombreEditorialPorId(int idEditorial) {
        String nombreEditorial = null;

        if (conexion != null) {
            String query = "SELECT nombreEditorial FROM editorial WHERE idEditorial = ?";

            try {
                PreparedStatement ps = conexion.prepareStatement(query);
                ps.setInt(1, idEditorial);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    nombreEditorial = rs.getString("nombreEditorial");
                }

                rs.close();
                ps.close();

            } catch (SQLException e) {
                System.out.println("Excepción SQL: " + e.getMessage());
            }
        }

        return nombreEditorial;
    }
    
    public String obtenerNombreAutorPorIdLibro(int idLibro) {
        String nombreAutor = null;

        if (conexion != null) {
            String query = "SELECT a.nombreAutor, a.apellidoAutor " +
                           "FROM autor a " +
                           "JOIN libro_Autor la ON a.idAutor = la.idAutor " +
                           "WHERE la.idLibro = ? LIMIT 1";

            try {
                PreparedStatement ps = conexion.prepareStatement(query);
                ps.setInt(1, idLibro);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    nombreAutor = rs.getString("nombreAutor") + " " + rs.getString("apellidoAutor");
                }

                rs.close();
                ps.close();

            } catch (SQLException e) {
                System.out.println("Excepción SQL: " + e.getMessage());
            }
        }

        return nombreAutor;
    }



    
    public int obtenerIdCategoriaPorNombre(String nombreCategoria) {
        int idCategoria = -1;

        if (conexion != null) {
            String query = "SELECT idCategoria FROM categoria WHERE nombreCategoria = ?";

            try (PreparedStatement ps = conexion.prepareStatement(query)) {
                ps.setString(1, nombreCategoria);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idCategoria = rs.getInt("idCategoria");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error obteniendo ID categoría: " + e.getMessage());
            }
        }
        return idCategoria;
    }
    
    public int obtenerIdAutorPorNombreCompleto(String nombreCompleto) {
        int idAutor = -1;

        if (conexion != null && nombreCompleto != null && !nombreCompleto.isEmpty()) {
            // Separar nombre y apellido
            String[] partes = nombreCompleto.split(" ", 2);
            if (partes.length < 2) return -1; // Si no tiene ambos componentes

            String query = "SELECT idAutor FROM autor WHERE nombreAutor = ? AND apellidoAutor = ?";

            try (PreparedStatement ps = conexion.prepareStatement(query)) {
                ps.setString(1, partes[0]);    // Nombre
                ps.setString(2, partes[1]);    // Apellido

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idAutor = rs.getInt("idAutor");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error obteniendo ID autor: " + e.getMessage());
            }
        }
        return idAutor;
    }

    public int obtenerIdEditorialPorNombre(String nombreEditorial) {
        int idEditorial = -1; // Valor por defecto si no encuentra

        if (conexion != null) {
            String query = "SELECT idEditorial FROM editorial WHERE nombreEditorial = ?";

            try (PreparedStatement ps = conexion.prepareStatement(query)) {
                ps.setString(1, nombreEditorial);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idEditorial = rs.getInt("idEditorial");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error obteniendo ID editorial: " + e.getMessage());
            }
        }
        return idEditorial;
    }

    private void listarEditoriales() {
        listaNombreEditorial.clear();

        if (conexion != null) {
            String query = "SELECT nombreEditorial FROM editorial";
            try {
                rs = st.executeQuery(query);
                while (rs.next()) {
                    listaNombreEditorial.add(rs.getString("nombreEditorial"));
                }
            } catch (SQLException e) {
                System.out.println("Excepción SQL: " + e.getMessage());
            }
        }
    }
    
    private void listarAutores() {
        listaNombreAutores.clear();

        if (conexion != null) {
            String query = "SELECT CONCAT(nombreAutor, ' ', apellidoAutor) AS nombreCompleto FROM autor";
            try {
                rs = st.executeQuery(query);
                while (rs.next()) {
                    listaNombreAutores.add(rs.getString("nombreCompleto"));
                }
            } catch (SQLException e) {
                System.out.println("Excepción SQL: " + e.getMessage());
            }
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
