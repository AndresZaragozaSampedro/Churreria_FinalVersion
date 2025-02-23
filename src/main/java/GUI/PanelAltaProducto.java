package GUI;

import DAO.ProductoDaoMongo;
import DataProductos.Producto;
import Utilidades.PlaceHolder;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @autor Teysha
 */
public class PanelAltaProducto extends PanelBase
{
    private JFrame ventanaPrincipal;
    private JTextField campoNomProducto;
    private JTextField campoPrecioProducto;
    private PlaceHolder campoDescripcionProducto; // Usamos PlaceHolder aquí
    private JComboBox<String> comboBoxColecciones;
    private ProductoDaoMongo productoDAO;
    private MongoDatabase baseDatos;

    // Constructor
    public PanelAltaProducto(JFrame ventana, ProductoDaoMongo productoDAO, MongoDatabase baseDatos)
    {
        this.ventanaPrincipal = ventana;
        this.productoDAO = productoDAO;
        this.baseDatos = baseDatos;
        misComponentesIniciales();
    }

    private void misComponentesIniciales()
    {
        setLayout(new BorderLayout());

        // Crear y añadir la etiqueta en la parte superior (Norte)
        JLabel etiquetaTitulo = new JLabel("Alta de Productos", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        etiquetaTitulo.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza
        etiquetaTitulo.setBorder(new EmptyBorder(30, 0, 10, 0)); // Añadir borde vacío en la parte superior
        add(etiquetaTitulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false); // Para que el fondo del panel principal se vea a través del panel de botones
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints restriccionesGridBag = new GridBagConstraints();
        restriccionesGridBag.fill = GridBagConstraints.HORIZONTAL;
        restriccionesGridBag.insets = new Insets(10, 5, 10, 5);

        // Crear y configurar las etiquetas y campos de texto
        JLabel etiquetaColeccion = new JLabel("Colección:");
        etiquetaColeccion.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaColeccion.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza

        comboBoxColecciones = new JComboBox<>();
        cargarColeccionesEnComboBox();

        JLabel etiquetaNomProducto = new JLabel("Nombre Producto:");
        etiquetaNomProducto.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaNomProducto.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza

        campoNomProducto = new JTextField(20); // Tamaño aumentado
        campoNomProducto.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel etiquetaPrecioProducto = new JLabel("Precio Producto:");
        etiquetaPrecioProducto.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaPrecioProducto.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza

        campoPrecioProducto = new JTextField(20); // Tamaño aumentado
        campoPrecioProducto.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel etiquetaDescripcionProducto = new JLabel("Descripción Producto:");
        etiquetaDescripcionProducto.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaDescripcionProducto.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza

        campoDescripcionProducto = new PlaceHolder("(Opcional)"); // Usamos PlaceHolder aquí
        campoDescripcionProducto.setFont(new Font("Arial", Font.PLAIN, 18));

        // Añadir los campos al panel central
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 0;
        restriccionesGridBag.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaColeccion, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.anchor = GridBagConstraints.WEST;
        panelCentral.add(comboBoxColecciones, restriccionesGridBag);

        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 1;
        restriccionesGridBag.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaNomProducto, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.anchor = GridBagConstraints.WEST;
        panelCentral.add(campoNomProducto, restriccionesGridBag);

        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 2;
        restriccionesGridBag.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaPrecioProducto, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.anchor = GridBagConstraints.WEST;
        panelCentral.add(campoPrecioProducto, restriccionesGridBag);

        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 3;
        restriccionesGridBag.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaDescripcionProducto, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.anchor = GridBagConstraints.WEST;
        panelCentral.add(campoDescripcionProducto, restriccionesGridBag);

        add(panelCentral, BorderLayout.CENTER);

        // Crear el panel sur con GridBagLayout para los botones
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false); // Para que el fondo del panel principal se vea a través del panel de botones

        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.gridx = 0;
        gbcBotones.gridy = 0;
        gbcBotones.gridwidth = 1;
        gbcBotones.weightx = 1.0;
        gbcBotones.insets = new Insets(10, 10, 10, 10);
        gbcBotones.fill = GridBagConstraints.BOTH;

        BotonPersonalizado botonCancelar = new BotonPersonalizado("Cancelar");
        BotonPersonalizado botonValidar = new BotonPersonalizado("Validar");

        gbcBotones.gridx = 0;
        panelBotones.add(botonCancelar, gbcBotones);

        gbcBotones.gridx = 1;
        panelBotones.add(botonValidar, gbcBotones);

        // Añadir ActionListener al botón "Cancelar"
        botonCancelar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                volverAlPanelPrincipal();
            }
        });

        // Añadir ActionListener al botón "Validar"
        botonValidar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                validarProducto();
            }
        });

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarColeccionesEnComboBox()
    {
        MongoIterable<String> colecciones = baseDatos.listCollectionNames();
        for (String coleccion : colecciones)
        {
            if (!"Empleados".equalsIgnoreCase(coleccion) && !"counters".equalsIgnoreCase(coleccion) && !"Mesas".equalsIgnoreCase(coleccion)
                    && !"Barra Izquierda".equalsIgnoreCase(coleccion) && !"Barra Derecha".equalsIgnoreCase(coleccion) && !"Mesa 1".equalsIgnoreCase(coleccion)
                    && !"Mesa 2".equalsIgnoreCase(coleccion) && !"constantes".equalsIgnoreCase(coleccion) && !"tickets".equalsIgnoreCase(coleccion))
            {
                comboBoxColecciones.addItem(coleccion);
            }
        }
    }

    private void validarProducto()
    {
        String nombreColeccion = (String) comboBoxColecciones.getSelectedItem();
        String nombre = campoNomProducto.getText();
        String precioText = campoPrecioProducto.getText();
        String descripcion = campoDescripcionProducto.getText();

        if (nombre.isEmpty() || precioText.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Los campos de nombre y precio deben estar llenos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precio;
        try
        {
            precio = Double.parseDouble(precioText);
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Producto producto = new Producto(nombre, precio, !descripcion.equals("(Opcional)") ? descripcion : "", 0); // Se asegura que la descripción no sea null
        JsonObject respuesta = productoDAO.insertar(producto, nombreColeccion);

        if (respuesta.get("estado").getAsString().equals("éxito"))
        {
            JOptionPane.showMessageDialog(this, "Producto añadido correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            volverAlPanelPrincipal();
        }
        else
        {
            JOptionPane.showMessageDialog(this, respuesta.get("mensaje").getAsString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAlPanelPrincipal()
    {
        if (ventanaPrincipal instanceof Ventana3)
        {
            Ventana3 ventana = (Ventana3) ventanaPrincipal;
            ventana.mostrarPanelProductos();
        }
    }
}
