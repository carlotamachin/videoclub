package main;

import javax.swing.SwingUtilities;

import gui.VentanaCatalogo;
import gui.VentanaDetallesPelicula;
import gui.VentanaLogin;
import gui.VentanaPerfilCliente;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
		       VentanaCatalogo ventana =
	                    new VentanaCatalogo(1);
			 ventana.setVisible(true);
            
        });

	}

}
