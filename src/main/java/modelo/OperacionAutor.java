package modelo;


public record OperacionAutor(Modo modo, Autor autor) {
    // Validación básica (opcional)
    public OperacionAutor {
        if (modo == Modo.ADD && autor != null) {
            throw new IllegalArgumentException("En modo AÑADIR, el autor debe ser null");
        }
        if ((modo == Modo.VER || modo == Modo.EDITAR) && autor == null) {
            throw new IllegalArgumentException("En modo VER/EDITAR, el autor no puede ser null");
        }
    }
}
