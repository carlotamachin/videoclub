
package modelo;

import java.time.LocalDate;

public class Alquiler {

    private int id;
    private String cliente;
    private Pelicula pelicula;
    private LocalDate fechaAlquiler;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;
    private double precio;

    public Alquiler(int id, String cliente, Pelicula pelicula,
                    LocalDate fechaAlquiler, LocalDate fechaLimite,
                    double precio) {

        this.id = id;
        this.cliente = cliente;
        this.pelicula = pelicula;
        this.fechaAlquiler = fechaAlquiler;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = null;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public LocalDate getFechaAlquiler() {
        return fechaAlquiler;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public double getPrecio() {
        return precio;
    }

    public boolean isActivo() {
        return fechaDevolucion == null;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
}
