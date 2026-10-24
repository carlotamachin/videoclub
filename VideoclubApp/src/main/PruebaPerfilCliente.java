package main;

import javax.swing.SwingUtilities;
import gui.VentanaPerfilCliente;

public class PruebaPerfilCliente {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            VentanaPerfilCliente ventana =
                    new VentanaPerfilCliente(1);

            ventana.setVisible(true);
        });
    }
}