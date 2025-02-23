package GUI;

import DAO.EmpleadoDAO;
import com.google.gson.JsonObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author Winderandrex
 */
public class Panel_Login extends PanelBase {
    private JFrame ventana;
    private EmpleadoDAO empleadoDAO;
    private JTextField textFieldUsuario;
    private JPasswordField passwordField;

    public Panel_Login(JFrame ventana, EmpleadoDAO empleadoDAO) {
        this.ventana = ventana;
        this.empleadoDAO = empleadoDAO;
        misComponentesIniciales();
    }

    private void misComponentesIniciales() {
        setLayout(new BorderLayout());

        // Etiqueta de título
        JLabel etiquetaTitulo = new JLabel("Login Empleados", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        etiquetaTitulo.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza
        etiquetaTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0)); // Añadir borde vacío en la parte superior
        add(etiquetaTitulo, BorderLayout.NORTH);

        // Panel central con GridBagLayout
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false); // Hacer que el panel central no sea opaco
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.insets = new Insets(10, 10, 10, 10);

        // Configuración de los JLabel
        Font fontLabels = new Font("Arial", Font.BOLD, 18); // Misma fuente y tamaño que en otros paneles
        Color colorAmarilloMostaza = new Color(255, 204, 0);

        // Primera fila: Usuario
        JLabel etiquetaUsuario = new JLabel("Usuario:");
        etiquetaUsuario.setFont(fontLabels);
        etiquetaUsuario.setForeground(colorAmarilloMostaza);
        textFieldUsuario = new JTextField(20);

        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaUsuario, restricciones);

        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        panelCentral.add(textFieldUsuario, restricciones);

        // Segunda fila: Password
        JLabel etiquetaPassword = new JLabel("Password:");
        etiquetaPassword.setFont(fontLabels);
        etiquetaPassword.setForeground(colorAmarilloMostaza);
        passwordField = new JPasswordField(20);

        restricciones.gridx = 0;
        restricciones.gridy = 1;
        restricciones.anchor = GridBagConstraints.EAST;
        panelCentral.add(etiquetaPassword, restricciones);

        restricciones.gridx = 1;
        restricciones.anchor = GridBagConstraints.WEST;
        panelCentral.add(passwordField, restricciones);

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

        BotonPersonalizado botonVolver = new BotonPersonalizado("Volver");
        BotonPersonalizado botonEntrar = new BotonPersonalizado("Entrar");

        gbcBotones.gridx = 0;
        panelBotones.add(botonVolver, gbcBotones);

        gbcBotones.gridx = 1;
        panelBotones.add(botonEntrar, gbcBotones);

        // Añadir ActionListener al botón "Volver"
        botonVolver.addActionListener(e -> volverAlPanelPrincipal());

        // Añadir ActionListener al botón "Entrar"
        botonEntrar.addActionListener(e -> entrar());

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void entrar() {
        String username = textFieldUsuario.getText();
        String password = new String(passwordField.getPassword());

        JsonObject respuesta = empleadoDAO.login(username, password);

        if (respuesta.get("estado").getAsString().equals("éxito")) {
            JOptionPane.showMessageDialog(this, "Login exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            // Redirigir al PanelMesasRestaurantes
            ((Ventana3) ventana).mostrarPanelMesasRestaurantes();
        } else {
            JOptionPane.showMessageDialog(this, respuesta.get("mensaje").getAsString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAlPanelPrincipal() {
        ((Ventana3) ventana).mostrarPanelPrincipal();
    }
}
