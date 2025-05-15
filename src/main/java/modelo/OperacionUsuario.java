
package modelo;

public record OperacionUsuario(Modo modo, Usuario usuario) {
    // Validación básica (opcional)
    public OperacionUsuario {
        if (modo == Modo.ADD && usuario != null) {
            throw new IllegalArgumentException("En modo AÑADIR, el usuario debe ser null");
        }
        if ((modo == Modo.VER || modo == Modo.EDITAR) && usuario == null) {
            throw new IllegalArgumentException("En modo VER/EDITAR, el usuario no puede ser null");
        }
    }
}