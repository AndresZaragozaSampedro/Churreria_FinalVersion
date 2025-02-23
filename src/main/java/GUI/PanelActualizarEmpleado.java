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
public class PanelActualizarEmpleado extends PanelBase
{
    private JFrame ventanaPrincipal;
    private JComboBox<String> comboEmpleados;
    private JPasswordField campoNuevaPassword;
    private EmpleadoDAO empleadoDAO;

    // CONSTRUCTOR
    public PanelActualizarEmpleado(JFrame ventana, EmpleadoDAO empleadoDAO)
    {
        this.ventanaPrincipal = ventana;
        this.empleadoDAO = empleadoDAO;
        misComponentesIniciales();
    }

    private void misComponentesIniciales()
    {
        setLayout(new BorderLayout());

        // Crear y añadir la etiqueta en la parte superior (Norte)
        JLabel etiquetaTitulo = new JLabel("Actualizar Empleados", SwingConstants.CENTER);
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
        JLabel etiquetaEmpleado = new JLabel("Empleado a Actualizar:");
        etiquetaEmpleado.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaEmpleado.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza

        comboEmpleados = new JComboBox<>();
        cargarEmpleadosEnComboBox();

        JLabel etiquetaNuevaPassword = new JLabel("Nueva Password:");
        etiquetaNuevaPassword.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaNuevaPassword.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza

        campoNuevaPassword = new JPasswordField(20); // Tamaño aumentado
        campoNuevaPassword.setFont(new Font("Arial", Font.PLAIN, 18));
        campoNuevaPassword.setEchoChar('*'); // Usar asteriscos para ocultar la contraseña

        JCheckBox mostrarPassword = new JCheckBox("Mostrar contraseña");
        mostrarPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        mostrarPassword.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza
        mostrarPassword.setOpaque(false);

        mostrarPassword.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (mostrarPassword.isSelected())
                {
                    campoNuevaPassword.setEchoChar((char) 0); // Mostrar la contraseña
                }
                else
                {
                    campoNuevaPassword.setEchoChar('*'); // Ocultar la contraseña con asteriscos
                }
            }
        });

        // Añadir la etiqueta y campo de texto de Empleado
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 0;
        restriccionesGridBag.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaEmpleado, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.anchor = GridBagConstraints.WEST;
        panelCentral.add(comboEmpleados, restriccionesGridBag);

        // Añadir la etiqueta y campo de texto de Nueva Password
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 1;
        restriccionesGridBag.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaNuevaPassword, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.anchor = GridBagConstraints.WEST;
        panelCentral.add(campoNuevaPassword, restriccionesGridBag);

        // Añadir el checkbox de mostrar contraseña
        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.gridy = 2;
        restriccionesGridBag.anchor = GridBagConstraints.WEST;
        panelCentral.add(mostrarPassword, restriccionesGridBag);

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
                volverAlPanelEmpleados();
            }
        });

        // Añadir ActionListener al botón "Validar"
        botonValidar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                actualizarEmpleado();
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

    private void actualizarEmpleado()
    {
        String empleadoSeleccionado = (String) comboEmpleados.getSelectedItem();
        String nuevaPassword = new String(campoNuevaPassword.getPassword());
        JsonObject respuesta = empleadoDAO.actualizarEmpleado(empleadoSeleccionado, nuevaPassword);

        if (respuesta.get("estado").getAsString().equals("éxito"))
        {
            JOptionPane.showMessageDialog(this, "Empleado actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            volverAlPanelEmpleados();
        }
        else
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
