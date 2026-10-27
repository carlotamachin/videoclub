
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class VentanaDevoluciones extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel mainPanel;
    private JTable tablaDevoluciones;
    private DefaultTableModel modeloDevoluciones;
    private JTextField txtBuscarCliente;

    public VentanaDevoluciones() {

        setTitle("Videoclub - Gestión de devoluciones");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBackground(Color.BLACK);

        cargarComponentes();

        add(mainPanel);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarComponentes() {

        // Panel superior: logo y titulo
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
            "GESTIÓN DE DEVOLUCIONES",
            SwingConstants.CENTER
        );
        lblTitulo.setForeground(Color.WHITE);

        panelSuperior.add(lblTitulo, BorderLayout.CENTER);

        // Buscador de clientes
        JPanel panelBusqueda = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 10, 10)
        );
        panelBusqueda.setBackground(Color.BLACK);

        JLabel lblBuscar = new JLabel("Buscar cliente:");
        lblBuscar.setForeground(Color.WHITE);

        txtBuscarCliente = new JTextField(20);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(255, 195, 40));

        btnBuscar.addActionListener(e -> buscarCliente());

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscarCliente);
        panelBusqueda.add(btnBuscar);

        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);

        mainPanel.add(panelSuperior, BorderLayout.NORTH);

        // Tabla de alquileres pendientes
        String[] columnas = {
            "ID", "Cliente", "Película",
            "Fecha alquiler", "Fecha límite"
        };

        modeloDevoluciones = new DefaultTableModel(columnas, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaDevoluciones = new JTable(modeloDevoluciones);
        tablaDevoluciones.setRowHeight(30);
        tablaDevoluciones.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        // Datos ficticios para el primer prototipo
        cargarAlquileres();

        JScrollPane scrollTabla = new JScrollPane(tablaDevoluciones);
        mainPanel.add(scrollTabla, BorderLayout.CENTER);

        // Panel inferior: boton de devolver
        JPanel panelBotones = new JPanel(
            new FlowLayout(FlowLayout.CENTER)
        );
        panelBotones.setBackground(Color.BLACK);

        JButton btnDevolver = new JButton("Registrar devolución");
        btnDevolver.setBackground(new Color(255, 195, 40));

        btnDevolver.addActionListener(e -> registrarDevolucion());

        panelBotones.add(btnDevolver);
        mainPanel.add(panelBotones, BorderLayout.SOUTH);
    }

    // Muestra los alquileres de ejemplo
    private void cargarAlquileres() {

        modeloDevoluciones.setRowCount(0);

        modeloDevoluciones.addRow(new Object[] {
            1, "María", "Titanic",
            "2026-10-26", "2026-10-29"
        });

        modeloDevoluciones.addRow(new Object[] {
            2, "Carlos", "Coco",
            "2026-10-27", "2026-10-30"
        });
    }

    // Busca alquileres por nombre del cliente
    private void buscarCliente() {

        String nombre = txtBuscarCliente.getText().trim();

        // Primero mostramos todos los alquileres
        cargarAlquileres();

        if (nombre.isEmpty()) {
            return;
        }

        // Recorremos la tabla de abajo hacia arriba
        for (int i = modeloDevoluciones.getRowCount() - 1;
                i >= 0; i--) {

            String cliente = modeloDevoluciones.getValueAt(
                i, 1
            ).toString();

            if (!cliente.toLowerCase().contains(
                    nombre.toLowerCase())) {

                modeloDevoluciones.removeRow(i);
            }
        }
    }

    private void registrarDevolucion() {

        int fila = tablaDevoluciones.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Selecciona un alquiler de la tabla."
            );
            return;
        }

        int filaModelo = tablaDevoluciones.convertRowIndexToModel(fila);

        String cliente = modeloDevoluciones.getValueAt(
            filaModelo, 1
        ).toString();

        String pelicula = modeloDevoluciones.getValueAt(
            filaModelo, 2
        ).toString();

        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Registrar la devolución de " + pelicula
                + " del cliente " + cliente + "?",
            "Confirmar devolución",
            JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(
                this,
                "La devolución real se implementará más adelante."
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaDevoluciones();
        });
    }
}
