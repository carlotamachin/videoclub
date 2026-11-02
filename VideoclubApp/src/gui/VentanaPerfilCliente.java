
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import main.Conexion;

public class VentanaPerfilCliente extends JFrame {

    private JLabel lblNombre;
    private JLabel lblEmail;
    private JLabel lblTelefono;
    private JTable tablaAlquileres;
    private JButton btnHistorial;
    private int idUsuario;

    public VentanaPerfilCliente(int idUsuario) {
        this.idUsuario = idUsuario;

        setTitle("Perfil del cliente");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        lblNombre = new JLabel("Nombre: ");
        lblEmail = new JLabel("Email: ");
        lblTelefono = new JLabel("Teléfono: ");
        lblNombre.setForeground(Color.WHITE);
        lblEmail.setForeground(Color.WHITE);
        lblTelefono.setForeground(Color.WHITE);
        JPanel panelDatos = new JPanel(new GridLayout(3, 1, 5, 5));
        
        
        TitledBorder titulo= new TitledBorder("datos personales");
        
        titulo.setTitleFont(new Font("SansSerif", Font.PLAIN, 15));
        titulo.setTitleColor(new Color(200, 59, 70));
        panelDatos.setBorder(titulo);
        panelDatos.add(lblNombre);
        panelDatos.add(lblEmail);
        panelDatos.add(lblTelefono);
        panelDatos.setBackground(new Color(43, 45, 49));
       
        JPanel panelSup= new JPanel();
        panelSup.setLayout(new BorderLayout());
        JLabel logo= new JLabel("VIDEOCLUB");
        logo.setFont(new Font("Ariel", Font.BOLD, 21));
        logo.setForeground(Color.RED);
        panelSup.setBackground(new Color(43, 45, 49));
        panelSup.add(logo, BorderLayout.NORTH);
        panelSup.add(panelDatos, BorderLayout.CENTER);
        String[] columnas = {
            "Película", "Fecha de alquiler",
            "Fecha límite", "Precio"
        };

        DefaultTableModel modelo = new DefaultTableModel(
                columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaAlquileres = new JTable(modelo);
        tablaAlquileres.setForeground(new Color(232, 230, 227));
        tablaAlquileres.setBackground(new Color(43, 45, 49));
        tablaAlquileres.setSelectionBackground(new Color(120, 40, 46));
        tablaAlquileres.setSelectionForeground(Color.WHITE);
        tablaAlquileres.setGridColor(new Color(65, 65, 70));
        tablaAlquileres.getTableHeader()
        .setBackground(new Color(32, 33, 36));
tablaAlquileres.getTableHeader()
        .setForeground(new Color(232, 230, 227));
        JScrollPane scroll = new JScrollPane(tablaAlquileres);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Alquileres activos"));
        scroll.getViewport().setBackground(new Color(43, 45, 49));
        scroll.setBorder(BorderFactory.createLineBorder(
                new Color(65, 65, 70)));
        btnHistorial = new JButton("Ver historial completo");

        btnHistorial.addActionListener(e -> {
            VentanaHistorialAlquileres ventana =
                    new VentanaHistorialAlquileres(idUsuario);

            ventana.setVisible(true);
        });
        setLayout(new BorderLayout(10, 10));

        add(panelSup, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(btnHistorial, BorderLayout.SOUTH);

        cargarDatosCliente();
        cargarAlquileresActivos();
    }

    private void cargarDatosCliente() {

        String sql = "SELECT nombre, apellidos, email, telefono "
                   + "FROM usuarios WHERE id_usuario = ? "
                   + "AND tipo_usuario = 'cliente'";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta =
                     conexion.prepareStatement(sql)) {

            consulta.setInt(1, idUsuario);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {
                    lblNombre.setText("Nombre: "
                            + resultado.getString("nombre") + " "
                            + resultado.getString("apellidos"));

                    lblEmail.setText("Email: "
                            + resultado.getString("email"));

                    String telefono =
                            resultado.getString("telefono");

                    lblTelefono.setText("Teléfono: "
                            + (telefono == null ? "No indicado" : telefono));

                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No se ha encontrado ese cliente."
                    );
                    dispose();
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar el cliente: " + e.getMessage()
            );
        }
    }

    private void cargarAlquileresActivos() {

        String sql = "SELECT p.titulo, a.fecha_alquiler, "
                   + "a.fecha_limite, a.precio "
                   + "FROM alquileres a "
                   + "JOIN peliculas p "
                   + "ON a.id_pelicula = p.id_pelicula "
                   + "WHERE a.id_usuario = ? "
                   + "AND a.fecha_devolucion IS NULL "
                   + "ORDER BY a.fecha_alquiler DESC";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta =
                     conexion.prepareStatement(sql)) {

            consulta.setInt(1, idUsuario);

            try (ResultSet resultado = consulta.executeQuery()) {

                DefaultTableModel modelo =
                        (DefaultTableModel) tablaAlquileres.getModel();

                modelo.setRowCount(0);

                while (resultado.next()) {
                    modelo.addRow(new Object[] {
                        resultado.getString("titulo"),
                        resultado.getString("fecha_alquiler"),
                        resultado.getString("fecha_limite"),
                        String.format("%.2f €",
                                resultado.getDouble("precio"))
                    });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los alquileres: " + e.getMessage()
            );
        }
    }
}
