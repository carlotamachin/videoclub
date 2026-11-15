
package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class VentanaEstadisticas extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel mainPanel;
    private JLabel lblAlquileres;
    private JLabel lblDevoluciones;
    private JLabel lblPendientes;
    private JLabel lblIngresos;
    private JTable tablaPeliculas;
    private DefaultTableModel modeloPeliculas;

    public VentanaEstadisticas() {

        setTitle("Videoclub - Estadísticas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout(15, 20));
        mainPanel.setBackground(Color.BLACK);

        cargarComponentes();

        add(mainPanel);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarComponentes() {

        // Panel superior: logo, titulo y periodo
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(Color.BLACK);

        JLabel lblLogo = new JLabel();
        ImageIcon logo = new ImageIcon("resources/images/logo_videoclub.png");

        if (logo.getIconWidth() > 0) {
            Image imagen = logo.getImage().getScaledInstance(150, -1, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imagen));
        }

        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        panelSuperior.add(lblLogo, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel("ESTADÍSTICAS DEL VIDEOCLUB", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);

        // Selector de periodo
        JPanel panelPeriodo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelPeriodo.setBackground(Color.BLACK);

        JLabel lblPeriodo = new JLabel("Periodo:");
        lblPeriodo.setForeground(Color.WHITE);

        JComboBox<String> cmbPeriodo = new JComboBox<>(new String[] {"Este mes", "Total"});
        cmbPeriodo.addActionListener(e -> actualizarEstadisticas(cmbPeriodo.getSelectedIndex()));

        panelPeriodo.add(lblPeriodo);
        panelPeriodo.add(cmbPeriodo);
        panelSuperior.add(panelPeriodo, BorderLayout.SOUTH);

        mainPanel.add(panelSuperior, BorderLayout.NORTH);

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout(10, 20));
        panelCentral.setBackground(Color.BLACK);

        // Los cuatro recuadros de estadisticas
        JPanel panelIndicadores = new JPanel(new GridLayout(2, 2, 15, 15));
        panelIndicadores.setBackground(Color.BLACK);

        lblAlquileres = crearIndicador();
        lblDevoluciones = crearIndicador();
        lblPendientes = crearIndicador();
        lblIngresos = crearIndicador();

        panelIndicadores.add(crearRecuadro("Alquileres realizados", lblAlquileres));
        panelIndicadores.add(crearRecuadro("Devoluciones", lblDevoluciones));
        panelIndicadores.add(crearRecuadro("Pendientes", lblPendientes));
        panelIndicadores.add(crearRecuadro("Ingresos", lblIngresos));

        panelCentral.add(panelIndicadores, BorderLayout.NORTH);

        // Tabla de peliculas mas alquiladas
        JPanel panelTabla = new JPanel(new BorderLayout(10, 10));
        panelTabla.setBackground(Color.BLACK);

        JLabel lblTabla = new JLabel("Películas más alquiladas");
        lblTabla.setForeground(Color.WHITE);
        lblTabla.setFont(new Font("Arial", Font.BOLD, 15));
        panelTabla.add(lblTabla, BorderLayout.NORTH);

        String[] columnas = {"Película", "Número de alquileres"};

        modeloPeliculas = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaPeliculas = new JTable(modeloPeliculas);
        tablaPeliculas.setRowHeight(30);

        JScrollPane scrollTabla = new JScrollPane(tablaPeliculas);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        panelCentral.add(panelTabla, BorderLayout.CENTER);
        mainPanel.add(panelCentral, BorderLayout.CENTER);

        // Cargamos las estadisticas iniciales
        actualizarEstadisticas(0);
    }

    // Numero que aparecera en cada recuadro
    private JLabel crearIndicador() {

        JLabel indicador = new JLabel("0", SwingConstants.CENTER);
        indicador.setForeground(new Color(255, 195, 40));
        indicador.setFont(new Font("Arial", Font.BOLD, 28));

        return indicador;
    }


    private JPanel crearRecuadro(String titulo, JLabel indicador) {

        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBackground(new Color(35, 35, 35));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(indicador, BorderLayout.CENTER);

        return panel;
    }

    // Actualiza los numeros y la tabla segun el periodo
    private void actualizarEstadisticas(int periodo) {

        modeloPeliculas.setRowCount(0);

        if (periodo == 0) {

            // Datos ficticios de este mes
            lblAlquileres.setText("24");
            lblDevoluciones.setText("19");
            lblPendientes.setText("13");
            lblIngresos.setText("67,50 €");

            modeloPeliculas.addRow(new Object[] {"Titanic", 8});
            modeloPeliculas.addRow(new Object[] {"Coco", 6});
            modeloPeliculas.addRow(new Object[] {"El Padrino", 5});

        } else {

            // Datos ficticios del total
            lblAlquileres.setText("86");
            lblDevoluciones.setText("73");
            lblPendientes.setText("13");
            lblIngresos.setText("243,00 €");

            modeloPeliculas.addRow(new Object[] {"Titanic", 20});
            modeloPeliculas.addRow(new Object[] {"Coco", 17});
            modeloPeliculas.addRow(new Object[] {"El Padrino", 14});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaEstadisticas());
    }
}
