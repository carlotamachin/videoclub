
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.Conexion;

public class VentanaDetallesPelicula extends JFrame {

    private JLabel lblTitulo;
    private JLabel lblGenero;
    private JLabel lblAnio;
    private JLabel lblPrecio;
    private JLabel lblDisponibilidad;
    private JLabel lblPromocion;
    private JLabel lblPrecioFinal;

    private JTextArea txtDescripcion;
    private JButton btnAlquilar;

    private int idPelicula;
    private int idUsuario;

    private double precioAlquilerFinal;

    private JLabel[] estrellas = new JLabel[5];
    private JLabel lblValoracion;

    private static HashMap<Integer, Integer> valoraciones =
            new HashMap<>();

    private final Color FONDO = new Color(20, 20, 20);
    private final Color ROJO = new Color(229, 9, 20);

    public VentanaDetallesPelicula(int idPelicula, int idUsuario) {

        this.idPelicula = idPelicula;
        this.idUsuario = idUsuario;

        setTitle("Detalles de la película");
        setSize(500, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FONDO);

        lblTitulo = new JLabel();
        lblGenero = new JLabel();
        lblAnio = new JLabel();
        lblPrecio = new JLabel();
        lblDisponibilidad = new JLabel();
        lblPromocion = new JLabel();
        lblPrecioFinal = new JLabel();

        txtDescripcion = new JTextArea(5, 30);
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        btnAlquilar = new JButton("Alquilar");

        // Panel de datos
        JPanel panelDatos = new JPanel(new GridLayout(8, 1, 5, 5));
        panelDatos.setBackground(FONDO);
        panelDatos.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel[] etiquetas = {
            lblGenero,
            lblAnio,
            lblPrecio,
            lblDisponibilidad,
            lblPromocion,
            lblPrecioFinal
        };

        for (JLabel etiqueta : etiquetas) {
            etiqueta.setForeground(Color.WHITE);
            etiqueta.setFont(new Font("Arial", Font.PLAIN, 14));
        }

        panelDatos.add(lblTitulo);
        panelDatos.add(lblGenero);
        panelDatos.add(lblAnio);
        panelDatos.add(lblPrecio);
        panelDatos.add(lblDisponibilidad);
        panelDatos.add(lblPromocion);
        panelDatos.add(lblPrecioFinal);
        panelDatos.add(crearPanelValoracion());

        // Logo
        JLabel logo = new JLabel("VIDEOCLUB");
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        logo.setForeground(ROJO);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(FONDO);
        panelSuperior.add(logo, BorderLayout.NORTH);
        panelSuperior.add(panelDatos, BorderLayout.CENTER);

        // Descripción
        txtDescripcion.setBackground(new Color(35, 35, 35));
        txtDescripcion.setForeground(Color.WHITE);
        txtDescripcion.setCaretColor(Color.WHITE);
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDescripcion.setBorder(
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );

        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setBorder(
                BorderFactory.createEmptyBorder(0, 10, 10, 10)
        );
        scrollDescripcion.getViewport().setBackground(
                new Color(35, 35, 35)
        );

        panelSuperior.add(scrollDescripcion, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.CENTER);

        // Botón
        btnAlquilar.setBackground(ROJO);
        btnAlquilar.setForeground(Color.WHITE);
        btnAlquilar.setFont(new Font("Arial", Font.BOLD, 15));
        btnAlquilar.setFocusPainted(false);
        btnAlquilar.setBorderPainted(false);
        btnAlquilar.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        add(btnAlquilar, BorderLayout.SOUTH);

        // Cargar información de la película
        cargarPelicula();

        // Mostrar el texto correcto del botón
        actualizarBotonAlquiler();

        // Acción del botón
        btnAlquilar.addActionListener(e -> {

            if (tieneAlquilerActivo()) {
                devolverPelicula();
            } else {
                realizarAlquiler();
            }

        });
    }

    // =========================================
    // VALORACIONES
    // =========================================

