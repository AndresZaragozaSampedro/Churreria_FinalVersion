package GUI;

import DAO.EmpleadoDAO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelVerEmpleados extends PanelBase {
    private EmpleadoDAO empleadoDAO;
    private JTable tablaEmpleados;

    public PanelVerEmpleados(JFrame ventana, EmpleadoDAO empleadoDAO) {
        this.empleadoDAO = empleadoDAO;
        misComponentesIniciales(ventana);
    }

    private void misComponentesIniciales(JFrame ventana) {
        setLayout(new BorderLayout());

        // Crear y añadir la etiqueta en la parte superior (Norte)
        JLabel etiquetaTitulo = new JLabel("Lista de Empleados", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        etiquetaTitulo.setForeground(new Color(255, 204, 0)); // Color amarillo mostaza
        etiquetaTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0)); // Añadir borde vacío en la parte superior
        add(etiquetaTitulo, BorderLayout.NORTH);

        // Crear el JTable y su modelo
        String[] columnas = {"ID", "Usuario", "Contraseña"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEmpleados = new JTable(modeloTabla);
        tablaEmpleados.setShowHorizontalLines(false);
        tablaEmpleados.setShowVerticalLines(false);
        JScrollPane scrollPane = new JScrollPane(tablaEmpleados);
        add(scrollPane, BorderLayout.CENTER);

        // Crear el botón "Volver"
        BotonPersonalizado botonVolver = new BotonPersonalizado("Volver");
        botonVolver.addActionListener(e -> volverAPanelEmpleados(ventana));

        JPanel panelBotonVolver = new JPanel();
        panelBotonVolver.setOpaque(false);
        panelBotonVolver.add(botonVolver);

        add(panelBotonVolver, BorderLayout.SOUTH);

        // Cargar los datos de los empleados
        cargarDatosEmpleados();
    }

    private void cargarDatosEmpleados() {
        JsonObject respuesta = empleadoDAO.listarTodos();

        if (respuesta.get("estado").getAsString().equals("éxito")) {
            JsonArray empleados = respuesta.getAsJsonArray("datos");
            DefaultTableModel modeloTabla = (DefaultTableModel) tablaEmpleados.getModel();
            modeloTabla.setRowCount(0); // Limpiar los datos existentes

            for (int i = 0; i < empleados.size(); i++) {
                JsonObject empleado = empleados.get(i).getAsJsonObject();
                String id = empleado.has("id") ? empleado.get("id").getAsString() : "N/A";
                String usuario = empleado.has("username") ? empleado.get("username").getAsString() : "N/A";
                String contraseña = empleado.has("password") ? empleado.get("password").getAsString() : "N/A";
                modeloTabla.addRow(new Object[]{id, usuario, contraseña});
            }
        } else {
            JOptionPane.showMessageDialog(this, respuesta.get("mensaje").getAsString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAPanelEmpleados(JFrame ventana) {
        ventana.setContentPane(new Panel_Empleados(ventana, empleadoDAO));
        ventana.revalidate();
        ventana.repaint();
    }
}
