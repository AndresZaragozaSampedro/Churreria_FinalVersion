package GUI;

import DAO.ProductoDaoMongo;
import com.mongodb.client.MongoDatabase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * @autor Teysha
 */
public class Panel_Productos extends PanelBase
{
    //atributos
    private ProductoDaoMongo productoDAO;
    private MongoDatabase baseDatos;

    //constructor
    public Panel_Productos(JFrame ventana, ProductoDaoMongo productoDAO, MongoDatabase baseDatos)
    {
        this.productoDAO = productoDAO;
        this.baseDatos = baseDatos;
        misComponentesIniciales(ventana);
    }

    private void misComponentesIniciales(JFrame ventana)
    {
        setLayout(new BorderLayout());

        // Crear y añadir la etiqueta en la parte superior (Norte)
        JLabel etiquetaTitulo = new JLabel("Gestion de productos", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        etiquetaTitulo.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza
        etiquetaTitulo.setBorder(new EmptyBorder(30, 0, 10, 0)); // Añadir borde vacío en la parte superior
        add(etiquetaTitulo, BorderLayout.NORTH);

        // Crear un panel para los botones con GridBagLayout
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false); // Para que el fondo del panel principal se vea a través del panel de botones
        panelBotones.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints restriccionesGridBag = new GridBagConstraints();
        restriccionesGridBag.fill = GridBagConstraints.BOTH;
        restriccionesGridBag.insets = new Insets(10, 10, 10, 10);

        // Crear y configurar los botones
        JButton botonAlta = new BotonPersonalizado("Alta de productos");
        JButton botonBusqueda = new BotonPersonalizado("Busqueda de productos");
        JButton botonActualizar = new BotonPersonalizado("Actualizar productos");
        JButton botonBorrar = new BotonPersonalizado("Borrar producto");
        JButton botonVer = new BotonPersonalizado("Ver productos");
        JButton botonVolver = new BotonPersonalizado("Volver");

        // Establecer tamaños preferidos para los botones
        Dimension botonGrande = new Dimension(200, 50);
        Dimension botonPequeno = new Dimension(150, 30);

        botonAlta.setPreferredSize(botonGrande);
        botonBusqueda.setPreferredSize(botonGrande);
        botonActualizar.setPreferredSize(botonGrande);
        botonBorrar.setPreferredSize(botonGrande);
        botonVer.setPreferredSize(botonPequeno);
        botonVolver.setPreferredSize(botonPequeno);

        // Primera fila, dos botones
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 0;
        restriccionesGridBag.weightx = 1.0; // Peso horizontal para expandir
        restriccionesGridBag.weighty = 1.0; // Peso vertical para expandir
        panelBotones.add(botonAlta, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.gridy = 0;
        panelBotones.add(botonBusqueda, restriccionesGridBag);

        // Segunda fila, dos botones
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 1;
        panelBotones.add(botonActualizar, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.gridy = 1;
        panelBotones.add(botonBorrar, restriccionesGridBag);

        // Tercera fila, un botón "Ver Productos"
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 2;
        restriccionesGridBag.gridwidth = 2;  // El botón "Ver Productos" ocupará dos columnas
        restriccionesGridBag.weightx = 0.0; // Peso horizontal menor para el botón "Ver Productos"
        restriccionesGridBag.weighty = 0.0; // Peso vertical menor para el botón "Ver Productos"
        panelBotones.add(botonVer, restriccionesGridBag);

        // Cuarta fila, un botón "Volver"
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 3;
        restriccionesGridBag.gridwidth = 2;  // El botón "Volver" ocupará dos columnas
        restriccionesGridBag.weightx = 0.0; // Peso horizontal menor para el botón "Volver"
        restriccionesGridBag.weighty = 0.0; // Peso vertical menor para el botón "Volver"
        panelBotones.add(botonVolver, restriccionesGridBag);


        // Añadir ActionListener al botón "Alta de Productos"
        botonAlta.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                irAlPanelDeAltaProductos(ventana);
            }
        });

        // Añadir ActionListener al botón "Busqueda de Productos"
        botonBusqueda.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                irAlPanelDeBusquedasProducto(ventana);
            }
        });

        botonActualizar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                irAlPanelActualizarProductos(ventana);
            }
        });

        botonBorrar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                irAlPanelBorrarProductos(ventana);
            }
        });

        // Añadir ActionListener al botón "Ver Productos"
        botonVer.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                irAlPanelDeVerProductos(ventana);
            }
        });

        // Añadir ActionListener al botón "Volver"
        botonVolver.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                volverAPanelPrincipal(ventana);
            }
        });

        add(panelBotones, BorderLayout.CENTER);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    private void irAlPanelDeAltaProductos(JFrame ventana)
    {
        ventana.setContentPane(new PanelAltaProducto(ventana, productoDAO, baseDatos));
        ventana.revalidate();
        ventana.repaint();
    }

    private void irAlPanelDeBusquedasProducto(JFrame ventana)
    {
        ventana.setContentPane(new PanelBusquedaProducto(ventana, productoDAO, baseDatos));
        ventana.revalidate();
        ventana.repaint();
    }

    private void irAlPanelActualizarProductos(JFrame ventana)
    {
        ventana.setContentPane(new PanelActualizarProducto(ventana, productoDAO, baseDatos));
        ventana.revalidate();
        ventana.repaint();
    }

    private void irAlPanelBorrarProductos(JFrame ventana)
    {
        ventana.setContentPane(new PanelBorrarProducto(ventana, productoDAO, baseDatos));
        ventana.revalidate();
        ventana.repaint();
    }

    private void irAlPanelDeVerProductos(JFrame ventana)
    {
        ventana.setContentPane(new PanelVerProducto(ventana, productoDAO, baseDatos));
        ventana.revalidate();
        ventana.repaint();
    }

    private void volverAPanelPrincipal(JFrame ventanaPrincipal)
    {
        // Establecer el panel principal como el contenido del JFrame
        if (ventanaPrincipal instanceof Ventana3)
        {
            Ventana3 ventana = (Ventana3) ventanaPrincipal;
            ventana.mostrarPanelPrincipal();
        }
    }
}
