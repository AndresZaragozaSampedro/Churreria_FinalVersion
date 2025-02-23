package GUI;

import DAO.EmpleadoDAO;
import com.google.gson.JsonObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Winderandrex
 */
public class PanelBorrarEmpleado extends PanelBase
{
    private JFrame ventanaPrincipal;
    private JComboBox<String> comboEmpleados;
    private EmpleadoDAO empleadoDAO;

    // CONSTRUCTOR
    public PanelBorrarEmpleado(JFrame ventana, EmpleadoDAO empleadoDAO)
    {
        this.ventanaPrincipal = ventana;
        this.empleadoDAO = empleadoDAO;
        misComponentesIniciales();
    }

    private void misComponentesIniciales()
    {
        setLayout(new BorderLayout());

        // Crear y añadir la etiqueta en la parte superior (Norte)
        JLabel etiquetaTitulo = new JLabel("Borrar Empleados", SwingConstants.CENTER);
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
        JLabel etiquetaEmpleado = new JLabel("Nombre Usuario:");
        etiquetaEmpleado.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaEmpleado.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza

        comboEmpleados = new JComboBox<>();
        cargarEmpleadosEnComboBox();
        comboEmpleados.setPreferredSize(new Dimension(200, 30)); // Ajustar el tamaño del combo box

        // Añadir la etiqueta y campo de texto de Empleado
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 0;
        restriccionesGridBag.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaEmpleado, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.anchor = GridBagConstraints.WEST;
        panelCentral.add(comboEmpleados, restriccionesGridBag);

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
        BotonPersonalizado botonEliminar = new BotonPersonalizado("Eliminar");

        gbcBotones.gridx = 0;
        panelBotones.add(botonCancelar, gbcBotones);

        gbcBotones.gridx = 1;
        panelBotones.add(botonEliminar, gbcBotones);

        // Añadir ActionListener al botón "Cancelar"
        botonCancelar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                volverAlPanelEmpleados();
            }
        });

        // Añadir ActionListener al botón "Eliminar"
        botonEliminar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eliminarEmpleado();
            }
        });

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarEmpleadosEnComboBox()
    {
        List<String> empleados = empleadoDAO.obtenerTodosLosNombresDeEmpleados();
        for (String nombre : empleados)
        {
            comboEmpleados.addItem(nombre);
        }
    }

    private void eliminarEmpleado()
    {
        String empleadoSeleccionado = (String) comboEmpleados.getSelectedItem();
        JsonObject respuesta = empleadoDAO.eliminarPorNombre(empleadoSeleccionado);

        if (respuesta.get("estado").getAsString().equals("éxito"))
        {
            JOptionPane.showMessageDialog(this, "Empleado eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            volverAlPanelEmpleados();
        } else
        {
            JOptionPane.showMessageDialog(this, respuesta.get("mensaje").getAsString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAlPanelEmpleados()
    {
        if (ventanaPrincipal instanceof Ventana3)
        {
            Ventana3 ventana = (Ventana3) ventanaPrincipal;
            ventana.mostrarPanelEmpleados();
        }
    }
}
