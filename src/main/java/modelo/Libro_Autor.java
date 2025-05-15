
package modelo;

public class Libro_Autor {
    private int idLibro;
    private int idAutor;

    public Libro_Autor(int idLibro, int idAutor) {
        this.idLibro = idLibro;
        this.idAutor = idAutor;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }
    
    
    
}
