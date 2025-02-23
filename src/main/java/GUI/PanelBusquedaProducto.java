package GUI;

import DAO.ProductoDaoMongo;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoDatabase;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @autor Teysha
 */
public class PanelBusquedaProducto extends PanelBase
{
    // Atributos
    private JFrame ventanaPrincipal;
    private JTextField campoNomProducto;
    private ProductoDaoMongo productoDAO;
    private MongoDatabase baseDatos;
    private JProgressBar barraDeProgreso;

    // Constructor
    public PanelBusquedaProducto(JFrame ventana, ProductoDaoMongo productoDAO, MongoDatabase baseDatos)
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
        JLabel etiquetaTitulo = new JLabel("Búsqueda de Productos", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        etiquetaTitulo.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza
        etiquetaTitulo.setBorder(new EmptyBorder(30, 0, 10, 0)); // Añadir borde vacío en la parte superior
        add(etiquetaTitulo, BorderLayout.NORTH);

        // Crear el panel central con GridBagLayout
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false); // Para que el fondo del panel principal se vea a través del panel de botones
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints restriccionesGridBag = new GridBagConstraints();
        restriccionesGridBag.fill = GridBagConstraints.HORIZONTAL;
        restriccionesGridBag.insets = new Insets(10, 5, 10, 5);

        // Crear y configurar las etiquetas y campos de texto
        JLabel etiquetaProducto = new JLabel("Producto a buscar:");
        etiquetaProducto.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaProducto.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza

        campoNomProducto = new JTextField(20); // Tamaño aumentado
        campoNomProducto.setFont(new Font("Arial", Font.PLAIN, 18));

        // Añadir la etiqueta y campo de texto de Producto
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 0;
        restriccionesGridBag.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaProducto, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.anchor = GridBagConstraints.WEST;
        panelCentral.add(campoNomProducto, restriccionesGridBag);

        add(panelCentral, BorderLayout.CENTER);

        // Crear el panel sur con GridBagLayout para los botones y la barra de progreso
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false); // Para que el fondo del panel principal se vea a través del panel de botones

        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false);

        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.gridx = 0;
        gbcBotones.gridy = 0;
        gbcBotones.gridwidth = 1;
        gbcBotones.weightx = 1.0;
        gbcBotones.insets = new Insets(10, 10, 10, 10);
        gbcBotones.fill = GridBagConstraints.BOTH;

        BotonPersonalizado botonCancelar = new BotonPersonalizado("Cancelar");
        BotonPersonalizado botonBuscar = new BotonPersonalizado("Buscar");

        gbcBotones.gridx = 0;
        panelBotones.add(botonCancelar, gbcBotones);

        gbcBotones.gridx = 1;
        panelBotones.add(botonBuscar, gbcBotones);

        // Añadir la barra de progreso
        barraDeProgreso = new JProgressBar(0, 100);
        barraDeProgreso.setStringPainted(true); // Mostrar el porcentaje de carga
        barraDeProgreso.setVisible(false);

        panelSur.add(panelBotones, BorderLayout.CENTER);
        panelSur.add(barraDeProgreso, BorderLayout.SOUTH);

        // Añadir ActionListener al botón "Cancelar"
        botonCancelar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                volverAlPanelPrincipal();
            }
        });

        // Añadir ActionListener al botón "Buscar"
        botonBuscar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buscarProducto();
            }
        });

        add(panelSur, BorderLayout.SOUTH);
    }

    private void buscarProducto()
    {
        barraDeProgreso.setVisible(true); // Mostrar la barra de progreso
        campoNomProducto.setEnabled(false); // Deshabilitar el campo de texto
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>()
        {
            private JsonObject respuesta;

            @Override
            protected Void doInBackground() throws Exception
            {
                int tiempoCarga = ThreadLocalRandom.current().nextInt(3000, 6001); // Tiempo aleatorio entre 3000 y 6000 ms
                int pasos = tiempoCarga / 100; // Dividir el tiempo de carga en 100 pasos

                for (int i = 0; i <= 100; i++)
                {
                    Thread.sleep(pasos); // Simular el tiempo de carga
                    publish(i); // Publicar el progreso
                }

                respuesta = productoDAO.buscarPorNombre(campoNomProducto.getText());
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks)
            {
                for (int value : chunks)
                {
                    barraDeProgreso.setValue(value); // Actualizar la barra de progreso
                }
            }

            @Override
            protected void done()
            {
                barraDeProgreso.setVisible(false); // Ocultar la barra de progreso
                campoNomProducto.setEnabled(true); // Habilitar el campo de texto

                try
                {
                    get(); // Asegurarse de que no haya excepciones
                    if (respuesta.get("estado").getAsString().equals("éxito"))
                    {
                        // Mostrar información del producto encontrado
                        JsonObject datos = respuesta.getAsJsonObject("datos");
                        JOptionPane.showMessageDialog(PanelBusquedaProducto.this,
                                "Producto encontrado: \n" +
                                        "Nombre: " + datos.get("nombre").getAsString() + "\n" +
                                        "Precio: " + datos.get("precio").getAsDouble() + "\n" +
                                        "Descripción: " + datos.get("descripcion").getAsString() + "\n" +
                                        "Colección: " + datos.get("coleccion").getAsString(),
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                    {
                        // Mostrar mensaje de error
                        JOptionPane.showMessageDialog(PanelBusquedaProducto.this,
                                respuesta.get("mensaje").getAsString(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(PanelBusquedaProducto.this,
                            "Error al realizar la búsqueda.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute(); // Iniciar el SwingWorker
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
