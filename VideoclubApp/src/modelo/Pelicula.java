
package modelo;

public class Pelicula {

    private int id;
    private String titulo;
    private String genero;
    private int anio;
    private int duracion;
    private double precio;
    private boolean disponible;

    public Pelicula(int id, String titulo, String genero,
                    int anio, int duracion, double precio,
                    boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.anio = anio;
        this.duracion = duracion;
        this.precio = precio;
        this.disponible = disponible;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public int getAnio() {
        return anio;
    }

    public int getDuracion() {
        return duracion;
    }

    public double getPrecio() {
        return precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
