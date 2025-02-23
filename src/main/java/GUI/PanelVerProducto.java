package GUI;

import DAO.ProductoDaoMongo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelVerProducto extends PanelBase {
    private ProductoDaoMongo productoDaoMongo;
    private MongoDatabase baseDatos;
    private JTable tablaProductos;
    private JComboBox<String> comboBoxColecciones;

    public PanelVerProducto(JFrame ventana, ProductoDaoMongo productoDAO, MongoDatabase baseDatos) {
        this.productoDaoMongo = productoDAO;
        this.baseDatos = baseDatos;
        misComponentesIniciales(ventana);
    }

    private void misComponentesIniciales(JFrame ventana) {
        setLayout(new BorderLayout());

        // Crear y añadir la etiqueta en la parte superior (Norte)
        JLabel etiquetaTitulo = new JLabel("Lista de Productos", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        etiquetaTitulo.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza
        etiquetaTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0)); // Añadir borde vacío en la parte superior
        add(etiquetaTitulo, BorderLayout.NORTH);

        // Crear el panel central con GridBagLayout
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints restriccionesGridBag = new GridBagConstraints();
        restriccionesGridBag.fill = GridBagConstraints.HORIZONTAL;
        restriccionesGridBag.insets = new Insets(10, 5, 10, 5);

        // Crear y configurar las etiquetas y el comboBox de colecciones
        JLabel etiquetaColeccion = new JLabel("Seleccionar Colección:");
        etiquetaColeccion.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaColeccion.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza

        comboBoxColecciones = new JComboBox<>();
        cargarColeccionesEnComboBox();

        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 0;
        restriccionesGridBag.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaColeccion, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.anchor = GridBagConstraints.WEST;
        panelCentral.add(comboBoxColecciones, restriccionesGridBag);

        add(panelCentral, BorderLayout.NORTH);

        // Crear el JTable y su modelo
        String[] columnas = {"Producto", "Precio", "Descripción"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setShowHorizontalLines(false);
        tablaProductos.setShowVerticalLines(false);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        add(scrollPane, BorderLayout.CENTER);

        // Crear el panel para los botones "Volver" y "Ver Productos"
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));

        // Crear el botón "Volver"
        BotonPersonalizado botonVolver = new BotonPersonalizado("Volver");
        botonVolver.addActionListener(e -> volverAPanelProducto(ventana));
        panelBotones.add(botonVolver);

        // Crear el botón "Ver Productos"
        BotonPersonalizado botonVerProductos = new BotonPersonalizado("Ver Productos");
        botonVerProductos.addActionListener(e -> cargarDatosproductos());
        panelBotones.add(botonVerProductos);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarColeccionesEnComboBox() {
        MongoIterable<String> colecciones = baseDatos.listCollectionNames();
        for (String coleccion : colecciones) {
            if (!"Empleados".equalsIgnoreCase(coleccion) && !"counters".equalsIgnoreCase(coleccion) && !"Mesas".equalsIgnoreCase(coleccion)) {
                comboBoxColecciones.addItem(coleccion);
            }
        }
    }

    private void cargarDatosproductos() {
        String nombreColeccion = (String) comboBoxColecciones.getSelectedItem();
        JsonObject respuesta = productoDaoMongo.listarTodos(nombreColeccion);

        if (respuesta.get("estado").getAsString().equals("éxito")) {
            JsonArray productos = respuesta.getAsJsonArray("datos");
            DefaultTableModel modeloTabla = (DefaultTableModel) tablaProductos.getModel();
            modeloTabla.setRowCount(0); // Limpiar los datos existentes

            for (int i = 0; i < productos.size(); i++) {
                JsonObject producto = productos.get(i).getAsJsonObject();
                String nom = producto.has("nombre") && !producto.get("nombre").isJsonNull() ? producto.get("nombre").getAsString() : "N/A";
                String precio = producto.has("precio") && !producto.get("precio").isJsonNull() ? producto.get("precio").getAsString() : "N/A";
                String descripcion = producto.has("descripcion") && !producto.get("descripcion").isJsonNull() ? producto.get("descripcion").getAsString() : "N/A";
                modeloTabla.addRow(new Object[]{nom, precio, descripcion});
            }
        } else {
            JOptionPane.showMessageDialog(this, respuesta.get("mensaje").getAsString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAPanelProducto(JFrame ventana) {
        ventana.setContentPane(new Panel_Productos(ventana, productoDaoMongo, baseDatos));
        ventana.revalidate();
        ventana.repaint();
    }
}
