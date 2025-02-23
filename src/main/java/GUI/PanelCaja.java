package GUI;

import Churreria.Churreria;
import Churreria.Ticket;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class PanelCaja extends PanelBase
{
    private Ventana3 ventanaPrincipal;
    private Churreria churreria;
    private MongoDatabase baseDatos;
    private JTable tablaTickets;
    private DefaultTableModel modeloTabla;

    public PanelCaja(Ventana3 ventanaPrincipal, Churreria churreria, MongoDatabase baseDatos)
    {
        this.ventanaPrincipal = ventanaPrincipal;
        this.churreria = churreria;
        this.baseDatos = baseDatos; // Asignar la instancia de MongoDatabase
        misComponentesIniciales();
        actualizarTablaTickets();
    }

    private void misComponentesIniciales()
    {
        setLayout(new BorderLayout());

        JLabel etiquetaCaja = new JLabel("Caja", SwingConstants.CENTER);
        etiquetaCaja.setFont(new Font("Arial", Font.BOLD, 30)); // Cambia el tamaño de la fuente
        etiquetaCaja.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza
        add(etiquetaCaja, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"Fecha", "Importe Total"}, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        tablaTickets = new JTable(modeloTabla);
        tablaTickets.setFont(new Font("Arial", Font.BOLD, 13)); // Cambiar el tamaño de la fuente de la tabla
        tablaTickets.setRowHeight(30); // Cambiar la altura de las filas

        // Ajustar el renderizador de la tabla para centrar el texto
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaTickets.setDefaultRenderer(Object.class, centerRenderer);

        // Establecer el color de fondo del JTable a amarillo mostaza
        Color amarilloMostaza = new Color(255, 204, 0);
        tablaTickets.setBackground(amarilloMostaza);
        tablaTickets.setOpaque(true);

        // Cambiar el color de fondo del encabezado del JTable a amarillo mostaza
        JTableHeader header = tablaTickets.getTableHeader();
        header.setBackground(amarilloMostaza);
        header.setForeground(Color.BLACK); // Ajustar el color del texto del encabezado si es necesario
        header.setFont(new Font("Arial", Font.BOLD, 15)); // Cambiar el tamaño de la fuente del encabezado

        JScrollPane scrollPane = new JScrollPane(tablaTickets);
        scrollPane.getViewport().setBackground(amarilloMostaza); // Color de fondo del viewport del scroll pane
        add(scrollPane, BorderLayout.CENTER);

        BotonPersonalizado botonVolver = new BotonPersonalizado("Volver");
        botonVolver.addActionListener(e -> ventanaPrincipal.mostrarPanelMesasRestaurantes());
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelSur.setOpaque(false); // Hacer que el panel sur no sea opaco
        panelSur.add(botonVolver);
        add(panelSur, BorderLayout.SOUTH);
    }

    private void actualizarTablaTickets()
    {
        List<Ticket> tickets = churreria.getTickets();
        modeloTabla.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm:ss", new Locale("es", "ES"));

        MongoCollection<Document> coleccionTickets = baseDatos.getCollection("tickets");
        for (Document doc : coleccionTickets.find())
        {
            Ticket ticket = Ticket.fromDocument(doc);
            String fechaFormateada = sdf.format(ticket.getFecha());
            modeloTabla.addRow(new Object[]{fechaFormateada, String.format("%.2f€", ticket.getImporteTotal())});
        }
    }
}
