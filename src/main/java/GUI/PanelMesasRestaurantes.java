package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import com.mongodb.client.MongoCollection;

import Churreria.Churreria;

public class PanelMesasRestaurantes extends PanelBase
{
    private MongoDatabase baseDatos;
    private Ventana3 ventanaPrincipal;
    private Churreria churreria;

    public PanelMesasRestaurantes(Ventana3 ventanaPrincipal, MongoDatabase baseDatos, Churreria churreria)
    {
        this.ventanaPrincipal = ventanaPrincipal;
        this.baseDatos = baseDatos;
        this.churreria = churreria;
        misComponentesIniciales();
    }

    private void misComponentesIniciales()
    {
        setLayout(new BorderLayout());

        // Parte North
        JLabel etiquetaMesas = new JLabel("Mesas de la Churreria", SwingConstants.CENTER);
        etiquetaMesas.setFont(new Font("Arial", Font.BOLD, 24));
        etiquetaMesas.setForeground(new Color(255, 204, 0));
        etiquetaMesas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Remove extra spaces
        add(etiquetaMesas, BorderLayout.NORTH);

        // Parte Center con GridBagLayout
        JPanel panelMesas = new JPanel(new GridBagLayout());
        panelMesas.setOpaque(false); // Hacer que el panel central no sea opaco
        panelMesas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Remove extra spaces
        JScrollPane scrollPane = new JScrollPane(panelMesas);
        scrollPane.setOpaque(false); // Hacer que el scroll pane no sea opaco
        scrollPane.getViewport().setOpaque(false); // Hacer que el viewport del scroll pane no sea opaco
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        add(scrollPane, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        MongoCollection<Document> coleccionMesas = baseDatos.getCollection("Mesas");
        MongoIterable<Document> documentos = coleccionMesas.find();
        int x = 0, y = 0;
        for (Document doc : documentos)
        {
            String nombreMesa = doc.getString("nombre");
            BotonPersonalizado botonMesa = new BotonPersonalizado(nombreMesa);
            botonMesa.setPreferredSize(new Dimension(200, 100)); // Ajustar el tamaño de los botones
            botonMesa.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    ventanaPrincipal.mostrarPanelGestionMesas(nombreMesa);
                }
            });

            gbc.gridx = x;
            gbc.gridy = y;
            panelMesas.add(botonMesa, gbc);

            x++;
            if (x == 2)
            { // Cambiar a una nueva fila después de 2 botones
                x = 0;
                y++;
            }
        }

        // Parte South
        BotonPersonalizado botonVolver = new BotonPersonalizado("Volver");
        botonVolver.setPreferredSize(new Dimension(150, 70));
        botonVolver.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ventanaPrincipal.mostrarPanelPrincipal();
            }
        });

        BotonPersonalizado botonVerCaja = new BotonPersonalizado("Ver Caja");
        botonVerCaja.setPreferredSize(new Dimension(150, 70));
        botonVerCaja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventanaPrincipal.mostrarPanelCaja();
            }
        });

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelSur.setOpaque(false); // Hacer que el panel sur no sea opaco
        panelSur.setBorder(new EmptyBorder(10, 10, 10, 10)); // Remove extra spaces
        panelSur.add(botonVolver);
        panelSur.add(botonVerCaja); // Añadir botonVerCaja al panelSur
        add(panelSur, BorderLayout.SOUTH); // Añadir panelSur al BorderLayout.SOUTH
    }
}
