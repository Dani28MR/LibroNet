
package modelo;

import java.time.LocalDate;

public class Reserva {
    private int idReserva;
    private int idUsuario;
    private int idLibro;
    private LocalDate fechaReserva;
    private LocalDate fechaExpiracion;
    private EstadoReserva estado;

    public Reserva(int idReserva, int idUsuario, int idLibro, LocalDate fechaReserva, LocalDate fechaExpiracion, EstadoReserva estado) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
        this.fechaReserva = fechaReserva;
        this.fechaExpiracion = fechaExpiracion;
        this.estado = estado;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }
    
    
    
}
