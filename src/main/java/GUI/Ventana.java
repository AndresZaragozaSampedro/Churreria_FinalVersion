package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * @author Winderandrex
 */
public class Ventana extends JFrame
{

    private JPanel panelPrincipal;

    public Ventana()
    {
        initComponents();

        ponPanelPrincipal();

        //ponPanelPrincipal2();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600); // Establecer tamaño de la ventana
        setLocationRelativeTo(getOwner()); // Centramos la ventana, relativo a la ventana padre, tabien podemos usar null
    }

    private void ponPanelPrincipal() {
        panelPrincipal = new PanelPersonalizado();
        panelPrincipal.setLayout(new BorderLayout());

        // Configuración del panel principal
        JLabel etiquetaTitulo = new JLabel("Bienvenido a la Churreria Expo", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        etiquetaTitulo.setForeground(new Color(255, 204, 0)); // Amarillo mostaza
        etiquetaTitulo.setBorder(new EmptyBorder(20, 0, 20, 0)); // Ajustar la posición de la etiqueta
        panelPrincipal.add(etiquetaTitulo, BorderLayout.NORTH);

        // Crear un panel para los botones con GridBagLayout
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false); // Para que el fondo del panel principal se vea a través del panel de botones
        panelBotones.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints restriccionesGridBag = new GridBagConstraints();
        restriccionesGridBag.fill = GridBagConstraints.BOTH;
        restriccionesGridBag.insets = new Insets(15, 15, 15, 15);
        restriccionesGridBag.weightx = 0.5;
        restriccionesGridBag.weighty = 0.2;

        // Crear y añadir los botones al panel
        JButton botonEntrar = crearBotonConImagen("Entrar", "/puerta.png", 50, 50);
        JButton botonEmpleados = crearBotonConImagen("Registro Empleados", "/empleados.png", 50, 50);
        JButton botonProductos = crearBotonConImagen("Registro Productos", "/churro.png", 50, 50);
        JButton botonGestionMesas = crearBotonConImagen("Gestion Mesas", "/mesas.png", 50, 50);
        JButton botonConfiguracion = crearBotonConImagen("Configuracion", "/configuracion.png", 30, 30);
        JButton botonSalir = crearBotonConImagen("Salir", "/exit.png", 30, 30);

        // Ajustar el tamaño mínimo de los botones para que tengan el mismo tamaño
        Dimension botonDimension = new Dimension(200, 100);
        botonEntrar.setMinimumSize(botonDimension);
        botonEmpleados.setMinimumSize(botonDimension);
        botonProductos.setMinimumSize(botonDimension);
        botonGestionMesas.setMinimumSize(botonDimension);
        botonConfiguracion.setMinimumSize(botonDimension);
        botonSalir.setMinimumSize(botonDimension);

        // Primera fila, dos botones con tamaño intermedio
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 0;
        panelBotones.add(botonEntrar, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        panelBotones.add(botonEmpleados, restriccionesGridBag);

        // Segunda fila, dos botones con tamaño intermedio
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 1;
        panelBotones.add(botonGestionMesas, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        panelBotones.add(botonProductos, restriccionesGridBag);

        // Tercera fila, dos botones con tamaño más pequeño
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 2;
        restriccionesGridBag.weighty = 0.1; // Reducir el tamaño de los botones de la tercera fila
        panelBotones.add(botonConfiguracion, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        panelBotones.add(botonSalir, restriccionesGridBag);

        // Añadir ActionListener a los botones
        botonEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //mostrarPanelEntrar();
            }
        });

        botonEmpleados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanelEmpleados();
            }
        });

        botonProductos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //mostrarPanelProductos();
            }
        });

        botonGestionMesas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //mostrarPanelGestionMesas();
            }
        });

        botonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Salir de la aplicación
            }
        });

        botonConfiguracion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //mostrarPanelConfiguracion();
            }
        });

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
    }

    public void mostrarPanelPrincipal() {
        setContentPane(panelPrincipal);
        revalidate();
        repaint();
    }

    private void mostrarPanelEmpleados() {
        //setContentPane(new Panel_Empleados(this));
        revalidate();
        repaint();
    }

    class PanelPersonalizado extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            int width = getWidth();
            int height = getHeight();

            // Pintar la mitad superior con color verde oscuro
            g2d.setColor(new Color(0, 100, 0)); // Verde oscuro
            g2d.fillPolygon(new int[]{0, width, 0}, new int[]{0, 0, height}, 3);

            // Pintar la mitad inferior con color negro
            g2d.setColor(Color.BLACK);
            g2d.fillPolygon(new int[]{width, width, 0}, new int[]{0, height, height}, 3);

            g2d.dispose();
        }
    }

    private JButton crearBotonConImagen(String text, String iconPath, int iconWidth, int iconHeight) {
        JButton boton = new JButton(text);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setBorderPainted(true);
        boton.setFocusPainted(false);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boton.setForeground(new Color(255, 204, 0)); // Amarillo mostaza
        boton.setFont(new Font("Arial", Font.BOLD, 18));

        try {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(iconPath));
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(imagenEscalada));
        } catch (NullPointerException e) {
            System.out.println("No se pudo cargar la imagen: " + iconPath);
        }

        return boton;
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - unknown

        //======== this ========
        // Crear un panel con BorderLayout
//        JPanel panelPrincipal = new JPanel(new BorderLayout());
//
//        // Crear y añadir la etiqueta en la parte superior (Norte)
//        JLabel etiquetaTitulo = new JLabel("Bienvenido a la Churreria Expo", SwingConstants.CENTER);
//        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 18));
//        panelPrincipal.add(etiquetaTitulo, BorderLayout.NORTH);
//
//        // Crear un panel para los botones con GridLayout
//        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
//        panelBotones.setBorder(new EmptyBorder(20, 20, 20, 20));
//
//        // Crear y añadir los botones al panel
//        JButton botonEntrar = new JButton("Entrar");
//        JButton botonEmpleados = new JButton("Registro Empleados");
//        JButton botonProductos = new JButton("Registro Productos");
//        JButton botonSalir = new JButton("Salir");
//
//        panelBotones.add(botonEntrar);
//        panelBotones.add(botonEmpleados);
//        panelBotones.add(botonProductos);
//        panelBotones.add(botonSalir);
//
//        // Añadir el panel de botones al centro (Centro) del BorderLayout
//        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
//
//        // Añadir el panelPrincipal al contentPane del JFrame
//        var contentPane = getContentPane();
//        contentPane.setLayout(new BorderLayout());
//        contentPane.add(panelPrincipal, BorderLayout.CENTER);
//
//        // Establecer el tamaño de la ventana
//        setSize(600, 500);
//
//        // Centrar la ventana en la pantalla
//        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

}
