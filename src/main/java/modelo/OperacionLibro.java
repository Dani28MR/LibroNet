
package modelo;


public record OperacionLibro(Modo modo, Libro libro) {
    // Validación básica (opcional)
    public OperacionLibro {
        if (modo == Modo.ADD && libro != null) {
            throw new IllegalArgumentException("En modo AÑADIR, el libro debe ser null");
        }
        if ((modo == Modo.VER || modo == Modo.EDITAR) && libro == null) {
            throw new IllegalArgumentException("En modo VER/EDITAR, el libro no puede ser null");
        }
    }
}
