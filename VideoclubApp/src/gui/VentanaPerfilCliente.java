
package gui;

import java.awt.BorderLayout;
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

        JPanel panelDatos = new JPanel(new GridLayout(3, 1, 5, 5));
        panelDatos.setBorder(BorderFactory.createTitledBorder(
                "Datos personales"));

        panelDatos.add(lblNombre);
        panelDatos.add(lblEmail);
        panelDatos.add(lblTelefono);

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

        JScrollPane scroll = new JScrollPane(tablaAlquileres);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Alquileres activos"));
        btnHistorial = new JButton("Ver historial completo");

        btnHistorial.addActionListener(e -> {
            VentanaHistorialAlquileres ventana =
                    new VentanaHistorialAlquileres(idUsuario);

            ventana.setVisible(true);
        });
        setLayout(new BorderLayout(10, 10));

        add(panelDatos, BorderLayout.NORTH);
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
