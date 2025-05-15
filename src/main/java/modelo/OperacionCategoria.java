
package modelo;


public record OperacionCategoria(Modo modo, Categoria categoria) {
    // Validación básica (opcional)
    public OperacionCategoria {
        if (modo == Modo.ADD && categoria != null) {
            throw new IllegalArgumentException("En modo AÑADIR, la categoria debe ser null");
        }
        if ((modo == Modo.VER || modo == Modo.EDITAR) && categoria == null) {
            throw new IllegalArgumentException("En modo VER/EDITAR, la categoria no puede ser null");
        }
    }
}