
package modelo;


public record OperacionEditorial(Modo modo, Editorial editorial) {
    // Validación básica (opcional)
    public OperacionEditorial {
        if (modo == Modo.ADD && editorial != null) {
            throw new IllegalArgumentException("En modo AÑADIR, la editorial debe ser null");
        }
        if ((modo == Modo.VER || modo == Modo.EDITAR) && editorial == null) {
            throw new IllegalArgumentException("En modo VER/EDITAR, la editorial no puede ser null");
        }
    }
}