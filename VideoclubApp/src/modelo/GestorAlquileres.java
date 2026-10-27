
package modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class GestorAlquileres {

    private static ArrayList<Alquiler> alquileres = new ArrayList<>();

    private static int siguienteId = 1;

    public static ArrayList<Alquiler> getAlquileres() {
        return new ArrayList<>(alquileres);
    }

    public static ArrayList<Alquiler> getAlquileresActivos() {

        ArrayList<Alquiler> activos = new ArrayList<>();

        for (Alquiler alquiler : alquileres) {
            if (alquiler.isActivo()) {
                activos.add(alquiler);
            }
        }

        return activos;
    }

    public static boolean registrarAlquiler(String cliente, Pelicula pelicula) {

        if (cliente == null || cliente.trim().isEmpty()
                || pelicula == null || !pelicula.isDisponible()) {
            return false;
        }

        LocalDate fechaAlquiler = LocalDate.now();
        LocalDate fechaLimite = fechaAlquiler.plusDays(3);

        Alquiler alquiler = new Alquiler(
            siguienteId,
            cliente.trim(),
            pelicula,
            fechaAlquiler,
            fechaLimite,
            pelicula.getPrecio()
        );

        alquileres.add(alquiler);
        siguienteId++;

        pelicula.setDisponible(false);

        return true;
    }

    public static boolean registrarDevolucion(int idAlquiler) {

        for (Alquiler alquiler : alquileres) {

            if (alquiler.getId() == idAlquiler && alquiler.isActivo()) {

                alquiler.setFechaDevolucion(LocalDate.now());
                alquiler.getPelicula().setDisponible(true);

                return true;
            }
        }

        return false;
    }
}
