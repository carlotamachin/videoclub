package gui;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class VentanaClientes extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField txtBusqueda;
	private JTable tablaClientes;
	private DefaultTableModel modeloClientes;
	private TableRowSorter<DefaultTableModel> sorterClientes;
	Object[][] ejemploDatos = {
		    {1, "Ana", "García", "ana@gmail.com", "ana"},
		    {2, "Luis", "Pérez", "luis@gmail.com", "luis"}
		};

	public VentanaClientes() {
		setTitle("Videoclub - Gestión de clientes");
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
		//panel norte: buscador
		JPanel panelBusqueda = new JPanel();
		panelBusqueda.setBackground(Color.BLACK);
		
		JLabel lblBusqueda = new JLabel("Buscar cliente:");
		lblBusqueda.setForeground(Color.WHITE);
		
		txtBusqueda = new JTextField(25);
		
		panelBusqueda.add(lblBusqueda);
		panelBusqueda.add(txtBusqueda);
		
		// panel superior con el menu
		JPanel panelSuperior = new JPanel(new BorderLayout());
		panelSuperior.setBackground(Color.BLACK);

		// parte izquierda
		JPanel panelIzquierda = new JPanel();
		panelIzquierda.setBackground(Color.BLACK);

		JLabel lblLogo = new JLabel();
		ImageIcon logo = cargarImagen("resources/images/logo_videoclub.png",new Dimension(60, 60));
		lblLogo.setIcon(logo);

		JLabel lblSaludo = new JLabel("Hola, administrador");
		lblSaludo.setForeground(Color.WHITE);

		panelIzquierda.add(lblLogo);
		panelIzquierda.add(lblSaludo);
		panelSuperior.add(panelIzquierda, BorderLayout.WEST);

		// parte derecha
		JPanel panelDerecha = new JPanel();
		panelDerecha.setBackground(Color.BLACK);

		JButton btnClientes = new JButton("Clientes");
		btnClientes.setBackground(new Color(255, 195, 40));
		btnClientes.setForeground(Color.BLACK);

		JButton btnPeliculas = new JButton("Películas");
		btnPeliculas.setBackground(new Color(255, 195, 40));
		btnPeliculas.setForeground(Color.BLACK);

		JButton btnDevoluciones = new JButton("Devoluciones");
		btnDevoluciones.setBackground(new Color(220, 35, 40));
		btnDevoluciones.setForeground(Color.WHITE);

		panelDerecha.add(btnClientes);
		panelDerecha.add(btnPeliculas);
		panelDerecha.add(btnDevoluciones);
		panelSuperior.add(panelDerecha, BorderLayout.EAST);

		JPanel panelNorte = new JPanel(new BorderLayout());
		panelNorte.add(panelSuperior, BorderLayout.NORTH);
		panelNorte.add(panelBusqueda, BorderLayout.CENTER);

		mainPanel.add(panelNorte, BorderLayout.NORTH);
		
		//panel centro con tabla de clientes
		String[] columnas = {
				"ID", "Nombre", "Apellidos", "Correo", "Usuario"
		};
		
		modeloClientes = new DefaultTableModel(ejemploDatos, columnas) {
		    @Override
		    public boolean isCellEditable(int fila, int columna) {
		        return false;
		    }
		};

		tablaClientes = new JTable(modeloClientes);
		tablaClientes.setSelectionMode(
		        ListSelectionModel.SINGLE_SELECTION);

		tablaClientes.setRowHeight(30);
		
		//renderer
		tablaClientes.setDefaultRenderer(Object.class,new FilaRenderer());

		JScrollPane scrollTabla = new JScrollPane(tablaClientes);
		mainPanel.add(scrollTabla, BorderLayout.CENTER);
		
		sorterClientes = new TableRowSorter<DefaultTableModel>(modeloClientes);
		tablaClientes.setRowSorter(sorterClientes);
		
		txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				filtrarClientes();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				filtrarClientes();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				filtrarClientes();
			}
		});
		
		//panel sur con botones de crear, modificar y eliminar
		JPanel panelBotones = new JPanel();
		panelBotones.setBackground(Color.BLACK);

		JButton btnModificar = new JButton("Modificar cliente");
		JButton btnEliminar = new JButton("Eliminar cliente");
		
		btnModificar.setBackground(new Color(255, 195, 40));
		
		btnEliminar.setBackground(new Color(220, 35, 40));
		btnEliminar.setForeground(Color.WHITE);
		
		//action listener del boton modificar
		btnModificar.addActionListener(e->{
			int fila = tablaClientes.getSelectedRow();
			
			if(fila == -1) {
				JOptionPane.showMessageDialog(this, "Selecciona un cliente para modificarlo.");
			}
			else {
				int filaModelo = tablaClientes.convertRowIndexToModel(fila);
				
				
				//JOptionPane para editar los datos
				JTextField txtNombre = new JTextField(
					    String.valueOf(modeloClientes.getValueAt(filaModelo, 1))
					);

					JTextField txtApellidos = new JTextField(
					    String.valueOf(modeloClientes.getValueAt(filaModelo, 2))
					);

					JTextField txtCorreo = new JTextField(
					    String.valueOf(modeloClientes.getValueAt(filaModelo, 3))
					);

					JTextField txtUsuario = new JTextField(
					    String.valueOf(modeloClientes.getValueAt(filaModelo, 4))
					);

					JPanel panelEditar = new JPanel(new GridLayout(4, 2, 10, 10));

					panelEditar.add(new JLabel("Nombre:"));
					panelEditar.add(txtNombre);

					panelEditar.add(new JLabel("Apellidos:"));
					panelEditar.add(txtApellidos);

					panelEditar.add(new JLabel("Correo:"));
					panelEditar.add(txtCorreo);

					panelEditar.add(new JLabel("Usuario:"));
					panelEditar.add(txtUsuario);

					int respuesta = JOptionPane.showConfirmDialog(
					    this,
					    panelEditar,
					    "Modificar cliente",
					    JOptionPane.OK_CANCEL_OPTION
					);
					
					if(respuesta == JOptionPane.OK_OPTION) {
						if(txtNombre.getText().isEmpty() || txtApellidos.getText().isEmpty()|| txtCorreo.getText().isEmpty() ||txtUsuario.getText().isEmpty()) {
							JOptionPane.showMessageDialog(this, "Debes completar todos los campos.");
						}else {
							modeloClientes.setValueAt(txtNombre.getText(), filaModelo, 1);
							modeloClientes.setValueAt(txtApellidos.getText(), filaModelo, 2);
							modeloClientes.setValueAt(txtCorreo.getText(), filaModelo, 3);
							modeloClientes.setValueAt(txtUsuario.getText(), filaModelo, 4);
						}
					}
			}
		});
		
		//action listener del boton eliminar
		btnEliminar.addActionListener(e->{
			int fila = tablaClientes.getSelectedRow();
			
			if(fila == -1) {
				JOptionPane.showMessageDialog(this, "Selecciona un cliente para eliminarlo.");
			}
			else {
				int respuesta = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres eliminar este cliente?", "EliminarCliente", JOptionPane.YES_NO_OPTION);
				
				if(respuesta == JOptionPane.YES_OPTION) {
					int filaModelo = tablaClientes.convertRowIndexToModel(fila);
					modeloClientes.removeRow(filaModelo);
				}
			}
		});
		
		panelBotones.add(btnModificar);
		panelBotones.add(btnEliminar);

		mainPanel.add(panelBotones, BorderLayout.SOUTH);
		
		
	}
	
	private void filtrarClientes() {
		String texto = txtBusqueda.getText();
		
		if(texto.isEmpty()) {
			sorterClientes.setRowFilter(null);
		}else {
			//buscar por nombre o apellido
			sorterClientes.setRowFilter(RowFilter.regexFilter("(?i)" +texto, 1,2));
		}
	}
	private ImageIcon cargarImagen(String imagen, Dimension dimension) {
	    ImageIcon imageIcon = new ImageIcon(imagen);

	    Image img = imageIcon.getImage();

	    Image newimg = img.getScaledInstance((int) dimension.getWidth(),(int) dimension.getHeight(), Image.SCALE_SMOOTH);

	    return new ImageIcon(newimg);
	}
	
public static void main(String[] args) {
	SwingUtilities.invokeLater(()->{
		new VentanaClientes();
	});
}
}




