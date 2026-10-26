
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import modelo.GestorPeliculas;
import modelo.Pelicula;

public class VentanaPeliculas extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel mainPanel;
    private JTextField txtBusqueda;
    private JTable tablaPeliculas;
    private DefaultTableModel modeloPeliculas;
    private TableRowSorter<DefaultTableModel> sorterPeliculas;

    private JComboBox<String> cmbGenero;
    private JComboBox<String> cmbDisponibilidad;

    public VentanaPeliculas() {
        setTitle("Videoclub - Catálogo y gestión de películas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.BLACK);

        cargarComponentes();

        add(mainPanel);
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarComponentes() {
        //Buscador y los filtros
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1, 5, 5));
        panelSuperior.setBackground(Color.BLACK);
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setBackground(Color.BLACK);
        JLabel lblBusqueda = new JLabel("Buscar película:");
        lblBusqueda.setForeground(Color.WHITE);
        txtBusqueda = new JTextField(20);
        panelBusqueda.add(lblBusqueda);
        panelBusqueda.add(txtBusqueda);
        JPanel panelFiltros = new JPanel();
        panelFiltros.setBackground(Color.BLACK);
        JLabel lblGenero = new JLabel("Género:");
        lblGenero.setForeground(Color.WHITE);
        String[] generos = {
            "Todos", "Acción", "Animación", "Comedia",
            "Drama", "Romance", "Ciencia ficción", "Suspense",
            "Terror", "Aventura", "Documental"
        };
        cmbGenero = new JComboBox<>(generos);
        JLabel lblDisponibilidad = new JLabel("Disponibilidad:");
        lblDisponibilidad.setForeground(Color.WHITE);
        String[] disponibilidades = {
            "Todas", "Disponible", "Alquilada"
        };
        cmbDisponibilidad = new JComboBox<>(disponibilidades);
        panelFiltros.add(lblGenero);
        panelFiltros.add(cmbGenero);
        panelFiltros.add(lblDisponibilidad);
        panelFiltros.add(cmbDisponibilidad);
        panelSuperior.add(panelBusqueda);
        panelSuperior.add(panelFiltros);
        mainPanel.add(panelSuperior, BorderLayout.NORTH);
        
        //La tabla de pelis
        String[] columnas = {
            "ID", "Título", "Género", "Año",
            "Duración (min)", "Precio (€)", "Disponibilidad"
        };
        modeloPeliculas = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaPeliculas = new JTable(modeloPeliculas);
        tablaPeliculas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPeliculas.setRowHeight(30);
        tablaPeliculas.setDefaultRenderer(Object.class, new FilaRenderer());
        sorterPeliculas = new TableRowSorter<DefaultTableModel>(modeloPeliculas);
        tablaPeliculas.setRowSorter(sorterPeliculas);
        JScrollPane scrollTabla = new JScrollPane(tablaPeliculas);
        mainPanel.add(scrollTabla, BorderLayout.CENTER);
        actualizarTabla();
        
        txtBusqueda.getDocument().addDocumentListener(
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
        cmbGenero.addActionListener(e -> filtrarPeliculas());
        cmbDisponibilidad.addActionListener(e -> filtrarPeliculas());

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.BLACK);
        JButton btnAnadir = new JButton("Añadir película");
        JButton btnModificar = new JButton("Modificar película");
        JButton btnEliminar = new JButton("Eliminar película");
        btnAnadir.setBackground(new Color(255, 195, 40));
        btnModificar.setBackground(new Color(255, 195, 40));
        btnEliminar.setBackground(new Color(220, 35, 40));
        btnEliminar.setForeground(Color.WHITE);
        btnAnadir.addActionListener(e -> anadirPelicula());
        btnModificar.addActionListener(e -> modificarPelicula());
        btnEliminar.addActionListener(e -> eliminarPelicula());
        panelBotones.add(btnAnadir);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        mainPanel.add(panelBotones, BorderLayout.SOUTH);
    }

    private void actualizarTabla() {
        modeloPeliculas.setRowCount(0);
        ArrayList<Pelicula> peliculas = GestorPeliculas.getPeliculas();
        for (Pelicula pelicula : peliculas) {
            Object[] fila = {
                pelicula.getId(),
                pelicula.getTitulo(),
                pelicula.getGenero(),
                pelicula.getAnio(),
                pelicula.getDuracion(),
                pelicula.getPrecio(),
                pelicula.isDisponible() ? "Disponible" : "Alquilada"
            };
            modeloPeliculas.addRow(fila);
        }
        filtrarPeliculas();
    }
    
    private void filtrarPeliculas() {
        ArrayList<RowFilter<DefaultTableModel, Object>> filtros = new ArrayList<>();
        String texto = txtBusqueda.getText().trim();
        if (!texto.isEmpty()) {
            filtros.add(RowFilter.regexFilter(
                "(?i)" + java.util.regex.Pattern.quote(texto),
                1
            ));
        }
        String genero = (String) cmbGenero.getSelectedItem();
        if (genero != null && !genero.equals("Todos")) {
            filtros.add(RowFilter.regexFilter(
                "^" + java.util.regex.Pattern.quote(genero) + "$",
                2
            ));
        }
        String disponibilidad = (String) cmbDisponibilidad.getSelectedItem();
        if ("Disponible".equals(disponibilidad)) {
            filtros.add(RowFilter.regexFilter("^Disponible$", 6));
        } else if ("Alquilada".equals(disponibilidad)) {
            filtros.add(RowFilter.regexFilter("^Alquilada$", 6));
        }
        if (filtros.isEmpty()) {
            sorterPeliculas.setRowFilter(null);
        } else {
            sorterPeliculas.setRowFilter(
                RowFilter.andFilter(filtros));
        }
    }

    private void anadirPelicula() {
        JTextField txtTitulo = new JTextField();
        JComboBox<String> cbGenero = crearComboGeneros();
        JTextField txtAnio = new JTextField();
        JTextField txtDuracion = new JTextField();
        JTextField txtPrecio = new JTextField();
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Título:"));
        panel.add(txtTitulo);
        panel.add(new JLabel("Género:"));
        panel.add(cbGenero);
        panel.add(new JLabel("Año:"));
        panel.add(txtAnio);
        panel.add(new JLabel("Duración (minutos):"));
        panel.add(txtDuracion);
        panel.add(new JLabel("Precio (€):"));
        panel.add(txtPrecio);
        int respuesta = JOptionPane.showConfirmDialog(
            this, panel, "Añadir película",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        if (respuesta != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            String titulo = txtTitulo.getText().trim();
            String genero = (String) cbGenero.getSelectedItem();

            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El título es obligatorio.");
                return;
            }
            int anio = Integer.parseInt(txtAnio.getText().trim());
            int duracion = Integer.parseInt(txtDuracion.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim().replace(',', '.'));
            if (anio < 1888 || anio > 2100 || duracion <= 0 || precio < 0 || !Double.isFinite(precio)) {
                JOptionPane.showMessageDialog(this, "Revisa el año, la duración y el precio.");
                return;
            }
            Pelicula pelicula = new Pelicula(GestorPeliculas.getSiguienteId(),titulo, genero, anio, duracion, precio, true);
            GestorPeliculas.anadirPelicula(pelicula);
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Película añadida correctamente.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,"El año, la duración y el precio deben ser números.");
        }
    }
    
    private void modificarPelicula() {
        int fila = tablaPeliculas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una película para modificarla.");
            return;
        }
        int filaModelo = tablaPeliculas.convertRowIndexToModel(fila);
        int id = (int) modeloPeliculas.getValueAt(filaModelo, 0);
        Pelicula pelicula = buscarPelicula(id);
        if (pelicula == null) {
            return;
        }
        JTextField txtTitulo = new JTextField(pelicula.getTitulo());
        JComboBox<String> cbGenero = crearComboGeneros();
        cbGenero.setSelectedItem(pelicula.getGenero());
        JTextField txtAnio = new JTextField(String.valueOf(pelicula.getAnio()));
        JTextField txtDuracion = new JTextField(String.valueOf(pelicula.getDuracion()));
        JTextField txtPrecio = new JTextField(String.valueOf(pelicula.getPrecio()));
        JCheckBox cbDisponible = new JCheckBox("Disponible", pelicula.isDisponible());
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("Título:"));
        panel.add(txtTitulo);
        panel.add(new JLabel("Género:"));
        panel.add(cbGenero);
        panel.add(new JLabel("Año:"));
        panel.add(txtAnio);
        panel.add(new JLabel("Duración (minutos):"));
        panel.add(txtDuracion);
        panel.add(new JLabel("Precio (€):"));
        panel.add(txtPrecio);
        panel.add(new JLabel("Estado:"));
        panel.add(cbDisponible);
        int respuesta = JOptionPane.showConfirmDialog(
            this, panel, "Modificar película",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (respuesta != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String titulo = txtTitulo.getText().trim();
            String genero = (String) cbGenero.getSelectedItem();

            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El título es obligatorio.");
                return;
            }

            int anio = Integer.parseInt(txtAnio.getText().trim());
            int duracion = Integer.parseInt(txtDuracion.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim().replace(',', '.'));
            if (anio < 1888 || anio > 2100 || duracion <= 0 || precio < 0 || !Double.isFinite(precio)) {
                JOptionPane.showMessageDialog(this, "Revisa el año, la duración y el precio.");
                return;
            }

            pelicula.setTitulo(titulo);
            pelicula.setGenero(genero);
            pelicula.setAnio(anio);
            pelicula.setDuracion(duracion);
            pelicula.setPrecio(precio);
            pelicula.setDisponible(cbDisponible.isSelected());
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Película modificada correctamente.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,"El año, la duración y el precio deben ser números.");
        }
    }

    private void eliminarPelicula() {

        int fila = tablaPeliculas.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una película para eliminarla.");
            return;
        }
        int filaModelo = tablaPeliculas.convertRowIndexToModel(fila);
        int id = (int) modeloPeliculas.getValueAt(filaModelo, 0);
        Pelicula pelicula = buscarPelicula(id);
        if (pelicula == null) {
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Quieres eliminar la película \""
                + pelicula.getTitulo() + "\"?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );
        if (respuesta == JOptionPane.YES_OPTION) {
            GestorPeliculas.eliminarPelicula(pelicula);
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Película eliminada correctamente.");
        }
    }

    private Pelicula buscarPelicula(int id) {
        for (Pelicula pelicula : GestorPeliculas.getPeliculas()) {
            if (pelicula.getId() == id) {
                return pelicula;
            }
        }
        return null;
    }

    private JComboBox<String> crearComboGeneros() {
        String[] generos = {
            "Acción", "Animación", "Comedia", "Drama",
            "Romance", "Ciencia ficción", "Suspense",
            "Terror", "Aventura", "Documental"
        };
        return new JComboBox<>(generos);
    }
}
