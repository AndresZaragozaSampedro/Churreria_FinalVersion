package GUI;

import DAO.ProductoDaoMongo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelActualizarProducto extends PanelBase
{
    private JFrame ventana;
    private JComboBox<String> comboBoxColecciones;
    private JComboBox<String> comboBoxProductos;
    private JTextField textFieldNuevoNombre;
    private JTextField textFieldNuevoPrecio;
    private JTextArea textAreaNuevaDescripcion;
    private ProductoDaoMongo productoDAO;
    private MongoDatabase baseDatos;

    public PanelActualizarProducto(JFrame ventana, ProductoDaoMongo productoDAO, MongoDatabase baseDatos)
    {
        this.ventana = ventana;
        this.productoDAO = productoDAO;
        this.baseDatos = baseDatos;
        misComponentesIniciales();
    }

    private void misComponentesIniciales()
    {
        setLayout(new BorderLayout());

        // Etiqueta de título
        JLabel etiquetaTitulo = new JLabel("Actualizar Productos", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        etiquetaTitulo.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza
        etiquetaTitulo.setBorder(new EmptyBorder(30, 0, 10, 0)); // Añadir borde vacío en la parte superior
        add(etiquetaTitulo, BorderLayout.NORTH);

        // Panel central con GridBagLayout
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false); // Hacer que el panel central no sea opaco
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20)); // Añadir borde vacío alrededor del panel central
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.insets = new Insets(10, 5, 10, 5);

        // Configuración de los JLabel
        Font fontLabels = new Font("Arial", Font.BOLD, 18); // Misma fuente y tamaño que en PanelAltaProducto
        Color colorAmarilloMostaza = new Color(255, 204, 0);

        // Primera fila: Colección a buscar
        JLabel etiquetaColeccion = new JLabel("Colección a buscar:");
        etiquetaColeccion.setFont(fontLabels);
        etiquetaColeccion.setForeground(colorAmarilloMostaza);
        comboBoxColecciones = new JComboBox<>();
        cargarColeccionesEnComboBox();

        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaColeccion, restricciones);

        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        panelCentral.add(comboBoxColecciones, restricciones);

        // Segunda fila: Producto a actualizar
        JLabel etiquetaProducto = new JLabel("Producto a actualizar:");
        etiquetaProducto.setFont(fontLabels);
        etiquetaProducto.setForeground(colorAmarilloMostaza);
        comboBoxProductos = new JComboBox<>();

        restricciones.gridx = 0;
        restricciones.gridy = 1;
        restricciones.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaProducto, restricciones);

        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        panelCentral.add(comboBoxProductos, restricciones);

        // Tercera fila: Nuevo nombre
        JLabel etiquetaNuevoNombre = new JLabel("Nuevo nombre:");
        etiquetaNuevoNombre.setFont(fontLabels);
        etiquetaNuevoNombre.setForeground(colorAmarilloMostaza);
        textFieldNuevoNombre = new JTextField(20);
        textFieldNuevoNombre.setFont(new Font("Arial", Font.PLAIN, 18)); // Misma fuente y tamaño que en PanelAltaProducto

        restricciones.gridx = 0;
        restricciones.gridy = 2;
        restricciones.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaNuevoNombre, restricciones);

        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        panelCentral.add(textFieldNuevoNombre, restricciones);

        // Cuarta fila: Nuevo precio
        JLabel etiquetaNuevoPrecio = new JLabel("Nuevo precio:");
        etiquetaNuevoPrecio.setFont(fontLabels);
        etiquetaNuevoPrecio.setForeground(colorAmarilloMostaza);
        textFieldNuevoPrecio = new JTextField(20);
        textFieldNuevoPrecio.setFont(new Font("Arial", Font.PLAIN, 18)); // Misma fuente y tamaño que en PanelAltaProducto

        restricciones.gridx = 0;
        restricciones.gridy = 3;
        restricciones.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaNuevoPrecio, restricciones);

        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        panelCentral.add(textFieldNuevoPrecio, restricciones);

        // Quinta fila: Nueva descripción
        JLabel etiquetaNuevaDescripcion = new JLabel("Nueva descripción:");
        etiquetaNuevaDescripcion.setFont(fontLabels);
        etiquetaNuevaDescripcion.setForeground(colorAmarilloMostaza);
        textAreaNuevaDescripcion = new JTextArea(5, 20);
        textAreaNuevaDescripcion.setFont(new Font("Arial", Font.PLAIN, 18)); // Misma fuente y tamaño que en PanelAltaProducto
        JScrollPane scrollPaneDescripcion = new JScrollPane(textAreaNuevaDescripcion);

        restricciones.gridx = 0;
        restricciones.gridy = 4;
        restricciones.anchor = GridBagConstraints.NORTHEAST;
        panelCentral.add(etiquetaNuevaDescripcion, restricciones);

        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        panelCentral.add(scrollPaneDescripcion, restricciones);

        add(panelCentral, BorderLayout.CENTER);

        // Panel sur con botones
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false); // Hacer que el panel de botones no sea opaco

        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.gridx = 0;
        gbcBotones.gridy = 0;
        gbcBotones.gridwidth = 1;
        gbcBotones.weightx = 1.0;
        gbcBotones.insets = new Insets(10, 10, 10, 10);
        gbcBotones.fill = GridBagConstraints.BOTH;

        BotonPersonalizado botonVolver = new BotonPersonalizado("Volver");
        BotonPersonalizado botonActualizar = new BotonPersonalizado("Actualizar");

        gbcBotones.gridx = 0;
        panelBotones.add(botonVolver, gbcBotones);

        gbcBotones.gridx = 1;
        panelBotones.add(botonActualizar, gbcBotones);

        // Añadir ActionListener al botón "Volver"
        botonVolver.addActionListener(e -> volverAlPanelPrincipal());

        // Añadir ActionListener al botón "Actualizar"
        botonActualizar.addActionListener(e -> actualizarProducto());

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
        comboBoxColecciones.addActionListener(e -> cargarProductosEnComboBox());
    }

    private void cargarProductosEnComboBox()
    {
        String nombreColeccion = (String) comboBoxColecciones.getSelectedItem();
        comboBoxProductos.removeAllItems();
        JsonObject respuesta = productoDAO.listarTodos(nombreColeccion);

        if (respuesta.get("estado").getAsString().equals("éxito"))
        {
            JsonArray productos = respuesta.getAsJsonArray("datos");
            for (int i = 0; i < productos.size(); i++)
            {
                JsonObject producto = productos.get(i).getAsJsonObject();
                String nombre = producto.has("nombre") ? producto.get("nombre").getAsString() : "N/A";
                comboBoxProductos.addItem(nombre);
            }
        }
    }

    private void actualizarProducto()
    {
        String nombreColeccion = (String) comboBoxColecciones.getSelectedItem();
        String productoOriginal = (String) comboBoxProductos.getSelectedItem();
        String nuevoNombre = textFieldNuevoNombre.getText();
        double nuevoPrecio = Double.parseDouble(textFieldNuevoPrecio.getText());
        String nuevaDescripcion = textAreaNuevaDescripcion.getText();

        JsonObject respuesta = productoDAO.actualizar(productoOriginal, nuevoNombre, nuevoPrecio, nuevaDescripcion, nombreColeccion);

        if (respuesta.get("estado").getAsString().equals("éxito"))
        {
            JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            volverAlPanelPrincipal();
        }
        else
        {
            JOptionPane.showMessageDialog(this, respuesta.get("mensaje").getAsString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAlPanelPrincipal()
    {
        ventana.setContentPane(new Panel_Productos(ventana, productoDAO, baseDatos));
        ventana.revalidate();
        ventana.repaint();
    }
}
