package GUI;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import DataProductos.Producto;
import Churreria.Churreria;
import Churreria.Mesa;
import Churreria.Ticket;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class Panel_GestionMesas extends PanelBase
{
    private JLabel etiquetaFecha;
    private JLabel etiquetaReloj;
    private JTable tablaMesa;
    private JPanel panelMenu;
    private MongoDatabase baseDatos;
    private Ventana3 ventanaPrincipal;
    private Mesa mesaActual;
    private DefaultTableModel modeloTabla;
    private JLabel etiquetaTotal;
    private Map<String, Mesa> mesasMap;
    private String nombreMesaActual;
    private JComboBox<String> comboBoxColecciones;
    private Churreria churreria;

    public Panel_GestionMesas(Ventana3 ventanaPrincipal, MongoDatabase baseDatos, String nombreMesa, Churreria churreria)
    {
        this.ventanaPrincipal = ventanaPrincipal;
        this.baseDatos = baseDatos;
        this.mesasMap = new HashMap<>();
        this.nombreMesaActual = nombreMesa;
        this.mesaActual = cargarMesa(nombreMesaActual);
        this.churreria = churreria;
        misComponentesIniciales();
        actualizarReloj();
        actualizarTablaMesa();
    }

    private void misComponentesIniciales()
    {
        setLayout(new BorderLayout());

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setOpaque(false);
        panelNorte.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel etiquetaChurreria = new JLabel("Churreria Expo", SwingConstants.CENTER);
        etiquetaChurreria.setFont(new Font("Arial", Font.BOLD, 24));
        etiquetaChurreria.setForeground(new Color(255, 204, 0));

        etiquetaFecha = new JLabel(fechaActual(), SwingConstants.LEFT);
        etiquetaFecha.setFont(new Font("Arial", Font.BOLD, 24));
        etiquetaFecha.setForeground(new Color(255, 204, 0));

        etiquetaReloj = new JLabel(horaActual(), SwingConstants.RIGHT);
        etiquetaReloj.setFont(new Font("Arial", Font.BOLD, 24));
        etiquetaReloj.setForeground(new Color(255, 204, 0));

        JPanel panelEtiquetasCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelEtiquetasCentro.setOpaque(false);
        panelEtiquetasCentro.add(etiquetaChurreria);
        panelEtiquetasCentro.add(etiquetaFecha);
        panelEtiquetasCentro.add(etiquetaReloj);

        panelNorte.add(panelEtiquetasCentro, BorderLayout.CENTER);

        add(panelNorte, BorderLayout.NORTH);

        PanelBase panelCentral = new PanelBase();
        panelCentral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel panelMesa = new JPanel(new BorderLayout());
        panelMesa.setPreferredSize(new Dimension(300, 300));
        tablaMesa = new JTable();
        tablaMesa.setShowGrid(false);

        Color amarilloMostaza = new Color(255, 204, 0);
        tablaMesa.setBackground(amarilloMostaza);
        tablaMesa.setOpaque(true);

        JScrollPane scrollPaneMesa = new JScrollPane(tablaMesa);
        scrollPaneMesa.getViewport().setBackground(amarilloMostaza);
        scrollPaneMesa.setBackground(amarilloMostaza);
        panelMesa.setBackground(amarilloMostaza);
        panelMesa.add(scrollPaneMesa, BorderLayout.CENTER);

        modeloTabla = new DefaultTableModel(new Object[]{"Cantidad", "Producto", "Precio Total"}, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        tablaMesa.setModel(modeloTabla);

        tablaMesa.getColumnModel().getColumn(0).setPreferredWidth(30);
        tablaMesa.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaMesa.getColumnModel().getColumn(2).setPreferredWidth(40);

        Font font = new Font("Arial", Font.BOLD, 15);
        tablaMesa.setFont(font);
        tablaMesa.setRowHeight(25);

        JTableHeader header = tablaMesa.getTableHeader();
        header.setFont(font);
        header.setPreferredSize(new Dimension(header.getWidth(), 30));

        JPanel panelTotal = new JPanel(new GridLayout(1, 2));
        panelTotal.setBackground(amarilloMostaza);
        JLabel etiquetaTextoTotal = new JLabel("Precio Total Mesa:");
        etiquetaTextoTotal.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaTotal = new JLabel("0.00€");
        etiquetaTotal.setFont(new Font("Arial", Font.BOLD, 18));
        panelTotal.add(etiquetaTextoTotal);
        panelTotal.add(etiquetaTotal);

        JPanel panelMesaConTotal = new JPanel(new BorderLayout());
        panelMesaConTotal.add(scrollPaneMesa, BorderLayout.CENTER);
        panelMesaConTotal.add(panelTotal, BorderLayout.SOUTH);

        panelMenu = new JPanel(new GridBagLayout());
        panelMenu.setBackground(amarilloMostaza);

        comboBoxColecciones = new JComboBox<>();
        cargarColeccionesEnComboBox();
        comboBoxColecciones.addActionListener(e -> cargarProductosEnPanelMenu());

        JPanel panelMenuConComboBox = new JPanel(new BorderLayout());
        panelMenuConComboBox.setBackground(amarilloMostaza);
        panelMenuConComboBox.add(comboBoxColecciones, BorderLayout.NORTH);
        panelMenuConComboBox.add(panelMenu, BorderLayout.CENTER);

        JScrollPane scrollPaneMenu = new JScrollPane(panelMenuConComboBox);
        scrollPaneMenu.setPreferredSize(new Dimension(400, 600));
        scrollPaneMenu.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneMenu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneMenu.getViewport().setBackground(amarilloMostaza);
        scrollPaneMenu.setBackground(amarilloMostaza);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panelCentral.add(panelMesaConTotal, gbc);

        gbc.gridx = 1;
        panelCentral.add(scrollPaneMenu, gbc);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelSur.setOpaque(false);
        JComboBox<String> comboBoxOpciones = new JComboBox<>(new String[]{"COBRAR", "IMPRIMIR FACTURA", "OIDO COCINA", "BORRAR"});
        JButton botonOK = new JButton("OK");
        JButton botonCambiarMesa = new JButton("Cambiar Mesa");
        JButton botonSalir = new JButton("SALIR");

        panelSur.add(comboBoxOpciones);
        panelSur.add(botonOK);
        panelSur.add(botonCambiarMesa);
        panelSur.add(botonSalir);

        add(panelSur, BorderLayout.SOUTH);

        botonSalir.addActionListener(e ->
        {
            guardarEstadoMesas();
            ventanaPrincipal.mostrarPanelPrincipal();
        });

        botonCambiarMesa.addActionListener(e ->
        {
            guardarEstadoMesas();
            ventanaPrincipal.mostrarPanelMesasRestaurantes();
        });

        botonOK.addActionListener(e ->
        {
            String opcionSeleccionada = (String) comboBoxOpciones.getSelectedItem();
            if (opcionSeleccionada != null)
            {
                switch (opcionSeleccionada)
                {
                    case "BORRAR":
                        int confirmacion = JOptionPane.showConfirmDialog(Panel_GestionMesas.this, "¿Está seguro de que desea borrar la mesa?", "Confirmar Borrado", JOptionPane.YES_NO_OPTION);
                        if (confirmacion == JOptionPane.YES_OPTION)
                        {
                            mesaActual.limpiarMesa();
                            actualizarTablaMesa();
                        }
                        break;
                    case "COBRAR":
                        Ticket ticket = churreria.cobrar(mesaActual);
                        String mensaje = String.format("Mesa cobrada. Importe Total: %.2f€\n%s", ticket.getImporteTotal(), ticket.toString());
                        JOptionPane.showMessageDialog(Panel_GestionMesas.this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
                        actualizarTablaMesa();
                        break;
                    case "OIDO COCINA":
                        JOptionPane.showMessageDialog(Panel_GestionMesas.this, "Oido Cocina", "Información", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "IMPRIMIR FACTURA":
                        JOptionPane.showMessageDialog(Panel_GestionMesas.this, "Se ha hecho la impresión de la factura", "Información", JOptionPane.INFORMATION_MESSAGE);
                        break;
                }
            }
        });
    }

    private void guardarEstadoMesas()
    {
        if (mesaActual != null)
        {
            mesasMap.put(nombreMesaActual, mesaActual);
            guardarMesa(nombreMesaActual, mesaActual);
        }
        for (Map.Entry<String, Mesa> entry : mesasMap.entrySet())
        {
            if (entry.getValue() != null)
            {
                guardarMesa(entry.getKey(), entry.getValue());
            }
        }
    }

    private void guardarMesa(String nombreMesa, Mesa mesa)
    {
        if (mesa == null)
        {
            return;
        }

        MongoCollection<Document> coleccionMesas = baseDatos.getCollection(nombreMesa);
        coleccionMesas.deleteMany(new Document());
        for (Producto producto : mesa.getProductos())
        {
            Document doc = new Document("nombre", producto.getNombre())
                    .append("precio", producto.getPrecio())
                    .append("descripcion", producto.getDescripcion())
                    .append("cantidad", producto.getCantidad());
            coleccionMesas.insertOne(doc);
        }
    }

    private Mesa cargarMesa(String nombreMesa)
    {
        MongoCollection<Document> coleccionMesas = baseDatos.getCollection(nombreMesa);
        MongoIterable<Document> documentos = coleccionMesas.find();
        Mesa mesa = new Mesa(1);
        boolean mesaVacia = true;
        for (Document doc : documentos)
        {
            Producto producto = new Producto(
                    doc.getString("nombre"),
                    doc.getDouble("precio"),
                    doc.getString("descripcion"),
                    doc.getInteger("cantidad"));
            mesa.agregarProductoConCantidad(producto, producto.getCantidad());
            mesaVacia = false;
        }
        return mesaVacia ? new Mesa(1) : mesa;
    }

    private String fechaActual()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        return sdf.format(new Date());
    }

    private String horaActual()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", new Locale("es", "ES"));
        return sdf.format(new Date());
    }

    private void actualizarReloj()
    {
        Timer timer = new Timer(1000, e -> etiquetaReloj.setText(horaActual()));
        timer.start();
    }

    private void cargarColeccionesEnComboBox()
    {
        String[] coleccionesDeseadas = {"Bebidas", "Porras", "Churros"};
        for (String coleccion : coleccionesDeseadas)
        {
            comboBoxColecciones.addItem(coleccion);
        }
    }

    private void cargarProductosEnPanelMenu()
    {
        String nombreColeccion = (String) comboBoxColecciones.getSelectedItem();
        panelMenu.removeAll();
        MongoCollection<Document> coleccionProductos = baseDatos.getCollection(nombreColeccion);

        MongoIterable<Document> documentos = coleccionProductos.find();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        int x = 0;
        int y = 0;

        for (Document doc : documentos)
        {
            String nombreProducto = doc.getString("nombre");
            String textoBoton = "<html><div style='text-align: center;'>" + insertarSaltosDeLinea(nombreProducto) + "</div></html>";
            JButton botonProducto = new JButton(textoBoton);
            botonProducto.setPreferredSize(new Dimension(120, 100));

            botonProducto.addActionListener(e ->
            {
                String cantidadStr = JOptionPane.showInputDialog(Panel_GestionMesas.this, "Ingrese la cantidad de " + doc.getString("nombre") + ":");
                try
                {
                    int cantidad = Integer.parseInt(cantidadStr);
                    if (cantidad <= 0)
                    {
                        JOptionPane.showMessageDialog(Panel_GestionMesas.this, "Por favor, ingrese un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        Producto producto = new Producto(doc.getString("nombre"), doc.getDouble("precio"), doc.getString("descripcion"), cantidad);
                        mesaActual.agregarProductoConCantidad(producto, cantidad);
                        actualizarTablaMesa();
                    }
                }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(Panel_GestionMesas.this, "Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            gbc.gridx = x;
            gbc.gridy = y;
            gbc.weightx = 0.0;
            gbc.weighty = 0.0;
            panelMenu.add(botonProducto, gbc);

            x++;
            if (x == 3)
            {
                x = 0;
                y++;
            }
        }

        // Rellenar con JLabels vacíos si la última fila no está completa
        while (x != 0 && x < 3)
        {
            gbc.gridx = x;
            gbc.gridy = y;
            gbc.weightx = 0.0;
            gbc.weighty = 0.0;
            JLabel filler = new JLabel();
            filler.setPreferredSize(new Dimension(120, 100));
            panelMenu.add(filler, gbc);
            x++;
        }

        gbc.gridx = 0;
        gbc.gridy = y + 1;
        gbc.gridwidth = 3; // Ocupar todo el ancho de las tres columnas
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelMenu.add(Box.createGlue(), gbc);

        panelMenu.revalidate();
        panelMenu.repaint();
    }

    private String insertarSaltosDeLinea(String texto)
    {
        String[] palabras = texto.split(" ");
        StringBuilder resultado = new StringBuilder();
        int contador = 0;
        for (String palabra : palabras)
        {
            resultado.append(palabra);
            contador++;
            if (contador % 2 == 0 && contador < palabras.length)
            {
                resultado.append("<br>");
            }
            else
            {
                resultado.append(" ");
            }
        }
        return resultado.toString().trim();
    }

    private void actualizarTablaMesa()
    {
        if (modeloTabla == null)
        {
            return;
        }
        modeloTabla.setRowCount(0);
        double total = 0;
        for (Producto p : mesaActual.getProductos())
        {
            double precioTotal = p.getCantidad() * p.getPrecio();
            total += precioTotal;
            modeloTabla.addRow(new Object[]{p.getCantidad(), p.getNombre(), String.format("%.2f€", precioTotal)});
        }
        etiquetaTotal.setText(String.format("%.2f€", total));
    }
}
