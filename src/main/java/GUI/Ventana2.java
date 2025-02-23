/*
 * Created by JFormDesigner on Thu May 30 10:46:49 CEST 2024
 */

package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * @author Winderandrex
 */
public class Ventana2 extends JFrame
{

    //ATRIBUTOS
    private JPanel panelPrincipal;

    //CONSTRUCTOR
    public Ventana2()
    {
        initComponents();

        ponPanelPrincipal2();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500); // Establecer tamaño de la ventana
        setLocationRelativeTo(getOwner()); // Centramos la ventana, relativo a la ventana padre, también podemos usar null
    }

    private void ponPanelPrincipal2() {

        panelPrincipal = new Ventana2.PanelPersonalizado();
        panelPrincipal.setLayout(new BorderLayout());

        // Configuración del panel principal
        JLabel etiquetaTitulo = new JLabel("Bienvenido a la Churreria Expo", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        etiquetaTitulo.setForeground(new Color(255, 204, 0)); // Amarillo mostaza
        etiquetaTitulo.setBorder(new EmptyBorder(20, 0, 20, 0)); // Ajustar la posición de la etiqueta
        panelPrincipal.add(etiquetaTitulo, BorderLayout.NORTH);

        // Crear un panel para los botones con GridLayout
        JPanel panelCentral = new JPanel(new GridLayout(3, 1, 0, 10));
        panelCentral.setOpaque(false); // Para que el fondo del panel principal se vea a través del panel de botones
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Crear los tres paneles de filas
        JPanel panelFila1 = new JPanel(new GridBagLayout());
        panelFila1.setOpaque(false);
        JPanel panelFila2 = new JPanel(new GridBagLayout());
        panelFila2.setOpaque(false);
        JPanel panelFila3 = new JPanel(new GridBagLayout());
        panelFila3.setOpaque(false);

        // Crear y añadir los botones a los paneles de filas
        JButton botonEntrar = crearBotonConImagen("Entrar", "/puerta.png", 50, 50);
        JButton botonEmpleados = crearBotonConImagen("Registro Empleados", "/empleados.png", 50, 50);
        JButton botonProductos = crearBotonConImagen("Registro Productos", "/churro.png", 50, 50);
        JButton botonGestionMesas = crearBotonConImagen("Gestion Mesas", "/mesas.png", 50, 50);
        JButton botonConfiguracion = crearBotonConImagenConTextoCentrado("Configuracion", "/configuracion.png", 30, 30);
        JButton botonSalir = crearBotonConImagenConTextoCentrado("Salir", "/exit.png", 30, 30);

        // Calcular el tamaño máximo preferido para todos los botones
        Dimension tamañoMaximoBoton = calcularTamañoMaximoDeBotones(botonEntrar, botonEmpleados, botonProductos, botonGestionMesas);
        // Calcular el tamaño más pequeño para los botones de panelFila3
        Dimension tamañoMinimoBoton = new Dimension(tamañoMaximoBoton.width, tamañoMaximoBoton.height / 2);

        // Establecer el tamaño preferido para todos los botones
        ajustarTamañoAlBoton(tamañoMaximoBoton, botonEntrar, botonEmpleados, botonProductos, botonGestionMesas);
        ajustarTamañoAlBoton(tamañoMinimoBoton, botonConfiguracion, botonSalir);

        // Añadir botones a los paneles de filas
        añadirBotonAlPanel(panelFila1, botonEntrar, botonEmpleados);
        añadirBotonAlPanel(panelFila2, botonGestionMesas, botonProductos);
        añadirBotonAlPanel(panelFila3, botonConfiguracion, botonSalir);

        // Añadir los paneles de filas al panel central
        panelCentral.add(panelFila1);
        panelCentral.add(panelFila2);
        panelCentral.add(panelFila3);

        // Añadir ActionListener a los botones
        añadirEscuchadores(botonEntrar, botonEmpleados, botonProductos, botonGestionMesas, botonSalir, botonConfiguracion);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
    }

    private Dimension calcularTamañoMaximoDeBotones(JButton... buttons)
    {
        int maxWidth = 0;
        int maxHeight = 0;

        for (JButton button : buttons)
        {
            Dimension size = button.getPreferredSize();
            if (size.width > maxWidth)
            {
                maxWidth = size.width;
            }
            if (size.height > maxHeight)
            {
                maxHeight = size.height;
            }
        }
        return new Dimension(maxWidth, maxHeight);
    }

    private void ajustarTamañoAlBoton(Dimension size, JButton... buttons)
    {
        for (JButton button : buttons)
        {
            button.setPreferredSize(size);
        }
    }

    private void añadirBotonAlPanel(JPanel panel, JButton... botones)
    {
        GridBagConstraints restriccionesGridBag = new GridBagConstraints();
        restriccionesGridBag.fill = GridBagConstraints.NONE;
        restriccionesGridBag.insets = new Insets(5, 15, 5, 15);
        restriccionesGridBag.weightx = 0.5;
        restriccionesGridBag.gridx = GridBagConstraints.RELATIVE;
        restriccionesGridBag.gridy = 0;

        for (JButton boton : botones)
        {
            panel.add(boton, restriccionesGridBag);
        }
    }

    private void añadirEscuchadores(JButton... buttons)
    {
        for (JButton button : buttons)
        {
            if (button.getText().equals("Entrar"))
            {
                button.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        //mostrarPanelEntrar();
                    }
                });
            }
            else
                if (button.getText().equals("Registro Empleados"))
            {
                button.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        mostrarPanelEmpleados();
                    }
                });
            }
                else
                    if (button.getText().equals("Registro Productos"))
            {
                button.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        //mostrarPanelProductos();
                    }
                });
            }
                    else
                        if (button.getText().equals("Gestion Mesas"))
            {
                button.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        //mostrarPanelGestionMesas();
                    }
                });
            }
                        else
                            if (button.getText().equals("Salir"))
            {
                button.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        System.exit(0); // Salir de la aplicación
                    }
                });
            }
                            else
                                if (button.getText().equals("Configuracion"))
            {
                button.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        //mostrarPanelConfiguracion();
                    }
                });
            }
        }
    }

    class PanelPersonalizado extends JPanel
    {
        @Override
        protected void paintComponent(Graphics g)
        {
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

    public void mostrarPanelPrincipal()
    {
        setContentPane(panelPrincipal);
        revalidate();
        repaint();
    }

    private void mostrarPanelEmpleados()
    {
        //setContentPane(new Panel_Empleados(this));
        revalidate();
        repaint();
    }

    private JButton crearBotonConImagen(String text, String iconPath, int iconWidth, int iconHeight)
    {
        JButton boton = new JButton(text);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setBorderPainted(true);
        boton.setFocusPainted(false);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boton.setForeground(new Color(255, 204, 0)); // Amarillo mostaza
        boton.setFont(new Font("Arial", Font.BOLD, 18));

        try
        {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(iconPath));
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(imagenEscalada));
        }
        catch (NullPointerException e)
        {
            System.out.println("No se pudo cargar la imagen: " + iconPath);
        }

        return boton;
    }

    private JButton crearBotonConImagenConTextoCentrado(String text, String iconPath, int iconWidth, int iconHeight)
    {
        JButton boton = new JButton(text);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setBorderPainted(true);
        boton.setFocusPainted(false);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.CENTER);
        boton.setForeground(new Color(255, 204, 0)); // Amarillo mostaza
        boton.setFont(new Font("Arial", Font.BOLD, 18));

        try
        {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(iconPath));
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(imagenEscalada));
            boton.setIconTextGap(-10); // Ajusta esto para controlar la superposición
        }
        catch (NullPointerException e)
        {
            System.out.println("No se pudo cargar la imagen: " + iconPath);
        }

        return boton;
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Andres

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Andres
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
