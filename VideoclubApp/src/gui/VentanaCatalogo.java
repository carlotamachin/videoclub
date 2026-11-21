
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.Conexion;

public class VentanaCatalogo extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtBuscar;
    private JComboBox<String> cbGenero;
    private JPanel panelPeliculas;

    private ArrayList<Pelicula> peliculas;

    // Usuario que ha iniciado sesión
    private int idUsuario;

    // Colores del diseño
    private final Color FONDO = new Color(20, 20, 20);
    private final Color TARJETA = new Color(38, 38, 38);
    private final Color ROJO = new Color(229, 9, 20);

    public VentanaCatalogo(int idUsuario) {

        this.idUsuario = idUsuario;

        setTitle("Videoclub - Catálogo");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(FONDO);

        peliculas = new ArrayList<>();

        crearCabecera();
        crearCatalogo();

        cargarPeliculas();
        cargarGeneros();
        mostrarPeliculas(peliculas);

    }

    // =========================================
    // CABECERA
    // =========================================

    private void crearCabecera() {

        JPanel cabecera = new JPanel(new BorderLayout());

        cabecera.setBackground(FONDO);
        cabecera.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        // Logo y título
        JLabel lblLogo = new JLabel("VIDEOCLUB");

        lblLogo.setForeground(ROJO);
        lblLogo.setFont(
            new Font("Arial", Font.BOLD, 30)
        );

        JLabel lblTitulo = new JLabel("  |  Catálogo de películas");

        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(
            new Font("Arial", Font.BOLD, 22)
        );

        JPanel panelTitulo = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        panelTitulo.setBackground(FONDO);
        panelTitulo.add(lblLogo);
        panelTitulo.add(lblTitulo);

        // Buscador y filtro
        JPanel panelFiltros = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        panelFiltros.setBackground(FONDO);

        txtBuscar = new JTextField(18);
        txtBuscar.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        txtBuscar.setToolTipText("Buscar película");

        cbGenero = new JComboBox<>();
        cbGenero.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        panelFiltros.add(txtBuscar);
        panelFiltros.add(cbGenero);

        cabecera.add(panelTitulo, BorderLayout.WEST);
        cabecera.add(panelFiltros, BorderLayout.EAST);

        add(cabecera, BorderLayout.NORTH);

        // Escuchar cambios en el buscador
        txtBuscar.getDocument().addDocumentListener(
            new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    filtrarPeliculas();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filtrarPeliculas();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filtrarPeliculas();
                }
            }
        );

        // Escuchar cambios en el filtro
        cbGenero.addActionListener(e -> filtrarPeliculas());
        
        
        
        
     // BOTÓN MI PERFIL
        JButton btnPerfil = new JButton("Mi perfil");

        btnPerfil.setBackground(ROJO);
        btnPerfil.setForeground(Color.WHITE);
        btnPerfil.setFocusPainted(false);

        btnPerfil.addActionListener(e -> {
            VentanaPerfilCliente perfil =
                new VentanaPerfilCliente(idUsuario);

            perfil.setVisible(true);
        });

        panelFiltros.add(btnPerfil);
    }

    // =========================================
    // PANEL DEL CATÁLOGO
    // =========================================

    private void crearCatalogo() {

        panelPeliculas = new JPanel(
            new GridLayout(0, 3, 20, 20)
        );

        panelPeliculas.setBackground(FONDO);

        panelPeliculas.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JScrollPane scroll = new JScrollPane(panelPeliculas);

        scroll.setBorder(null);
        scroll.getViewport().setBackground(FONDO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
    }

    // =========================================
    // CARGAR PELÍCULAS DE SQLITE
    // =========================================

    private void cargarPeliculas() {

        String sql = "SELECT id_pelicula, titulo, genero, anio, "
                   + "precio_alquiler, stock_total "
                   + "FROM peliculas "
                   + "ORDER BY titulo";

        peliculas.clear();

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta =
                 conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {

                Pelicula pelicula = new Pelicula(
                    resultado.getInt("id_pelicula"),
                    resultado.getString("titulo"),
                    resultado.getString("genero"),
                    resultado.getInt("anio"),
                    resultado.getDouble("precio_alquiler"),
                    resultado.getInt("stock_total")
                );

                peliculas.add(pelicula);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Error al cargar las películas:\n"
                    + e.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================
    // CARGAR GÉNEROS
    // =========================================

    private void cargarGeneros() {

        Set<String> generos = new LinkedHashSet<>();

        for (Pelicula pelicula : peliculas) {

            if (pelicula.genero != null
                    && !pelicula.genero.isBlank()) {

                generos.add(pelicula.genero);
            }
        }

        cbGenero.removeAllItems();

        cbGenero.addItem("Todos los géneros");

        for (String genero : generos) {
            cbGenero.addItem(genero);
        }
    }

    // =========================================
    // FILTRAR PELÍCULAS
    // =========================================

    private void filtrarPeliculas() {

        if (txtBuscar == null || cbGenero == null) {
            return;
        }

        String texto = txtBuscar.getText()
            .trim()
            .toLowerCase();

        String generoSeleccionado =
            (String) cbGenero.getSelectedItem();

        ArrayList<Pelicula> filtradas = new ArrayList<>();

        for (Pelicula pelicula : peliculas) {

            boolean coincideTitulo =
                pelicula.titulo != null
                && pelicula.titulo.toLowerCase().contains(texto);

            boolean coincideGenero =
                generoSeleccionado == null
                || generoSeleccionado.equals("Todos los géneros")
                || generoSeleccionado.equals(pelicula.genero);

            if (coincideTitulo && coincideGenero) {
                filtradas.add(pelicula);
            }
        }

        mostrarPeliculas(filtradas);
    }

    // =========================================
    // MOSTRAR PELÍCULAS
    // =========================================

    private void mostrarPeliculas(ArrayList<Pelicula> lista) {

        panelPeliculas.removeAll();

        if (lista.isEmpty()) {

            panelPeliculas.setLayout(new BorderLayout());

            JLabel lblVacio = new JLabel(
                "No se han encontrado películas",
                SwingConstants.CENTER
            );

            lblVacio.setForeground(Color.WHITE);
            lblVacio.setFont(
                new Font("Arial", Font.BOLD, 18)
            );

            panelPeliculas.add(
                lblVacio,
                BorderLayout.CENTER
            );

        } else {

            panelPeliculas.setLayout(
                new GridLayout(0, 3, 20, 20)
            );

            for (Pelicula pelicula : lista) {

                panelPeliculas.add(
                    crearTarjeta(pelicula)
                );
            }
        }

        panelPeliculas.revalidate();
        panelPeliculas.repaint();
    }

    // =========================================
    // CREAR TARJETA DE PELÍCULA
    // =========================================

    private JPanel crearTarjeta(Pelicula pelicula) {

        JPanel tarjeta = new JPanel(
            new BorderLayout(10, 12)
        );

        tarjeta.setBackground(TARJETA);

        tarjeta.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(65, 65, 65)
                ),
                BorderFactory.createEmptyBorder(
                    18, 18, 18, 18
                )
            )
        );

        tarjeta.setPreferredSize(
            new Dimension(280, 230)
        );

        // Título
        JLabel lblTitulo = new JLabel(
            pelicula.titulo == null
                ? "Sin título"
                : pelicula.titulo
        );

        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(
            new Font("Arial", Font.BOLD, 19)
        );

        // Información
        JLabel lblGenero = new JLabel(
            "Género: " + pelicula.genero
        );

        JLabel lblAnio = new JLabel(
            "Año: " + pelicula.anio
        );

        JLabel lblPrecio = new JLabel(
            String.format(
                "Precio: %.2f €",
                pelicula.precio
            )
        );

        JLabel lblStock = new JLabel(
            "Stock total: " + pelicula.stock
        );

        JLabel[] etiquetas = {
            lblGenero,
            lblAnio,
            lblPrecio,
            lblStock
        };

        JPanel datos = new JPanel(
            new GridLayout(4, 1, 5, 5)
        );

        datos.setBackground(TARJETA);

        for (JLabel etiqueta : etiquetas) {

            etiqueta.setForeground(Color.LIGHT_GRAY);
            etiqueta.setFont(
                new Font("Arial", Font.PLAIN, 14)
            );

            datos.add(etiqueta);
        }

        // Botón detalles
        JButton btnDetalles = new JButton("Ver detalles");

        btnDetalles.setBackground(ROJO);
        btnDetalles.setForeground(Color.WHITE);
        btnDetalles.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        btnDetalles.setFocusPainted(false);
        btnDetalles.setBorderPainted(false);
        btnDetalles.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        btnDetalles.addActionListener(e -> {
            VentanaDetallesPelicula ventana =
                new VentanaDetallesPelicula(pelicula.id, idUsuario);

            ventana.setVisible(true);
        });

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(datos, BorderLayout.CENTER);
        tarjeta.add(btnDetalles, BorderLayout.SOUTH);

        return tarjeta;
    }

    // =========================================
    // OBTENER ID DEL USUARIO
    // =========================================

    public int getIdUsuario() {
        return idUsuario;
    }

    // =========================================
    // CLASE INTERNA PELÍCULA
    // =========================================

    private static class Pelicula {

        int id;
        String titulo;
        String genero;
        int anio;
        double precio;
        int stock;

        public Pelicula(
            int id,
            String titulo,
            String genero,
            int anio,
            double precio,
            int stock
        ) {

            this.id = id;
            this.titulo = titulo;
            this.genero = genero;
            this.anio = anio;
            this.precio = precio;
            this.stock = stock;
        }
    }
}
