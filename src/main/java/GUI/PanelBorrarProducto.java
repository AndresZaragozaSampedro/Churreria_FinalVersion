package GUI;

import DAO.ProductoDaoMongo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author Winderandrex
 */
public class PanelBorrarProducto extends PanelBase
{
    private JFrame ventana;
    private JComboBox<String> comboBoxColecciones;
    private JComboBox<String> comboBoxProductos;
    private ProductoDaoMongo productoDAO;
    private MongoDatabase baseDatos;

    public PanelBorrarProducto(JFrame ventana, ProductoDaoMongo productoDAO, MongoDatabase baseDatos)
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
        JLabel etiquetaTitulo = new JLabel("Borrar Productos", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        etiquetaTitulo.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza
        etiquetaTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0)); // Añadir borde vacío en la parte superior
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

        // Primera fila: Colección de productos
        JLabel etiquetaColeccion = new JLabel("Colección de productos:");
        etiquetaColeccion.setFont(fontLabels);
        etiquetaColeccion.setForeground(colorAmarilloMostaza);
        comboBoxColecciones = new JComboBox<>();
        comboBoxColecciones.setPreferredSize(new Dimension(200, 30));
        cargarColeccionesEnComboBox();

        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaColeccion, restricciones);

        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        panelCentral.add(comboBoxColecciones, restricciones);

        // Segunda fila: Producto
        JLabel etiquetaProducto = new JLabel("Producto:");
        etiquetaProducto.setFont(fontLabels);
        etiquetaProducto.setForeground(colorAmarilloMostaza);
        comboBoxProductos = new JComboBox<>();
        comboBoxProductos.setPreferredSize(new Dimension(200, 30));

        restricciones.gridx = 0;
        restricciones.gridy = 1;
        restricciones.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaProducto, restricciones);

        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        panelCentral.add(comboBoxProductos, restricciones);

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

        BotonPersonalizado botonCancelar = new BotonPersonalizado("Cancelar");
        BotonPersonalizado botonBorrar = new BotonPersonalizado("Borrar");

        gbcBotones.gridx = 0;
        panelBotones.add(botonCancelar, gbcBotones);

        gbcBotones.gridx = 1;
        panelBotones.add(botonBorrar, gbcBotones);

        // Añadir ActionListener al botón "Cancelar"
        botonCancelar.addActionListener(e -> volverAlPanelPrincipal());

        // Añadir ActionListener al botón "Borrar"
        botonBorrar.addActionListener(e ->
        {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres borrar este producto?", "Confirmar borrado", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION)
            {
                borrarProducto();
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

    private void borrarProducto()
    {
        String nombreColeccion = (String) comboBoxColecciones.getSelectedItem();
        String productoNombre = (String) comboBoxProductos.getSelectedItem();

        JsonObject respuesta = productoDAO.eliminarPorNombre(productoNombre, nombreColeccion);

        if (respuesta.get("estado").getAsString().equals("éxito"))
        {
            JOptionPane.showMessageDialog(this, "Producto borrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Andres

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.
                swing.border.EmptyBorder(0,0,0,0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn",javax.swing.border
                .TitledBorder.CENTER,javax.swing.border.TitledBorder.BOTTOM,new java.awt.Font("Dia\u006cog"
                ,java.awt.Font.BOLD,12),java.awt.Color.red), getBorder
                ())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java
                                                                                                                                     .beans.PropertyChangeEvent e){if("\u0062ord\u0065r".equals(e.getPropertyName()))throw new RuntimeException
                ();}});
        setLayout(new BorderLayout());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }
}
