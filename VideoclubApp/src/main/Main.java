package main;

import javax.swing.SwingUtilities;

import gui.VentanaDetallesPelicula;
import gui.VentanaLogin;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
		       VentanaDetallesPelicula ventana =
	                    new VentanaDetallesPelicula(2);
			 ventana.setVisible(true);
            
        });

	}

}
