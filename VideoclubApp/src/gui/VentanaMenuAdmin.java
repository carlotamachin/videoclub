package gui;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.*;
import javax.swing.SwingUtilities;

public class VentanaMenuAdmin extends JFrame{

	private static final long serialVersionUID = 1L;
	JPanel mainPanel;

	public VentanaMenuAdmin() {
		setTitle("Videoclub - Menú de administrador");
		this.setSize(900, 600);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        cargarComponentes();

        add(mainPanel);
        
        setVisible(true);
	}

	private void cargarComponentes() {
		
		//panel superior
		JPanel panelSuperior = new JPanel();
		panelSuperior.setLayout(new BorderLayout());
		panelSuperior.setBackground(Color.BLACK);
		
		//parte de la izquierda
		JPanel panelIzquierda = new JPanel();
		panelIzquierda.setBackground(Color.BLACK);
		
		JLabel lblLogo = new JLabel();
		ImageIcon logo = cargarImagen("resources/images/logo_videoclub.png", new Dimension(60,60));
		lblLogo.setIcon(logo);
		
		JLabel lblSaludo = new JLabel("Hola, administrador");
		lblSaludo.setForeground(Color.WHITE);

		panelIzquierda.add(lblLogo);
		panelIzquierda.add(lblSaludo);

		panelSuperior.add(panelIzquierda, BorderLayout.WEST);
		
		//parte de la derecha
		JPanel panelDerecha = new JPanel();
		panelDerecha.setBackground(Color.BLACK);

		//boton de gestion de clientes
		JButton btnClientes = new JButton("Clientes");
		btnClientes.setBackground(new Color(255, 195, 40));
		btnClientes.setForeground(Color.BLACK);
		
		btnClientes.addActionListener(e -> {
		    new VentanaClientes();
		    dispose();
		});
		
		//boton de gestion de peliculas
		JButton btnPeliculas = new JButton("Películas");
		btnPeliculas.setBackground(new Color(255, 195, 40));
		btnPeliculas.setForeground(Color.BLACK);
		
		//boton de gestion de devoluciones
		JButton btnDevoluciones = new JButton("Devoluciones");
		btnDevoluciones.setBackground(new Color(220, 35, 40));
		btnDevoluciones.setForeground(Color.WHITE);

		panelDerecha.add(btnClientes);
		panelDerecha.add(btnPeliculas);
		panelDerecha.add(btnDevoluciones);

		panelSuperior.add(panelDerecha, BorderLayout.EAST);
		
		mainPanel.add(panelSuperior, BorderLayout.NORTH);
		
		//bienvenida
		JPanel panelBienvenida = new JPanel();
		panelBienvenida.setBackground(Color.BLACK);

		JLabel lblBienvenida = new JLabel("Bienvenido al menú de administración");
		lblBienvenida.setForeground(Color.WHITE);

		panelBienvenida.add(lblBienvenida);

		mainPanel.add(panelBienvenida, BorderLayout.CENTER);
		
	}
	
	private ImageIcon cargarImagen(String imagen, Dimension dimension) {
	    ImageIcon imageIcon = new ImageIcon(imagen);

	    Image img = imageIcon.getImage();

	    Image newimg = img.getScaledInstance((int) dimension.getWidth(),(int) dimension.getHeight(), Image.SCALE_SMOOTH);

	    return new ImageIcon(newimg);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(()->{
			new VentanaMenuAdmin();
		});
	}

}
