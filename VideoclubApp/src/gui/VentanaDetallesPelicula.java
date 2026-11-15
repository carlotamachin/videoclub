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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;

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
    private JLabel imagen;
    private int idPelicula;
    private JLabel[] estrellas = new JLabel[5];
    private JLabel lblValoracion;
    private static HashMap<Integer, Integer> valoraciones = new HashMap<>();

    public VentanaDetallesPelicula(int idPelicula) {
        this.idPelicula = idPelicula;

        setTitle("Detalles de la película");
        setSize(500, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        lblTitulo = new JLabel();
        lblGenero = new JLabel();
        lblAnio = new JLabel();
        lblPrecio = new JLabel();
        lblDisponibilidad = new JLabel();
        lblPromocion = new JLabel("Promoción: Sin promoción");
        lblPrecioFinal = new JLabel("Precio final: -- €");
        txtDescripcion = new JTextArea(5, 30);
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        btnAlquilar = new JButton("Iniciar alquiler");

        JPanel panelDatos = new JPanel(new GridLayout(8, 1, 5, 5));
     // Fondo gris casi negro
        panelDatos.setBackground(new Color(20, 20, 20));

        // Título blanco y grande
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));

        // Resto de los datos en blanco
        lblGenero.setForeground(Color.WHITE);
        lblAnio.setForeground(Color.WHITE);
        lblPrecio.setForeground(Color.WHITE);
        lblDisponibilidad.setForeground(Color.WHITE);
        lblPromocion.setForeground(Color.WHITE);
        lblPrecioFinal.setForeground(Color.WHITE);

        //area de descripción oscura
        txtDescripcion.setBackground(new Color(35, 35, 35));
        txtDescripcion.setForeground(Color.WHITE);
        txtDescripcion.setCaretColor(Color.WHITE);

        // Boton rojo 
        btnAlquilar.setBackground(new Color(229, 9, 20));
        btnAlquilar.setForeground(Color.WHITE);
        btnAlquilar.setFocusPainted(false);
        btnAlquilar.setOpaque(true);
        btnAlquilar.setBorderPainted(false);
        panelDatos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        
        panelDatos.add(lblTitulo);
        panelDatos.add(lblGenero);
        panelDatos.add(lblAnio);
        panelDatos.add(lblPrecio);
        panelDatos.add(lblDisponibilidad);
        panelDatos.add(lblPromocion);
        panelDatos.add(lblPrecioFinal);
        panelDatos.add(crearPanelValoracion());
        add(btnAlquilar, BorderLayout.SOUTH);

        JLabel logo= new JLabel("VIDEOCLUB");
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        logo.setForeground(Color.RED);
        JPanel panelSup= new JPanel();
        panelSup.setBackground(new Color(20, 20, 20));
        panelSup.setLayout(new BorderLayout());
        panelSup.add(logo, BorderLayout.NORTH);
        panelSup.add(panelDatos, BorderLayout.CENTER);

        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDescripcion.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        scrollDescripcion.getViewport().setBackground(new Color(35, 35, 35));
        scrollDescripcion.setBackground(new Color(20, 20, 20));
        panelSup.add(scrollDescripcion, BorderLayout.SOUTH);

        this.add(panelSup);
        cargarPelicula();
        
//todavia no estamos actuando sobre alquileres 
        btnAlquilar.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this,
                "El proceso de alquiler se conectará más adelante."
            )
        );
    }

    public static int buscarIdPorTitulo(String titulo) {
        String sql = "SELECT id_pelicula FROM peliculas WHERE LOWER(titulo) = LOWER(?)";
        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
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

    private JPanel crearPanelValoracion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        panel.setBackground(new Color(20, 20, 20));

        JLabel lblTexto = new JLabel("Tu valoración: ");
        lblTexto.setForeground(Color.WHITE);
        panel.add(lblTexto);

        for (int i = 0; i < estrellas.length; i++) {
            int valor = i + 1;
            JLabel estrella = new JLabel("\u2605");
            estrella.setFont(new Font("Dialog", Font.PLAIN, 22));
            estrella.setCursor(new Cursor(Cursor.HAND_CURSOR));
            estrella.setToolTipText(valor + " de 5");

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
                estrellas[i].setForeground(new Color(255, 195, 40));
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

    private void cargarPelicula() {
    	
    	//aqui totamos los datos de la pelicula q nos interesa, realizamos el metodo para ejecutar en sql con el select
        String sql = "SELECT titulo, genero, anio, descripcion, "
                   + "precio_alquiler, stock_total "
                   + "FROM peliculas WHERE id_pelicula = ?";
//aqui dejamos el where id_pelicula=? porq ese ? vamos a añadir el id q hemos metido al constructor inicialemnte 
       
        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
// y diras, porq 1? pues porq significa en el interrogante 1, su hubera "WHERE idPelicula=? AND anio=?" dirias 1> id y 2> anio
            consulta.setInt(1, idPelicula);
//aqui metes visualemnte las cosas a su correspondiente label
            try (ResultSet resultado = consulta.executeQuery()) {
                if (resultado.next()) {
                    lblTitulo.setText("Título: "
                            + resultado.getString("titulo"));
                    lblGenero.setText("Género: "
                            + resultado.getString("genero"));
                    lblAnio.setText("Año: "
                            + resultado.getInt("anio"));
                    lblPrecio.setText("Precio: "
                            + resultado.getDouble("precio_alquiler") + " €");
//aqui como quieres saca el precio final despues de la promocion, pues gurdas ese valor en una variable y asi poder realizar operaciones con ella mas adelanre
                    double precioOriginal = resultado.getDouble("precio_alquiler");
                    cargarPromocion(precioOriginal);
                    lblDisponibilidad.setText("Stock total: "
                            + resultado.getInt("stock_total"));

                    txtDescripcion.setText(
                            resultado.getString("descripcion"));
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
    private void cargarPromocion(double precioOriginal) {

        String sql = "SELECT nombre, descuento FROM promociones "
                   + "WHERE id_pelicula = ? "
                   + "AND date('now') BETWEEN date(fecha_inicio) "
                   + "AND date(fecha_fin) "
                   + "ORDER BY descuento DESC LIMIT 1";
       // Por tanto, no mostramos cualquier promoción guardada en
       // la base de datos, sino una que corresponda a la película y
      //  esté vigente según la fecha de SQLite.
        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idPelicula);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {

                    String nombre = resultado.getString("nombre");
                    double descuento = resultado.getDouble("descuento");

                    double precioFinal = precioOriginal
                            * (1 - descuento / 100.0);

                    lblPromocion.setText(
                            "Promoción: " + nombre
                            + " (" + descuento + "% de descuento)");

                    lblPrecioFinal.setText(String.format(
                            java.util.Locale.ROOT,
                            "Precio final: %.2f €",
                            precioFinal));

                } else {

                    lblPromocion.setText("Promoción: Sin promoción");

                    lblPrecioFinal.setText(String.format(
                            java.util.Locale.ROOT,
                            "Precio final: %.2f €",
                            precioOriginal));
                }
            }

        } catch (SQLException e) {

            lblPromocion.setText("No se pudieron cargar las promociones");

            lblPrecioFinal.setText(String.format(
                    java.util.Locale.ROOT,
                    "Precio original: %.2f €",
                    precioOriginal));

            e.printStackTrace();
        }
    }
}