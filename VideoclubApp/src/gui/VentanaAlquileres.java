
package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.time.LocalDate;

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

import modelo.GestorPeliculas;
import modelo.Pelicula;

public class VentanaAlquileres extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel mainPanel;
    private JTextField txtCliente;
    private JComboBox<String> cmbPelicula;
    private JTextField txtFecha;
    private JTextField txtFechaLimite;
    private JTextField txtPrecio;

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

        txtCliente = new JTextField(20);

        cmbPelicula = new JComboBox<>();
        cmbPelicula.addItem("Selecciona una película");

        for (Pelicula pelicula : GestorPeliculas.getPeliculas()) {
            if (pelicula.isDisponible()) {
                cmbPelicula.addItem(pelicula.getTitulo());
            }
        }

        txtFecha = new JTextField(LocalDate.now().toString(), 20);
        txtFecha.setEditable(false);

        txtFechaLimite = new JTextField(
            LocalDate.now().plusDays(3).toString(), 20
        );
        txtFechaLimite.setEditable(false);

        txtPrecio = new JTextField(20);
        txtPrecio.setEditable(false);

        panelFormulario.add(crearEtiqueta("Cliente:"));
        panelFormulario.add(txtCliente);

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

    private void actualizarPrecio() {

        String titulo = (String) cmbPelicula.getSelectedItem();

        txtPrecio.setText("");

        for (Pelicula pelicula : GestorPeliculas.getPeliculas()) {

            if (pelicula.getTitulo().equals(titulo)) {
                txtPrecio.setText(
                    String.format("%.2f", pelicula.getPrecio())
                );
                break;
            }
        }
    }

    private void registrarAlquiler() {

        if (txtCliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Introduce el nombre del cliente."
            );
            return;
        }

        if (cmbPelicula.getSelectedIndex() == 0) {
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
