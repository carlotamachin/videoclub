package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class VentanaRegistro extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel mainPanel;
	private JTextField txtNombre;
	private JTextField txtApellidos;
	private JTextField txtEmail;
	private JTextField txtUsuario;
	private JPasswordField txtPassword;
	private JPasswordField txtRepetirPassword;

	public VentanaRegistro() {
		this.setTitle("Videoclub - Registro");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.BLACK);

		cargarComponentes();

		this.add(mainPanel);
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void cargarComponentes() {
		// panel norte
		JPanel panelLogo = new JPanel();
		panelLogo.setBackground(Color.BLACK);

		JLabel lblLogo = new JLabel();
		ImageIcon logo = new ImageIcon("resources/images/logo_videoclub.png");
		lblLogo.setIcon(new ImageIcon(logo.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH)));

		panelLogo.add(lblLogo);
		mainPanel.add(panelLogo, BorderLayout.NORTH);

		// panel central
		JPanel panelCampos = new JPanel(new GridLayout(7, 1, 0, 10));
		panelCampos.setBackground(Color.BLACK);

		// nombre
		JPanel panelNombre = new JPanel();
		panelNombre.setBackground(Color.BLACK);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setPreferredSize(new Dimension(120,25));

		txtNombre = new JTextField(20);

		panelNombre.add(lblNombre);
		panelNombre.add(txtNombre);

		panelCampos.add(panelNombre);

		// apellidos
		JPanel panelApellidos = new JPanel();
		panelApellidos.setBackground(Color.BLACK);

		JLabel lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setForeground(Color.WHITE);
		lblApellidos.setPreferredSize(new Dimension(120,25));

		txtApellidos = new JTextField(20);

		panelApellidos.add(lblApellidos);
		panelApellidos.add(txtApellidos);

		panelCampos.add(panelApellidos);

		// correo
		JPanel panelEmail = new JPanel();
		panelEmail.setBackground(Color.BLACK);

		JLabel lblEmail = new JLabel("Correo:");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setPreferredSize(new Dimension(120,25));

		txtEmail = new JTextField(20);

		panelEmail.add(lblEmail);
		panelEmail.add(txtEmail);

		panelCampos.add(panelEmail);

		// usuario
		JPanel panelUsuario = new JPanel();
		panelUsuario.setBackground(Color.BLACK);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setForeground(Color.WHITE);
		lblUsuario.setPreferredSize(new Dimension(120,25));

		txtUsuario = new JTextField(20);

		panelUsuario.add(lblUsuario);
		panelUsuario.add(txtUsuario);

		panelCampos.add(panelUsuario);

		// contraseña
		JPanel panelPassword = new JPanel();
		panelPassword.setBackground(Color.BLACK);
		
		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setPreferredSize(new Dimension(120,25));
		
		txtPassword = new JPasswordField(20);
		
		panelPassword.add(lblPassword);
		panelPassword.add(txtPassword);

		panelCampos.add(panelPassword);
		
		//repetir contraseña
		JPanel panelRepetirPassword = new JPanel();
		panelRepetirPassword.setBackground(Color.BLACK);

		JLabel lblRepetirPassword = new JLabel("Repetir contraseña:");
		lblRepetirPassword.setForeground(Color.WHITE);
		lblRepetirPassword.setPreferredSize(new Dimension(120,25));

		txtRepetirPassword = new JPasswordField(20);

		panelRepetirPassword.add(lblRepetirPassword);
		panelRepetirPassword.add(txtRepetirPassword);

		panelCampos.add(panelRepetirPassword);

		
		//boton registrarse
		JPanel panelBotonRegistro = new JPanel();
		panelBotonRegistro.setBackground(Color.BLACK);

		JButton btnRegistro = new JButton("Registrarse");
		btnRegistro.setBackground(new Color(255, 195, 40));
		
		btnRegistro.addActionListener(e->{
			if(txtNombre.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Introduce tu nombre.");
			}
			else if(txtApellidos.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Introduce tus apellidos.");
			}
			else if(txtEmail.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Introduce tu correo.");
				
			}else if(txtUsuario.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Introduce tu nombre de usuario.");
			}
			else if(txtPassword.getPassword().length == 0) {
				JOptionPane.showMessageDialog(this, "Introduce tu contraseña.");
			}else if(txtRepetirPassword.getPassword().length == 0) {
				JOptionPane.showMessageDialog(this, "Repite tu contraseña.");
			}
			else if(!new String(txtPassword.getPassword()).equals(new String(txtRepetirPassword.getPassword()))){
				JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
			}
			else {
				//falta conectar la BD para registrar el nuevo cliente
			}
		});
		
		panelBotonRegistro.add(btnRegistro);

		panelCampos.add(panelBotonRegistro);

		mainPanel.add(panelCampos, BorderLayout.CENTER);
		
		//panel sur para volver al login
		JPanel panelLogin = new JPanel(new GridLayout(2, 1, 5, 5));
		panelLogin.setBackground(Color.BLACK);

		JLabel lblLogin = new JLabel(
		    "¿Ya tienes cuenta?", SwingConstants.CENTER
		);
		lblLogin.setForeground(Color.GRAY);
		panelLogin.add(lblLogin);

		JButton btnLogin = new JButton("Iniciar sesión");
		btnLogin.setBackground(new Color(220, 35, 40));
		btnLogin.setForeground(Color.WHITE);

		btnLogin.addActionListener(e -> {
		    new VentanaLogin();
		    dispose();
		});

		JPanel panelBotonLogin = new JPanel();
		panelBotonLogin.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelBotonLogin.setBackground(Color.BLACK);
		panelBotonLogin.add(btnLogin);

		panelLogin.add(panelBotonLogin);

		mainPanel.add(panelLogin, BorderLayout.SOUTH);
	}

}
