
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import main.Conexion;

public class VentanaAlquileres extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel mainPanel;
    private JComboBox<String> cmbCliente;
    private JComboBox<String> cmbPelicula;
    private JTextField txtFecha;
    private JTextField txtFechaLimite;
    private JTextField txtPrecio;

    // Guardar los identificadores y precios de la base de datos
    private HashMap<String, Integer> clientes = new HashMap<>();
    private HashMap<String, Integer> peliculas = new HashMap<>();
    private HashMap<String, Double> precios = new HashMap<>();

    public VentanaAlquileres() {

        setTitle("Videoclub - Gestión de alquileres");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBackground(Color.BLACK);

        cargarComponentes();

        add(mainPanel);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarComponentes() {

        // Panel superior: logo y título
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.BLACK);

        JLabel lblLogo = new JLabel();
        ImageIcon logo = new ImageIcon(
            "resources/images/logo_videoclub.png"
        );

        if (logo.getIconWidth() > 0) {
            lblLogo.setIcon(new ImageIcon(
                logo.getImage().getScaledInstance(
                    150, -1, Image.SCALE_SMOOTH
                )
            ));
        }

        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        panelSuperior.add(lblLogo, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel(
            "GESTIÓN DE ALQUILERES",
            SwingConstants.CENTER
        );
        lblTitulo.setForeground(Color.WHITE);

        panelSuperior.add(lblTitulo, BorderLayout.SOUTH);
        mainPanel.add(panelSuperior, BorderLayout.NORTH);

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout(10, 15));
        panelCentral.setBackground(Color.BLACK);

        // Formulario centrado
        JPanel panelContenedor = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 10, 10)
        );
        panelContenedor.setBackground(Color.BLACK);

        JPanel panelFormulario = new JPanel(
            new GridLayout(5, 2, 15, 12)
        );
        panelFormulario.setBackground(Color.BLACK);

        // Desplegables de clientes y películas
        cmbCliente = new JComboBox<>();
        cmbCliente.addItem("Selecciona un cliente");

        cmbPelicula = new JComboBox<>();
        cmbPelicula.addItem("Selecciona una película");

        // Cargar datos reales de la base de datos
        cargarClientes();
        cargarPeliculas();

        txtFecha = new JTextField(LocalDate.now().toString(), 20);
        txtFecha.setEditable(false);

        txtFechaLimite = new JTextField(
            LocalDate.now().plusDays(3).toString(), 20
        );
        txtFechaLimite.setEditable(false);

        txtPrecio = new JTextField(20);
        txtPrecio.setEditable(false);

        panelFormulario.add(crearEtiqueta("Cliente:"));
        panelFormulario.add(cmbCliente);

        panelFormulario.add(crearEtiqueta("Película:"));
        panelFormulario.add(cmbPelicula);

        panelFormulario.add(crearEtiqueta("Fecha de alquiler:"));
        panelFormulario.add(txtFecha);

        panelFormulario.add(crearEtiqueta("Fecha límite:"));
        panelFormulario.add(txtFechaLimite);

        panelFormulario.add(crearEtiqueta("Precio (€):"));
        panelFormulario.add(txtPrecio);

        panelContenedor.add(panelFormulario);
        panelCentral.add(panelContenedor, BorderLayout.NORTH);

        // Mostrar precio de la película seleccionada
        cmbPelicula.addActionListener(e -> actualizarPrecio());

        // Tabla de alquileres activos
        String[] columnas = {
            "ID", "Cliente", "Película",
            "Fecha alquiler", "Fecha límite", "Precio (€)"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        JTable tablaAlquileres = new JTable(modelo);
        tablaAlquileres.setRowHeight(28);

        // Datos de ejemplo para esta primera entrega
        modelo.addRow(new Object[] {
            1, "María", "Titanic",
            "2026-10-26", "2026-10-29", 2.50
        });

        modelo.addRow(new Object[] {
            2, "Carlos", "Coco",
            "2026-10-27", "2026-10-30", 2.00
        });

        JScrollPane scrollTabla = new JScrollPane(tablaAlquileres);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);

        mainPanel.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior: botón registrar
        JPanel panelBotones = new JPanel(
            new FlowLayout(FlowLayout.CENTER)
        );
        panelBotones.setBackground(Color.BLACK);

        JButton btnRegistrar = new JButton("Registrar alquiler");
        btnRegistrar.setBackground(new Color(255, 195, 40));

        btnRegistrar.addActionListener(e -> registrarAlquiler());

        panelBotones.add(btnRegistrar);
        mainPanel.add(panelBotones, BorderLayout.SOUTH);
    }

    private JLabel crearEtiqueta(String texto) {

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(Color.WHITE);
        etiqueta.setHorizontalAlignment(SwingConstants.RIGHT);

        return etiqueta;
    }

    // Cargar los clientes registrados en la base de datos
    private void cargarClientes() {

        String sql = "SELECT id_usuario, nombre, apellidos "
                   + "FROM usuarios "
                   + "WHERE tipo_usuario = 'cliente'";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {

                int id = resultado.getInt("id_usuario");

                String nombre = resultado.getString("nombre")
                        + " " + resultado.getString("apellidos");

                cmbCliente.addItem(nombre);
                clientes.put(nombre, id);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this, "Error al cargar los clientes."
            );
            e.printStackTrace();
        }
    }

    // Cargar las películas y sus precios de la base de datos
    private void cargarPeliculas() {

        String sql = "SELECT id_pelicula, titulo, precio_alquiler "
                   + "FROM peliculas";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {

                int id = resultado.getInt("id_pelicula");

                String titulo = resultado.getString("titulo");

                double precio = resultado.getDouble("precio_alquiler");

                cmbPelicula.addItem(titulo);
                peliculas.put(titulo, id);
                precios.put(titulo, precio);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this, "Error al cargar las películas."
            );
            e.printStackTrace();
        }
    }

    // Mostrar el precio real de la película seleccionada
    private void actualizarPrecio() {

        String titulo = (String) cmbPelicula.getSelectedItem();

        txtPrecio.setText("");

        if (precios.containsKey(titulo)) {
            txtPrecio.setText(
                String.format("%.2f", precios.get(titulo))
            );
        }
    }

    private void registrarAlquiler() {

        if (cmbCliente.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(
                this, "Selecciona un cliente."
            );
            return;
        }

        if (cmbPelicula.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(
                this, "Selecciona una película."
            );
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Quieres registrar este alquiler?",
            "Confirmar alquiler",
            JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(
                this,
                "El registro de alquileres se implementará más adelante."
            );
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new VentanaAlquileres();
        });
    }
}
