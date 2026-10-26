
package modelo;

import java.util.ArrayList;

public class GestorPeliculas {

    private static ArrayList<Pelicula> peliculas = new ArrayList<>();

    private static int siguienteId = 1;

    static {
        peliculas.add(new Pelicula(
            siguienteId++, "El Padrino", "Drama",
            1972, 175, 3.50, true));

        peliculas.add(new Pelicula(
            siguienteId++, "Interestelar", "Ciencia ficción",
            2014, 169, 3.00, true));

        peliculas.add(new Pelicula(
            siguienteId++, "El caballero oscuro", "Acción",
            2008, 152, 2.50, false));

        peliculas.add(new Pelicula(
            siguienteId++, "Coco", "Animación",
            2017, 105, 2.00, true));

        peliculas.add(new Pelicula(
            siguienteId++, "Titanic", "Romance",
            1997, 195, 2.50, true));

        peliculas.add(new Pelicula(
            siguienteId++, "Parásitos", "Suspense",
            2019, 132, 3.00, true));

        peliculas.add(new Pelicula(
            siguienteId++, "Gladiator", "Acción",
            2000, 155, 2.50, false));

        peliculas.add(new Pelicula(
            siguienteId++, "Toy Story", "Animación",
            1995, 81, 2.00, true));
    }

    public static ArrayList<Pelicula> getPeliculas() {
        return peliculas;
    }

    public static void anadirPelicula(Pelicula pelicula) {
        pelicula.setDisponible(true);
        peliculas.add(pelicula);
        siguienteId++;
    }

    public static void eliminarPelicula(Pelicula pelicula) {
        peliculas.remove(pelicula);
    }

    public static int getSiguienteId() {
        return siguienteId;
    }
}
