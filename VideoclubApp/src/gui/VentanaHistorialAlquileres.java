
package gui;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import main.Conexion;

public class VentanaHistorialAlquileres extends JFrame {

    private int idUsuario;
    private JTable tablaAlquileres;
    private DefaultTableModel modelo;

    public VentanaHistorialAlquileres(int idUsuario) {
        this.idUsuario = idUsuario;

        setTitle("Historial de alquileres");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnas = {
            "Película",
            "Fecha de alquiler",
            "Fecha límite",
            "Fecha de devolución",
            "Precio",
            "Estado"
        };

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaAlquileres = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tablaAlquileres);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Todos los alquileres del cliente"));

        add(scroll, BorderLayout.CENTER);

        cargarHistorial();
    }

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
                        String.format(java.util.Locale.ROOT,
                                "%.2f €",
                                resultado.getDouble("precio")),
                        estado
                    });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar el historial: " + e.getMessage()
            );
        }
    }
}
