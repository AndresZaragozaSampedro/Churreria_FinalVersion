package GUI;

import DAO.EmpleadoDAO;
import com.google.gson.JsonObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * @author Winderandrex
 */
public class PanelAltaEmpleado extends PanelBase
{
    // ATRIBUTOS
    private JFrame ventanaPrincipal;
    private JTextField campoUsuario;
    private JPasswordField campoPassword;
    private EmpleadoDAO empleadoDAO;

    // CONSTRUCTOR
    public PanelAltaEmpleado(JFrame ventana, EmpleadoDAO empleadoDAO)
    {
        this.ventanaPrincipal = ventana;
        this.empleadoDAO = empleadoDAO;
        misComponentesIniciales();
    }

    private void misComponentesIniciales()
    {
        setLayout(new BorderLayout());

        // Crear y añadir la etiqueta en la parte superior (Norte)
        JLabel etiquetaTitulo = new JLabel("Alta de Empleados", SwingConstants.CENTER);
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
        JLabel etiquetaUsuario = new JLabel("Usuario:");
        etiquetaUsuario.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaUsuario.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza

        campoUsuario = new JTextField(20); // Tamaño aumentado
        campoUsuario.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel etiquetaPassword = new JLabel("Contraseña:");
        etiquetaPassword.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaPassword.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza

        campoPassword = new JPasswordField(20); // Tamaño aumentado
        campoPassword.setFont(new Font("Arial", Font.PLAIN, 18));
        campoPassword.setEchoChar('*'); // Usar asteriscos para ocultar la contraseña

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
                    campoPassword.setEchoChar((char) 0); // Mostrar la contraseña
                } else
                {
                    campoPassword.setEchoChar('*'); // Ocultar la contraseña con asteriscos
                }
            }
        });

        // Añadir la etiqueta y campo de texto de Usuario
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 0;
        restriccionesGridBag.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaUsuario, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.anchor = GridBagConstraints.WEST;
        panelCentral.add(campoUsuario, restriccionesGridBag);

        // Añadir la etiqueta y campo de texto de Contraseña
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 1;
        restriccionesGridBag.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaPassword, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.anchor = GridBagConstraints.WEST;
        panelCentral.add(campoPassword, restriccionesGridBag);

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
                validarEmpleado();
            }
        });

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void validarEmpleado()
    {
        String username = campoUsuario.getText();
        String password = new String(campoPassword.getPassword());
        JsonObject respuesta = empleadoDAO.insertarEmpleado(username, password);

        if (respuesta.get("estado").getAsString().equals("éxito"))
        {
            JOptionPane.showMessageDialog(this, "Empleado añadido correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
