
package modelo;


public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String imagenUsuario;
    private String email;
    private String contraseña;
    private String telefono;
    private String direccion;
    private RolUsuario rol;

    public Usuario(int idUsuario, String nombre, String apellido,String imagen, String email, String contraseña, String telefono, String direccion, RolUsuario rol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombre;
        this.apellidoUsuario = apellido;
        this.imagenUsuario = imagen;
        this.email = email;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public String getImagenUsuario() {
        return imagenUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public void setImagenUsuario(String imagenUsuario) {
        this.imagenUsuario = imagenUsuario;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    
    
}
