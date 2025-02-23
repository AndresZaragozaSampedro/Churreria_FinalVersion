package GUI;

import DAO.EmpleadoDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * @author Winderandrex
 */
public class Panel_Empleados extends PanelBase
{
    private EmpleadoDAO empleadoDAO;

    // CONSTRUCTOR
    public Panel_Empleados(JFrame ventana, EmpleadoDAO empleadoDAO)
    {
        this.empleadoDAO = empleadoDAO;
        misComponentesIniciales(ventana);
    }

    private void misComponentesIniciales(JFrame ventana)
    {
        setLayout(new BorderLayout());

        // Crear y añadir la etiqueta en la parte superior (Norte)
        JLabel etiquetaTitulo = new JLabel("Gestion de empleados", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        etiquetaTitulo.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza
        etiquetaTitulo.setBorder(new EmptyBorder(30, 0, 10, 0)); // Añadir borde vacío en la parte superior
        add(etiquetaTitulo, BorderLayout.NORTH);

        // Crear un panel para los botones con GridBagLayout
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false); // Para que el fondo del panel principal se vea a través del panel de botones
        panelBotones.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints restriccionesGridBag = new GridBagConstraints();
        restriccionesGridBag.fill = GridBagConstraints.BOTH;
        restriccionesGridBag.insets = new Insets(10, 10, 10, 10);

        // Crear y configurar los botones
        JButton botonAlta = new BotonPersonalizado("Alta de empleados");
        JButton botonBusqueda = new BotonPersonalizado("Busqueda de empleados");
        JButton botonActualizar = new BotonPersonalizado("Actualizar empleados");
        JButton botonBorrar = new BotonPersonalizado("Borrar empleado");
        JButton botonVer = new BotonPersonalizado("Ver Empleados");
        JButton botonVolver = new BotonPersonalizado("Volver");

        // Establecer tamaños preferidos para los botones
        Dimension botonGrande = new Dimension(200, 50);
        Dimension botonPequeno = new Dimension(150, 30);

        botonAlta.setPreferredSize(botonGrande);
        botonBusqueda.setPreferredSize(botonGrande);
        botonActualizar.setPreferredSize(botonGrande);
        botonBorrar.setPreferredSize(botonGrande);
        botonVer.setPreferredSize(botonPequeno);
        botonVolver.setPreferredSize(botonPequeno);

        // Primera fila, dos botones
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 0;
        restriccionesGridBag.weightx = 1.0; // Peso horizontal para expandir
        restriccionesGridBag.weighty = 1.0; // Peso vertical para expandir
        panelBotones.add(botonAlta, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.gridy = 0;
        panelBotones.add(botonBusqueda, restriccionesGridBag);

        // Segunda fila, dos botones
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 1;
        panelBotones.add(botonActualizar, restriccionesGridBag);

        restriccionesGridBag.gridx = 1;
        restriccionesGridBag.gridy = 1;
        panelBotones.add(botonBorrar, restriccionesGridBag);

        // Tercera fila, un botón "Ver Empleados"
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 2;
        restriccionesGridBag.gridwidth = 2;  // El botón "Ver Empleados" ocupará dos columnas
        restriccionesGridBag.weightx = 0.0; // Peso horizontal menor para el botón "Ver Empleados"
        restriccionesGridBag.weighty = 0.0; // Peso vertical menor para el botón "Ver Empleados"
        panelBotones.add(botonVer, restriccionesGridBag);

        // Cuarta fila, un botón "Volver"
        restriccionesGridBag.gridx = 0;
        restriccionesGridBag.gridy = 3;
        restriccionesGridBag.gridwidth = 2;  // El botón "Volver" ocupará dos columnas
        restriccionesGridBag.weightx = 0.0; // Peso horizontal menor para el botón "Volver"
        restriccionesGridBag.weighty = 0.0; // Peso vertical menor para el botón "Volver"
        panelBotones.add(botonVolver, restriccionesGridBag);

        // Añadir ActionListener al botón "Alta de Empleados"
        botonAlta.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                irAlPanelDeAltas(ventana);
            }
        });

        // Añadir ActionListener al botón "Busqueda de Empleados"
        botonBusqueda.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                irAlPanelDeBusquedas(ventana);
            }
        });

        // Añadir ActionListener al boton ActualizarEmpleados
        botonActualizar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                irAlPanelActualizarEmpleados(ventana);
            }
        });

        // Añadir ActionListener al boton borrarEmpleados
        botonBorrar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                irAlPanelDeBorrar(ventana);
            }
        });

        // Añadir ActionListener al botón "Ver Empleados"
        botonVer.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                irAlPanelDeVerEmpleados(ventana);
            }
        });

        botonActualizar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                irAlPanelActualizarEmpleados(ventana);
            }
        });

        // Añadir ActionListener al botón "Volver"
        botonVolver.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                volverAPanelPrincipal(ventana);
            }
        });

        add(panelBotones, BorderLayout.CENTER);
    }

    private void irAlPanelDeAltas(JFrame ventana)
    {
        ventana.setContentPane(new PanelAltaEmpleado(ventana, empleadoDAO));
        ventana.revalidate();
        ventana.repaint();
    }

    private void irAlPanelDeBusquedas(JFrame ventana)
    {
        ventana.setContentPane(new PanelBusquedaEmpleado(ventana, empleadoDAO));
        ventana.revalidate();
        ventana.repaint();
    }


    private void irAlPanelDeBorrar(JFrame ventana)
    {
        ventana.setContentPane(new PanelBorrarEmpleado(ventana, empleadoDAO));
        ventana.revalidate();
        ventana.repaint();
    }

    private void irAlPanelDeVerEmpleados(JFrame ventana)
    {
        ventana.setContentPane(new PanelVerEmpleados(ventana, empleadoDAO));
        ventana.revalidate();
        ventana.repaint();
    }

    private void irAlPanelActualizarEmpleados(JFrame ventana)
    {
        ventana.setContentPane(new PanelActualizarEmpleado(ventana, empleadoDAO));
        ventana.revalidate();
        ventana.repaint();
    }

    private void volverAPanelPrincipal(JFrame ventanaPrincipal)
    {
        // Establecer el panel principal como el contenido del JFrame
        if (ventanaPrincipal instanceof Ventana3)
        {
            Ventana3 ventana = (Ventana3) ventanaPrincipal;
            ventana.mostrarPanelPrincipal();
        }
    }
}