    private JPanel crearPanelValoracion() {

        JPanel panel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 3, 0)
        );
        panel.setBackground(FONDO);

        JLabel lblTexto = new JLabel("Tu valoración: ");
        lblTexto.setForeground(Color.WHITE);
        panel.add(lblTexto);

        for (int i = 0; i < estrellas.length; i++) {

            int valor = i + 1;

            JLabel estrella = new JLabel("\u2605");
            estrella.setFont(new Font("Dialog", Font.PLAIN, 22));
            estrella.setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );

            estrella.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    pintarEstrellas(valor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    pintarEstrellas(getValoracion());
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    valoraciones.put(idPelicula, valor);
                    pintarEstrellas(valor);
                    actualizarTextoValoracion();
                }
            });

            estrellas[i] = estrella;
            panel.add(estrella);
        }

        lblValoracion = new JLabel();
        lblValoracion.setForeground(Color.WHITE);
        panel.add(lblValoracion);

        pintarEstrellas(getValoracion());
        actualizarTextoValoracion();

        return panel;
    }

    private int getValoracion() {
        return valoraciones.getOrDefault(idPelicula, 0);
    }

    private void pintarEstrellas(int cantidad) {

        for (int i = 0; i < estrellas.length; i++) {

            if (i < cantidad) {
                estrellas[i].setForeground(
                        new Color(255, 195, 40)
                );
            } else {
                estrellas[i].setForeground(Color.GRAY);
            }
        }
    }

    private void actualizarTextoValoracion() {

        int valor = getValoracion();

        if (valor == 0) {
            lblValoracion.setText("  Sin valorar");
        } else {
            lblValoracion.setText("  " + valor + "/5");
        }
    }

    // =========================================
    // CARGAR PELÍCULA
    // =========================================

    private void cargarPelicula() {

        String sql =
                "SELECT titulo, genero, anio, descripcion, "
                + "precio_alquiler, stock_total "
                + "FROM peliculas WHERE id_pelicula = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta =
                     conexion.prepareStatement(sql)) {

            consulta.setInt(1, idPelicula);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {

                    lblTitulo.setText(
                            "Título: " + resultado.getString("titulo")
                    );

                    lblGenero.setText(
                            "Género: " + resultado.getString("genero")
                    );

                    lblAnio.setText(
                            "Año: " + resultado.getInt("anio")
                    );

                    double precioOriginal =
                            resultado.getDouble("precio_alquiler");

                    lblPrecio.setText(
                            "Precio: " + precioOriginal + " €"
                    );

                    cargarPromocion(precioOriginal);

                    lblDisponibilidad.setText(
                            "Stock total: "
                            + resultado.getInt("stock_total")
                    );

                    txtDescripcion.setText(
                            resultado.getString("descripcion")
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "No se ha encontrado esa película."
                    );

                    dispose();
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar la película: " + e.getMessage()
            );
        }
    }

    // =========================================
    // PROMOCIONES
    // =========================================

    private void cargarPromocion(double precioOriginal) {

        String sql =
                "SELECT nombre, descuento FROM promociones "
                + "WHERE id_pelicula = ? "
                + "AND date('now') BETWEEN date(fecha_inicio) "
                + "AND date(fecha_fin) "
                + "ORDER BY descuento DESC LIMIT 1";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta =
                     conexion.prepareStatement(sql)) {

            consulta.setInt(1, idPelicula);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {

                    String nombre = resultado.getString("nombre");
                    double descuento =
                            resultado.getDouble("descuento");

                    precioAlquilerFinal =
                            precioOriginal * (1 - descuento / 100.0);

                    lblPromocion.setText(
                            "Promoción: " + nombre
                            + " (" + descuento + "% de descuento)"
                    );

                    lblPrecioFinal.setText(String.format(
                            java.util.Locale.ROOT,
                            "Precio final: %.2f €",
                            precioAlquilerFinal
                    ));

                } else {

                    precioAlquilerFinal = precioOriginal;

                    lblPromocion.setText(
                            "Promoción: Sin promoción"
                    );

                    lblPrecioFinal.setText(String.format(
                            java.util.Locale.ROOT,
                            "Precio final: %.2f €",
                            precioOriginal
                    ));
                }
            }

        } catch (SQLException e) {

            precioAlquilerFinal = precioOriginal;

            lblPromocion.setText(
                    "No se pudieron cargar las promociones"
            );

            lblPrecioFinal.setText(String.format(
                    java.util.Locale.ROOT,
                    "Precio original: %.2f €",
                    precioOriginal
            ));

            e.printStackTrace();
        }
    }

    // =========================================
    // COMPROBAR ALQUILER ACTIVO
    // =========================================

    private boolean tieneAlquilerActivo() {

        String sql =
                "SELECT COUNT(*) FROM alquileres "
                + "WHERE id_usuario = ? "
                + "AND id_pelicula = ? "
                + "AND fecha_devolucion IS NULL";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta =
                     conexion.prepareStatement(sql)) {

            consulta.setInt(1, idUsuario);
            consulta.setInt(2, idPelicula);

            try (ResultSet resultado = consulta.executeQuery()) {

                return resultado.next()
                        && resultado.getInt(1) > 0;
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al comprobar el alquiler: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // =========================================
    // ACTUALIZAR TEXTO DEL BOTÓN
    // =========================================

    private void actualizarBotonAlquiler() {

        if (tieneAlquilerActivo()) {
            btnAlquilar.setText("Devolver");
        } else {
            btnAlquilar.setText("Alquilar");
        }
    }

    // =========================================
    // REALIZAR ALQUILER
    // =========================================

    private void realizarAlquiler() {

        String sqlStock =
                "SELECT p.stock_total, "
                + "(SELECT COUNT(*) FROM alquileres a "
                + "WHERE a.id_pelicula = p.id_pelicula "
                + "AND a.fecha_devolucion IS NULL) AS alquiladas "
                + "FROM peliculas p WHERE p.id_pelicula = ?";

        String sqlInsertar =
                "INSERT INTO alquileres "
                + "(id_usuario, id_pelicula, fecha_alquiler, "
                + "fecha_limite, precio, fecha_devolucion) "
                + "VALUES (?, ?, date('now'), "
                + "date('now', '+7 days'), ?, NULL)";

        try (Connection conexion = Conexion.conectar()) {

            // Comprobar si quedan unidades
            try (PreparedStatement consulta =
                    conexion.prepareStatement(sqlStock)) {

                consulta.setInt(1, idPelicula);

                try (ResultSet resultado = consulta.executeQuery()) {

                    if (!resultado.next()) {

                        JOptionPane.showMessageDialog(
                                this,
                                "No se ha encontrado la película."
                        );

                        return;
                    }

                    int stockTotal =
                            resultado.getInt("stock_total");

                    int alquiladas =
                            resultado.getInt("alquiladas");

                    if (alquiladas >= stockTotal) {

                        JOptionPane.showMessageDialog(
                                this,
                                "No quedan unidades disponibles."
                        );

                        return;
                    }
                }
            }

            // Insertar alquiler
            try (PreparedStatement insertar =
                    conexion.prepareStatement(sqlInsertar)) {

                insertar.setInt(1, idUsuario);
                insertar.setInt(2, idPelicula);
                insertar.setDouble(3, precioAlquilerFinal);

                insertar.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "¡Alquiler realizado correctamente!\n"
                        + "Fecha límite: dentro de 7 días."
                );

                actualizarBotonAlquiler();
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al realizar el alquiler: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // =========================================
    // DEVOLVER PELÍCULA
    // =========================================

    private void devolverPelicula() {

        String sql =
                "UPDATE alquileres "
                + "SET fecha_devolucion = date('now') "
                + "WHERE id_usuario = ? "
                + "AND id_pelicula = ? "
                + "AND fecha_devolucion IS NULL";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta =
                     conexion.prepareStatement(sql)) {

            consulta.setInt(1, idUsuario);
            consulta.setInt(2, idPelicula);

            int filas = consulta.executeUpdate();

            if (filas > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "¡Película devuelta correctamente!"
                );

                actualizarBotonAlquiler();
                cargarPelicula();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No tienes esta película alquilada."
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al devolver la película: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // =========================================
    // BUSCAR ID POR TÍTULO
    // =========================================

    public static int buscarIdPorTitulo(String titulo) {

        String sql =
                "SELECT id_pelicula FROM peliculas "
                + "WHERE LOWER(titulo) = LOWER(?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta =
                     conexion.prepareStatement(sql)) {

            consulta.setString(1, titulo);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {
                    return resultado.getInt("id_pelicula");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}

