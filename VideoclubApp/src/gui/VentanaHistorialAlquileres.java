
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import main.Conexion;

public class VentanaHistorialAlquileres extends JFrame {

    private static final long serialVersionUID = 1L;

    private int idUsuario;
    private JTable tablaAlquileres;
    private DefaultTableModel modelo;

    // Colores del diseño
    private final Color FONDO = new Color(20, 20, 20);
    private final Color TARJETA = new Color(38, 38, 38);
    private final Color ROJO = new Color(229, 9, 20);
    private final Color TEXTO = Color.WHITE;

    public VentanaHistorialAlquileres(int idUsuario) {

        this.idUsuario = idUsuario;

        setTitle("Videoclub - Historial de alquileres");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(FONDO);

        crearCabecera();
        crearTabla();

        cargarHistorial();
    }

    // =========================================
    // CABECERA
    // =========================================

    private void crearCabecera() {

        JPanel panelCabecera = new JPanel(new BorderLayout());

        panelCabecera.setBackground(FONDO);
        panelCabecera.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JLabel lblLogo = new JLabel("VIDEOCLUB");

        lblLogo.setForeground(ROJO);
        lblLogo.setFont(
            new Font("Arial", Font.BOLD, 28)
        );

        JLabel lblTitulo = new JLabel("  |  Mi historial");

        lblTitulo.setForeground(TEXTO);
        lblTitulo.setFont(
            new Font("Arial", Font.BOLD, 21)
        );

        JPanel panelTitulos = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        panelTitulos.setBackground(FONDO);
        panelTitulos.add(lblLogo);
        panelTitulos.add(lblTitulo);

        JButton btnActualizar = new JButton("Actualizar");

        btnActualizar.setBackground(ROJO);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        btnActualizar.addActionListener(e -> cargarHistorial());

        panelCabecera.add(panelTitulos, BorderLayout.WEST);
        panelCabecera.add(btnActualizar, BorderLayout.EAST);

        add(panelCabecera, BorderLayout.NORTH);
    }

    // =========================================
    // TABLA
    // =========================================

    private void crearTabla() {

        String[] columnas = {
            "Película",
            "Fecha de alquiler",
            "Fecha límite",
            "Fecha de devolución",
            "Precio",
            "Estado"
        };

        modelo = new DefaultTableModel(columnas, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaAlquileres = new JTable(modelo);

        // Apariencia de la tabla
        tablaAlquileres.setBackground(TARJETA);
        tablaAlquileres.setForeground(TEXTO);

        tablaAlquileres.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        tablaAlquileres.setRowHeight(40);
        tablaAlquileres.setGridColor(new Color(65, 65, 65));
        tablaAlquileres.setSelectionBackground(
            new Color(100, 30, 30)
        );
        tablaAlquileres.setSelectionForeground(Color.WHITE);

        // Cabecera de la tabla
        JTableHeader cabecera = tablaAlquileres.getTableHeader();

        cabecera.setBackground(ROJO);
        cabecera.setForeground(Color.WHITE);
        cabecera.setFont(
            new Font("Arial", Font.BOLD, 13)
        );
        cabecera.setReorderingAllowed(false);

        // Centrar el contenido de las celdas
        DefaultTableCellRenderer centrado =
            new DefaultTableCellRenderer();

        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        centrado.setBackground(TARJETA);
        centrado.setForeground(TEXTO);

        for (int i = 0; i < tablaAlquileres.getColumnCount(); i++) {
            tablaAlquileres.getColumnModel()
                .getColumn(i)
                .setCellRenderer(centrado);
        }

        // Panel que contiene la tabla
        JPanel panelTabla = new JPanel(new BorderLayout());

        panelTabla.setBackground(FONDO);
        panelTabla.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(65, 65, 65)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            )
        );

        JScrollPane scroll = new JScrollPane(tablaAlquileres);

        scroll.setBorder(null);
        scroll.getViewport().setBackground(TARJETA);

        panelTabla.add(scroll, BorderLayout.CENTER);

        JPanel contenedor = new JPanel(new BorderLayout());

        contenedor.setBackground(FONDO);
        contenedor.setBorder(
            BorderFactory.createEmptyBorder(0, 25, 25, 25)
        );

        contenedor.add(panelTabla, BorderLayout.CENTER);

        add(contenedor, BorderLayout.CENTER);
    }

    // =========================================
    // CARGAR HISTORIAL DESDE SQLITE
    // =========================================

    private void cargarHistorial() {

        String sql = "SELECT p.titulo, a.fecha_alquiler, "
                   + "a.fecha_limite, a.fecha_devolucion, a.precio "
                   + "FROM alquileres a "
                   + "JOIN peliculas p "
                   + "ON a.id_pelicula = p.id_pelicula "
                   + "WHERE a.id_usuario = ? "
                   + "ORDER BY a.fecha_alquiler DESC";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta =
                 conexion.prepareStatement(sql)) {

            consulta.setInt(1, idUsuario);

            try (ResultSet resultado = consulta.executeQuery()) {

                modelo.setRowCount(0);

                while (resultado.next()) {

                    String fechaDevolucion =
                        resultado.getString("fecha_devolucion");

                    String estado;

                    if (fechaDevolucion == null
                            || fechaDevolucion.isBlank()) {

                        estado = "Activo";
                        fechaDevolucion = "Pendiente";

                    } else {

                        estado = "Devuelto";
                    }

                    modelo.addRow(new Object[] {

                        resultado.getString("titulo"),

                        resultado.getString("fecha_alquiler"),

                        resultado.getString("fecha_limite"),

                        fechaDevolucion,

                        String.format(
                            java.util.Locale.ROOT,
                            "%.2f €",
                            resultado.getDouble("precio")
                        ),

                        estado
                    });
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Error al cargar el historial:\n"
                    + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

