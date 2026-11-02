package main;

import javax.swing.SwingUtilities;
import gui.VentanaHistorialAlquileres;

public class PruebaHistorialAlquileres {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            VentanaHistorialAlquileres ventana =
                    new VentanaHistorialAlquileres(1);

            ventana.setVisible(true);
        });
    }
}