package GUI;

import DAO.ProductoDaoMongo;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import DAO.EmpleadoDAO;
import DAO.EmpleadoDAOMongo;
import data.Gestiona_BaseDatos_Churreria;
import data.Gestiona_BaseDatos_Empleados;
import Churreria.Churreria;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @autor Winderandrex
 */
public class Ventana3 extends JFrame
{
    // ATRIBUTOS
    private JPanel panelPrincipal;
    private Point clickInicial;
    private Map<JButton, Runnable> accionesBotones;
    private JButton botonEntrar;
    private JButton botonEmpleados;
    private JButton botonProductos;
    private JButton botonGestionMesas;
    private JButton botonConfiguracion;
    private JButton botonSalir;
    private EmpleadoDAO empleadoDAO;
    private ProductoDaoMongo productoDAO;
    private MongoDatabase baseDatos;  // Nueva variable
    private Churreria churreria; // Nueva variable

    // CONSTRUCTOR
    public Ventana3()
    {
        setTitle("App Churreria Expo"); // Establecemos el título de la ventana
        setUndecorated(true); // Quitamos la barra de título
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicializamos la conexión con la base de datos
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        baseDatos = mongoClient.getDatabase("BaseDatosChurreria"); // Inicializamos la base de datos aquí
        Gestiona_BaseDatos_Empleados gestionaBaseDatos = new Gestiona_BaseDatos_Empleados(baseDatos);
        Gestiona_BaseDatos_Churreria gestionaBaseDatosProductos = new Gestiona_BaseDatos_Churreria(baseDatos);
        empleadoDAO = new EmpleadoDAOMongo(gestionaBaseDatos);
        productoDAO = new ProductoDaoMongo(gestionaBaseDatosProductos);
        churreria = new Churreria(baseDatos); // Inicializa la churreria

        ponPanelPrincipal2();

        setSize(1000, 800); // Establecemos el tamaño de la ventana
        setLocationRelativeTo(null); // Centramos la ventana
        setVisible(true); // Hacemos visible la ventana

        // Añadimos listeners para redimensionar la ventana
        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                clickInicial = e.getPoint();
                getComponentAt(clickInicial);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                // Movemos la ventana
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                int xMoved = e.getX() - clickInicial.x;
                int yMoved = e.getY() - clickInicial.y;

                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation(X, Y);
            }
        });
    }

    private void ponPanelPrincipal2()
    {
        panelPrincipal = new PanelBase();
        panelPrincipal.setLayout(new BorderLayout());

        // Configuramos el panel principal
        JLabel etiquetaTitulo = new JLabel("Bienvenido a la Churreria Expo", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        etiquetaTitulo.setForeground(new Color(255, 204, 0)); // Amarillo mostaza
        etiquetaTitulo.setBorder(new EmptyBorder(20, 0, 20, 0)); // Ajustamos la posición de la etiqueta
        panelPrincipal.add(etiquetaTitulo, BorderLayout.NORTH);

        // Creamos un panel para los botones con GridBagLayout
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false); // Para que el fondo del panel principal se vea a través del panel de botones
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Creamos los tres paneles de filas
        JPanel panelFila1 = new JPanel(new GridBagLayout());
        panelFila1.setOpaque(false);
        JPanel panelFila2 = new JPanel(new GridBagLayout());
        panelFila2.setOpaque(false);
        JPanel panelFila3 = new JPanel(new GridBagLayout());
        panelFila3.setOpaque(false);

        // Creamos y añadimos los botones a los paneles de filas
        botonEntrar = new BotonPersonalizadoConImagen("Entrar", "/puerta.png", false);
        botonEmpleados = new BotonPersonalizadoConImagen("Registro Empleados", "/empleados.png", false);
        botonProductos = new BotonPersonalizadoConImagen("Registro Productos", "/churro.png", false);
        botonGestionMesas = new BotonPersonalizadoConImagen("Gestion Mesas", "/mesas.png", false);
        botonConfiguracion = new BotonPersonalizadoConImagen("Configuracion", "/configuracion.png", true);
        botonSalir = new BotonPersonalizadoConImagen("Salir", "/exit.png", true);

        // Ajustamos el tamaño de los botones
        Dimension maxButtonSize = new Dimension(200, 100);
        Dimension smallButtonSize = new Dimension(100, 100); // Hacer los botones pequeños más cuadrados

        botonEntrar.setPreferredSize(maxButtonSize);
        botonEmpleados.setPreferredSize(maxButtonSize);
        botonProductos.setPreferredSize(maxButtonSize);
        botonGestionMesas.setPreferredSize(maxButtonSize);
        botonConfiguracion.setPreferredSize(smallButtonSize);
        botonSalir.setPreferredSize(smallButtonSize);

        // Añadimos botones a los paneles de filas
        añadirBotonAlPanel(panelFila1, botonEntrar, botonEmpleados);
        añadirBotonAlPanel(panelFila2, botonGestionMesas, botonProductos);
        añadirBotonAlPanel(panelFila3, botonConfiguracion, botonSalir);

        // Añadimos los paneles de filas al panel central
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelCentral.add(panelFila1, gbc);

        gbc.gridy = 1;
        panelCentral.add(panelFila2, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.5;
        panelCentral.add(panelFila3, gbc);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        setContentPane(panelPrincipal);

        // Añadimos ActionListeners
        inicializarAccionesBotones();
        añadirEscuchadores(botonEntrar, botonEmpleados, botonProductos, botonGestionMesas, botonSalir, botonConfiguracion);
    }

    private void añadirBotonAlPanel(JPanel panel, JButton... botones)
    {
        GridBagConstraints restriccionesGridBag = new GridBagConstraints();
        restriccionesGridBag.fill = GridBagConstraints.BOTH;
        restriccionesGridBag.insets = new Insets(5, 15, 5, 15);
        restriccionesGridBag.weightx = 1.0;
        restriccionesGridBag.weighty = 1.0;
        restriccionesGridBag.gridx = GridBagConstraints.RELATIVE;
        restriccionesGridBag.gridy = 0;

        for (JButton boton : botones)
        {
            panel.add(boton, restriccionesGridBag);
        }
    }

    // Método para inicializar las acciones de los botones y asociarlas a cada botón
    private void inicializarAccionesBotones()
    {
        accionesBotones = new HashMap<>(); // Inicializamos el mapa de acciones
        // Asociamos cada botón con su acción correspondiente
        accionesBotones.put(botonEntrar, () -> mostrarPanelLogin());
        accionesBotones.put(botonEmpleados, () -> mostrarPanelEmpleados());
        accionesBotones.put(botonProductos, () -> mostrarPanelProductos());
        accionesBotones.put(botonGestionMesas, () -> mostrarPanelMesasRestaurantes());
        accionesBotones.put(botonSalir, () -> System.exit(0));
        accionesBotones.put(botonConfiguracion, () -> System.out.println("Configuracion"));
    }

    // Método para añadir ActionListeners a los botones
    private void añadirEscuchadores(JButton... botones)
    {
        for (JButton boton : botones)
        { // Recorremos cada botón
            Runnable accion = accionesBotones.get(boton); // Obtenemos la acción correspondiente del mapa
            if (accion != null)
            { // Si hay una acción asociada
                boton.addActionListener(e -> accion.run()); // Añadimos el ActionListener que ejecuta la acción
            }
        }
    }

    public void mostrarPanelPrincipal()
    {
        setContentPane(panelPrincipal);
        revalidate();
        repaint();
    }

    public void mostrarPanelEmpleados()
    {
        Panel_Empleados panelEmpleados = new Panel_Empleados(this, empleadoDAO);
        setContentPane(panelEmpleados);
        revalidate();
        repaint();
    }

    public void mostrarPanelLogin()
    {
        Panel_Login panelLogin = new Panel_Login(this, empleadoDAO);
        setContentPane(panelLogin);
        revalidate();
        repaint();
    }

    public void mostrarPanelProductos()
    {
        Panel_Productos panelProductos = new Panel_Productos(this, productoDAO, baseDatos);
        setContentPane(panelProductos);
        revalidate();
        repaint();
    }

    public void mostrarPanelMesasRestaurantes()
    {
        PanelMesasRestaurantes panelMesasResturante = new PanelMesasRestaurantes(this, baseDatos, churreria);
        setContentPane(panelMesasResturante);
        revalidate();
        repaint();
    }

    public void mostrarPanelGestionMesas(String nombreMesa)
    {
        Panel_GestionMesas panelGestionMesas = new Panel_GestionMesas(this, baseDatos, nombreMesa, churreria);
        setContentPane(panelGestionMesas);
        revalidate();
        repaint();
    }

    public void mostrarPanelCaja()
    {
        PanelCaja panelCaja = new PanelCaja(this, churreria, baseDatos);
        setContentPane(panelCaja);
        revalidate();
        repaint();
    }

    public MongoDatabase getBaseDatos()
    {
        return baseDatos;
    }

    private class BotonPersonalizadoConImagen extends JButton
    {
        private BufferedImage iconoOriginal;
        private String texto;
        private boolean esPequeno;

        public BotonPersonalizadoConImagen(String text, String iconPath)
        {
            this(text, iconPath, false);
        }

        public BotonPersonalizadoConImagen(String text, String iconPath, boolean esPequeno)
        {
            super("");
            this.texto = text;
            this.esPequeno = esPequeno;
            inicializarBoton(iconPath);
        }

        private void inicializarBoton(String iconPath)
        {
            setContentAreaFilled(false);
            setOpaque(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(new Color(255, 204, 0)); // Amarillo mostaza
            setFont(new Font("Arial", Font.BOLD, 24)); // Aumentar el tamaño de la fuente

            try
            {
                iconoOriginal = ImageIO.read(getClass().getResource(iconPath));
            }
            catch (IOException e)
            {
                System.out.println("No se pudo cargar la imagen: " + iconPath);
            }

            setHorizontalTextPosition(SwingConstants.CENTER);
            setVerticalTextPosition(SwingConstants.BOTTOM);
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            int width = getWidth();
            int height = getHeight();

            if (iconoOriginal != null)
            {
                int iconWidth = esPequeno ? width / 3 : width / 2;
                int iconHeight = esPequeno ? height / 3 : height / 2;
                Image imagenEscalada = iconoOriginal.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
                int x = (width - iconWidth) / 2;
                int y = height / 4;
                g2d.drawImage(imagenEscalada, x, y, this);

                // Dibuja el texto
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(texto);
                int textX = (width - textWidth) / 2;
                int textY = y + iconHeight + fm.getHeight();
                g2d.drawString(texto, textX, textY);
            }

            g2d.dispose();
        }
    }
}
