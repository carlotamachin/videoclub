package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.*;

public class VentanaLogin extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField txtUsuario;
	private JPasswordField txtPassword;

	public VentanaLogin() {
		this.setTitle("Videoclub - Inicio de sesión");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.BLACK);
		
		
		//panel norte
		JPanel panelLogo = new JPanel();
		panelLogo.setBackground(Color.BLACK);
		
		JLabel lblLogo = new JLabel();
		ImageIcon logo = new ImageIcon("resources/images/logo_videoclub.png");
		lblLogo.setIcon(new ImageIcon(logo.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH)));
		
		panelLogo.add(lblLogo);
		mainPanel.add(panelLogo, BorderLayout.NORTH);

		// panel central
		JPanel panelCampos = new JPanel();
		panelCampos.setLayout(new GridLayout(3, 1, 10, 10));
		panelCampos.setBackground(Color.BLACK);

		// usuario
		JPanel panelUsuario = new JPanel(new FlowLayout());
		panelUsuario.setBackground(Color.BLACK);

		JLabel lblUsuario = new JLabel("Usuario: ");
		lblUsuario.setForeground(Color.WHITE);
		
		txtUsuario = new JTextField(20);
		
		panelUsuario.add(lblUsuario);
		panelUsuario.add(txtUsuario);
		
		panelCampos.add(panelUsuario);

		// contraseña
		JPanel panelPassword = new JPanel(new FlowLayout());
		panelPassword.setBackground(Color.BLACK);

		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setForeground(Color.WHITE);

		txtPassword = new JPasswordField(20);

		panelPassword.add(lblPassword);
		panelPassword.add(txtPassword);

		
		panelCampos.add(panelPassword);
		
		//boton iniciar sesion
		JPanel panelBotonSesion = new JPanel();
		panelBotonSesion.setBackground(Color.BLACK);
		
		JButton btnLogin = new JButton("Iniciar sesión");
		panelBotonSesion.add(btnLogin);
		
		panelCampos.add(panelBotonSesion);

		mainPanel.add(panelCampos, BorderLayout.CENTER);
		
		//panel sur de registro
		JPanel panelRegistro = new JPanel(new GridLayout(2,1,5,5));
		panelRegistro.setBackground(Color.BLACK);
		
		JLabel lblRegistro = new JLabel("¿Aún no tienes cuenta?", SwingConstants.CENTER);
		lblRegistro.setForeground(Color.GRAY);
		panelRegistro.add(lblRegistro);
		
		JButton btnRegistro = new JButton("Registrarse");
		JPanel panelBotonRegistro = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelBotonRegistro.setBackground(Color.BLACK);
		panelBotonRegistro.add(btnRegistro);

		panelRegistro.add(panelBotonRegistro);
		
		mainPanel.add(panelRegistro,BorderLayout.SOUTH);
		

		this.add(mainPanel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

}
