package main;

import javax.swing.SwingUtilities;

import gui.VentanaDetallesPelicula;
import gui.VentanaLogin;
import gui.VentanaPerfilCliente;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
		       VentanaDetallesPelicula ventana =
	                    new VentanaDetallesPelicula(1);
			 ventana.setVisible(true);
            
        });

	}

}
