
package gui;

import main.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VentanaPerfilCliente extends JFrame {

    private static final Color FONDO = new Color(20, 20, 20);
    private static final Color TARJETA = new Color(35, 35, 35);
    private static final Color ROJO = new Color(229, 9, 20);
    private static final Color BLANCO = new Color(245, 245, 245);
    private static final Color GRIS = new Color(170, 170, 170);

    private int idUsuario;

    private JTextField txtNombre;
    private JTextField txtApellidos;
    private JTextField txtEmail;
    private JTextField txtTelefono;

    private JButton btnEditar;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnHistorial;

    private JTable tablaAlquileres;
    private DefaultTableModel modelo;

    public VentanaPerfilCliente(int idUsuario) {
        this.idUsuario = idUsuario;

        setTitle("CineClub | Mi perfil");
        setSize(900, 700);
        setMinimumSize(new Dimension(750, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout(0, 20));

        // CABECERA
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(FONDO);
        cabecera.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel logo = new JLabel("CINECLUB");
        logo.setFont(new Font("Arial", Font.BOLD, 30));
        logo.setForeground(ROJO);

        JLabel subtitulo = new JLabel("MI ESPACIO");
        subtitulo.setFont(new Font("Arial", Font.BOLD, 14));
        subtitulo.setForeground(GRIS);

        cabecera.add(logo, BorderLayout.WEST);
        cabecera.add(subtitulo, BorderLayout.EAST);

        add(cabecera, BorderLayout.NORTH);

        // CONTENIDO PRINCIPAL
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(FONDO);
        contenido.setBorder(BorderFactory.createEmptyBorder(0, 30, 25, 30));

        // TÍTULO
        JLabel titulo = new JLabel("Mi perfil");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(BLANCO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        contenido.add(titulo);
        contenido.add(Box.createVerticalStrut(20));

        // TARJETA DE DATOS
        JPanel tarjetaPerfil = new JPanel(new BorderLayout(15, 15));
        tarjetaPerfil.setBackground(TARJETA);
        tarjetaPerfil.setBorder(BorderFactory.createEmptyBorder(22, 25, 22, 25));
        tarjetaPerfil.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjetaPerfil.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        JLabel tituloDatos = new JLabel("INFORMACIÓN PERSONAL");
        tituloDatos.setFont(new Font("Arial", Font.BOLD, 15));
        tituloDatos.setForeground(ROJO);

        tarjetaPerfil.add(tituloDatos, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridLayout(2, 2, 20, 15));
        panelDatos.setBackground(TARJETA);

        txtNombre = crearCampo();
        txtApellidos = crearCampo();
        txtEmail = crearCampo();
        txtTelefono = crearCampo();

        panelDatos.add(crearGrupoCampo("NOMBRE", txtNombre));
        panelDatos.add(crearGrupoCampo("APELLIDOS", txtApellidos));
        panelDatos.add(crearGrupoCampo("CORREO ELECTRÓNICO", txtEmail));
        panelDatos.add(crearGrupoCampo("TELÉFONO", txtTelefono));

        tarjetaPerfil.add(panelDatos, BorderLayout.CENTER);

        // BOTONES PERFIL
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(TARJETA);

        btnEditar = crearBoton("EDITAR PERFIL", ROJO);
        btnGuardar = crearBoton("GUARDAR CAMBIOS", ROJO);
        btnCancelar = crearBoton("CANCELAR", new Color(75, 75, 75));

        btnGuardar.setVisible(false);
        btnCancelar.setVisible(false);

        panelBotones.add(btnEditar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        tarjetaPerfil.add(panelBotones, BorderLayout.SOUTH);

        contenido.add(tarjetaPerfil);
        contenido.add(Box.createVerticalStrut(30));

        // TÍTULO ALQUILERES
        JLabel tituloAlquileres = new JLabel("Mis alquileres activos");
        tituloAlquileres.setFont(new Font("Arial", Font.BOLD, 24));
        tituloAlquileres.setForeground(BLANCO);
        tituloAlquileres.setAlignmentX(Component.LEFT_ALIGNMENT);

        contenido.add(tituloAlquileres);
        contenido.add(Box.createVerticalStrut(15));

        // TABLA DE ALQUILERES
        modelo = new DefaultTableModel(
                new String[]{"PELÍCULA", "FECHA DE ALQUILER", "FECHA LÍMITE", "PRECIO"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaAlquileres = new JTable(modelo);
        estilizarTabla();

        JScrollPane scroll = new JScrollPane(tablaAlquileres);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(TARJETA);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setPreferredSize(new Dimension(800, 180));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        contenido.add(scroll);
        contenido.add(Box.createVerticalStrut(15));

        // BOTÓN HISTORIAL
        JPanel panelHistorial = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelHistorial.setBackground(FONDO);
        panelHistorial.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnHistorial = crearBoton("VER HISTORIAL COMPLETO", new Color(75, 75, 75));
        panelHistorial.add(btnHistorial);

        contenido.add(panelHistorial);

        JScrollPane scrollContenido = new JScrollPane(contenido);
        scrollContenido.setBorder(null);
        scrollContenido.getViewport().setBackground(FONDO);
        scrollContenido.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollContenido, BorderLayout.CENTER);

        // EVENTOS
        btnEditar.addActionListener(e -> activarEdicion());
        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> cancelarEdicion());

        btnHistorial.addActionListener(e -> {
            new VentanaHistorialAlquileres(idUsuario).setVisible(true);
        });

        // CARGAR DATOS
        cargarDatosCliente();
        cargarAlquileresActivos();
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(GRIS);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 11));
        return etiqueta;
    }

    private JPanel crearGrupoCampo(String titulo, JTextField campo) {
        JPanel grupo = new JPanel(new BorderLayout(0, 6));
        grupo.setBackground(TARJETA);

        grupo.add(crearEtiqueta(titulo), BorderLayout.NORTH);
        grupo.add(campo, BorderLayout.CENTER);

        return grupo;
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setEditable(false);
        campo.setBackground(new Color(50, 50, 50));
        campo.setForeground(BLANCO);
        campo.setCaretColor(BLANCO);
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 75, 75)),
                BorderFactory.createEmptyBorder(9, 10, 9, 10)
        ));
        return campo;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setForeground(BLANCO);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        return boton;
    }

    private void estilizarTabla() {
        tablaAlquileres.setBackground(TARJETA);
        tablaAlquileres.setForeground(BLANCO);
        tablaAlquileres.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaAlquileres.setRowHeight(38);
        tablaAlquileres.setGridColor(new Color(65, 65, 65));
        tablaAlquileres.setSelectionBackground(new Color(90, 20, 25));
        tablaAlquileres.setSelectionForeground(BLANCO);
        tablaAlquileres.setFillsViewportHeight(true);

        JTableHeader cabecera = tablaAlquileres.getTableHeader();
        cabecera.setBackground(new Color(50, 50, 50));
        cabecera.setForeground(BLANCO);
        cabecera.setFont(new Font("Arial", Font.BOLD, 11));
        cabecera.setPreferredSize(new Dimension(100, 40));
        cabecera.setReorderingAllowed(false);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        centrado.setBackground(TARJETA);
        centrado.setForeground(BLANCO);

        for (int i = 0; i < tablaAlquileres.getColumnCount(); i++) {
            tablaAlquileres.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }

    // CARGAR DATOS DEL CLIENTE
    private void cargarDatosCliente() {
        String sql = "SELECT nombre, apellidos, email, telefono " +
                     "FROM usuarios " +
                     "WHERE id_usuario = ? AND tipo_usuario = 'cliente'";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtNombre.setText(rs.getString("nombre"));
                    txtApellidos.setText(rs.getString("apellidos"));
                    txtEmail.setText(rs.getString("email"));
                    txtTelefono.setText(rs.getString("telefono"));
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No se han encontrado los datos del cliente."
                    );
                    dispose();
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los datos: " + e.getMessage()
            );
        }
    }

    // ACTIVAR EDICIÓN
    private void activarEdicion() {
        txtNombre.setEditable(true);
        txtApellidos.setEditable(true);
        txtEmail.setEditable(true);
        txtTelefono.setEditable(true);

        btnEditar.setVisible(false);
        btnGuardar.setVisible(true);
        btnCancelar.setVisible(true);
    }

    // GUARDAR CAMBIOS
    private void guardarCambios() {
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String email = txtEmail.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El nombre, los apellidos y el email son obligatorios."
            );
            return;
        }

        String sql = "UPDATE usuarios SET nombre = ?, apellidos = ?, " +
                     "email = ?, telefono = ? " +
                     "WHERE id_usuario = ? AND tipo_usuario = 'cliente'";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, email);
            ps.setString(4, telefono);
            ps.setInt(5, idUsuario);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "¡Perfil actualizado correctamente!"
                );

                cargarDatosCliente();
                cancelarEdicion();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se ha podido actualizar el perfil."
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar los cambios: " + e.getMessage()
            );
        }
    }

    // CANCELAR EDICIÓN
    private void cancelarEdicion() {
        cargarDatosCliente();

        txtNombre.setEditable(false);
        txtApellidos.setEditable(false);
        txtEmail.setEditable(false);
        txtTelefono.setEditable(false);

        btnEditar.setVisible(true);
        btnGuardar.setVisible(false);
        btnCancelar.setVisible(false);
    }

    // CARGAR ALQUILERES ACTIVOS
    private void cargarAlquileresActivos() {
        modelo.setRowCount(0);

        String sql = "SELECT p.titulo, a.fecha_alquiler, a.fecha_limite, a.precio " +
                     "FROM alquileres a " +
                     "JOIN peliculas p ON a.id_pelicula = p.id_pelicula " +
                     "WHERE a.id_usuario = ? AND a.fecha_devolucion IS NULL " +
                     "ORDER BY a.fecha_alquiler DESC";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                            rs.getString("titulo"),
                            rs.getString("fecha_alquiler"),
                            rs.getString("fecha_limite"),
                            rs.getDouble("precio") + " €"
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

