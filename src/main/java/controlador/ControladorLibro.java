
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
    private Label txtCategoriaElegida;

    @FXML
    private TextField txtCategoriaIntroducida;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private Label txtEditorialElegida;

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
    FilteredList<String> filteredCategoria;
    FilteredList<String> filterNombres;
    
    Connection conexion;
    Statement st;
    ResultSet rs;
    private ControladorMain cMain;
    private OperacionLibro operacion;
    Libro libroSeleccionado;

    
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
        FilteredList<String> filteredEditorial = new FilteredList<>(listaNombreEditorial);
        lstEditoriales.setItems(filteredEditorial);

        txtEditorialIntroducida.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredEditorial.setPredicate(item ->
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
    }
    

    public void initDatos() {
        try {
            conexion = cMain.dameConnection();
            if (conexion != null) {
                this.st = conexion.createStatement();
                listarCategorias();
                listarEditoriales();
                
                
                
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
                editarLibro();
            }
        }
        
        cerrarVentana(event);
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
        btnAceptar.setVisible(false);
        lstCategorias.setDisable(true);
        lstEditoriales.setDisable(true);
    }
    private void edicion(){
        rellenarDatos();
        txtTitulo.setEditable(false);
        txtISBN.setEditable(false);
    }
    
    private void crearNuevoLibro() {
        try {
            // Validar campos numéricos
            int totalCopias = Integer.parseInt(txtTotalCopias.getText());
            int idCategoria = obtenerIdCategoriaPorNombre(txtCategoriaElegida.getText());
            int idEditorial = obtenerIdEditorialPorNombre(txtEditorialElegida.getText());

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
    }

    // Métodos auxiliares
    public void mostrarAlertaExito(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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
            // Validar que hay un libro seleccionado
            if (libroSeleccionado == null) {
                mostrarAlertaError("Error", "No hay ningún libro seleccionado");
                return;
            }

            // Validar campos numéricos
            int totalCopias = Integer.parseInt(txtTotalCopias.getText());
            int idCategoria = obtenerIdCategoriaPorNombre(txtCategoriaElegida.getText());
            int idEditorial = obtenerIdEditorialPorNombre(txtEditorialElegida.getText());

            
            // Obtener copias ya reservadas
            int copiasReservadas = obtenerCopiasReservadas(libroSeleccionado.getIdLibro());

            // Validar que no se reduzca por debajo de las copias reservadas
            if (totalCopias < copiasReservadas) {
                mostrarAlertaError("Error de validación",
                    "No se puede establecer un número de copias menor a las ya reservadas (" + copiasReservadas + ").");
                return;
            }
            
            // Actualizar el objeto libro
            libroSeleccionado.setTitulo(txtTitulo.getText().trim());
            libroSeleccionado.setDescripcion(txtDescripcion.getText().trim());
            libroSeleccionado.setISBN(txtISBN.getText().trim());
            libroSeleccionado.setTotalCopias(totalCopias);
            libroSeleccionado.setIdCategoria(idCategoria);
            libroSeleccionado.setIdEditorial(idEditorial);

            // Query de actualización
            String query = "UPDATE libro SET "
                    + "titulo = ?, "
                    + "descripcion = ?, "
                    + "ISBN = ?, "
                    + "totalCopias = ?, "
                    + "idCategoria = ?, "
                    + "idEditorial = ? "
                    + "WHERE idLibro = ?";

            try (PreparedStatement pst = conexion.prepareStatement(query)) {

                // Setear parámetros
                pst.setString(1, libroSeleccionado.getTitulo());
                pst.setString(2, libroSeleccionado.getDescripcion());
                pst.setString(3, libroSeleccionado.getISBN());
                pst.setInt(4, libroSeleccionado.getTotalCopias());
                pst.setInt(5, libroSeleccionado.getIdCategoria());
                pst.setInt(6, libroSeleccionado.getIdEditorial());
                pst.setInt(7, libroSeleccionado.getIdLibro());

                int filasAfectadas = pst.executeUpdate();

                if (filasAfectadas > 0) {
                    mostrarAlertaExito("Éxito", "Libro actualizado correctamente");

                    // Cerrar ventana de edición
                    Stage stage = (Stage) btnAceptar.getScene().getWindow();
                    stage.close();

                    // Actualizar tabla principal
                    cMain.tbvLibros.setItems(cMain.listaTodosLibros());
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
    
    private void rellenarDatos(){
        txtTitulo.setText(libroSeleccionado.getTitulo());
        txtISBN.setText(libroSeleccionado.getISBN());
        txtDescripcion.setText(libroSeleccionado.getDescripcion());
        txtTotalCopias.setText(String.valueOf(libroSeleccionado.getTotalCopias()));
        txtCategoriaElegida.setText(obtenerNombreCategoriaPorId(libroSeleccionado.getIdCategoria()));
        txtEditorialElegida.setText(obtenerNombreEditorialPorId(libroSeleccionado.getIdEditorial()));
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
    
    public int obtenerIdCategoriaPorNombre(String nombreCategoria) {
        int idCategoria = -1; // Valor por defecto si no encuentra

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
    

   
    private void cerrarVentana(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        stage.setResizable(false);
        stage.close();
        cMain.resetearBotonesLibros();
    }
    
}
