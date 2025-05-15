
package modelo;

public class Autor {
    private int idAutor;
    private String nombreAutor;
    private String apellidosAutor;
    private String nacionalidad;
    private String imagenAutor;
    private String biografia;

    public Autor(int idAutor, String nombreAutor, String apellidosAutor, String nacionalidad, String imagenAutor, String biografia) {
        this.idAutor = idAutor;
        this.nombreAutor = nombreAutor;
        this.apellidosAutor = apellidosAutor;
        this.nacionalidad = nacionalidad;
        this.imagenAutor = imagenAutor;
        this.biografia = biografia;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public String getApellidosAutor() {
        return apellidosAutor;
    }

    public String getImagenAutor() {
        return imagenAutor;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public void setApellidosAutor(String apellidosAutor) {
        this.apellidosAutor = apellidosAutor;
    }

    public void setImagenAutor(String imagenAutor) {
        this.imagenAutor = imagenAutor;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    
    
    
}
