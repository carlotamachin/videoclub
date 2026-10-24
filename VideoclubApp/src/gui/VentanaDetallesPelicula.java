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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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

    public VentanaDetallesPelicula(int idPelicula) {
        this.idPelicula = idPelicula;

        setTitle("Detalles de la película");
        setSize(500, 400);
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

        JPanel panelDatos = new JPanel(new GridLayout(7, 1, 5, 5));
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
        add(panelDatos, BorderLayout.NORTH);
        add(txtDescripcion, BorderLayout.CENTER);
        add(btnAlquilar, BorderLayout.SOUTH);

        JLabel logo= new JLabel("VIDEOCLUB");
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        logo.setForeground(Color.RED);
        JPanel panelSup= new JPanel();
        panelSup.setBackground(new Color(20, 20, 20));
        panelSup.setLayout(new BorderLayout());
        panelSup.add(logo, BorderLayout.NORTH);
        panelSup.add(panelDatos, BorderLayout.CENTER);

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