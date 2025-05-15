
package modelo;


public class Libro {
    private int idLibro;
    private String titulo;
    private String descripcion;
    private String ISBN;
    private int totalCopias;
    private int copiasDisponibles;
    private int idCategoria;
    private int idEditorial;

    public Libro(int idLibro, String titulo, String descripcion, String ISBN, int totalCopias, int copiasDisponibles, int idCategoria, int idEditorial) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ISBN = ISBN;
        this.totalCopias = totalCopias;
        this.copiasDisponibles = copiasDisponibles;
        this.idCategoria = idCategoria;
        this.idEditorial = idEditorial;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getISBN() {
        return ISBN;
    }

    public int getTotalCopias() {
        return totalCopias;
    }

    public int getCopiasDisponibles() {
        return copiasDisponibles;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public int getIdEditorial() {
        return idEditorial;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setTotalCopias(int totalCopias) {
        this.totalCopias = totalCopias;
    }

    public void setCopiasDisponibles(int copiasDisponibles) {
        this.copiasDisponibles = copiasDisponibles;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setIdEditorial(int idEditorial) {
        this.idEditorial = idEditorial;
    }
    
    
    
}
