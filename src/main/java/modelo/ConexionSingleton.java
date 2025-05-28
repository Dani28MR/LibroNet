
package modelo;

import javafx.scene.control.Alert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.System.exit;


public class ConexionSingleton {

    private static Connection conexion;

    public static Connection obtenerConexion() {
        if(conexion==null){
            try{
                conexion = getConnection();
                System.out.println("Conexión establecida");
            }catch (IOException e){
                System.out.println("No se pudo conectar");
            }
        }
        return conexion;
    }

    private static Connection getConnection() throws IOException {
        Properties properties = new Properties();
        String IP, PORT, BBDD, USER, PWD;
        try {
            InputStream input_ip = new FileInputStream("ip.properties");
            properties.load(input_ip);
            IP = (String) properties.get("IP");
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo encontrar el archivo de propiedades para IP, se establece localhost por defecto");
            IP = "34.237.81.204";
        }

        InputStream input = ConexionSingleton.class.getClassLoader().getResourceAsStream("bbdd.properties");
        if (input == null) {
            System.out.println("No se pudo encontrar el archivo de propiedades");
            return null;
        } else {
            properties.load(input);
            PORT = properties.getProperty("PORT", "3306");
            BBDD = properties.getProperty("BBDD", "LibroNet");
            USER = properties.getProperty("USER", "admin");
            PWD = properties.getProperty("PWD", "a1u4HcLMHupH");

            
            Connection conn;
            try {
                String cadconex = "jdbc:mariadb://" + IP + ":" + PORT + "/" + BBDD + " USER:" + USER + "PWD:" + PWD;
                System.out.println(cadconex);
                conn = DriverManager.getConnection("jdbc:mariadb://" + IP + ":" + PORT + "/" + BBDD, USER, PWD);
                return conn;
            } catch (SQLException e) {
                System.out.println("Error SQL: " + e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Ha ocurrido un error de conexión");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                exit(0);
                return null;
            }
        }
    }

}
