
package modelo;

public class Editorial {
    private int idEditorial;
    private String nombreEditorial;
    private String direccion;
    private String telefono;

    public Editorial(int idEditorial, String nombreEditorial, String direccion, String telefono) {
        this.idEditorial = idEditorial;
        this.nombreEditorial = nombreEditorial;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public int getIdEditorial() {
        return idEditorial;
    }

    public String getNombreEditorial() {
        return nombreEditorial;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setIdEditorial(int idEditorial) {
        this.idEditorial = idEditorial;
    }

    public void setNombreEditorial(String nombreEditorial) {
        this.nombreEditorial = nombreEditorial;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
}
