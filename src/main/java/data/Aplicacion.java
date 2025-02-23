package data;

import Churreria.Mesa;
import DAO.ProductoDao;
import DAO.ProductoDaoMongo;
import DataProductos.Producto;
import GUI.Ventana;
import GUI.Ventana2;
import GUI.Ventana3;
import Utilidades.Configuraciones;
import Utilidades.Respuestas;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import javax.swing.*;

public class Aplicacion
{
    private static MongoClient clienteMongo;
    private static Gestiona_BaseDatos_Empleados gestorBD;
    private static MongoDatabase baseDatos;

    public static void main(String[] args)
    {
        clienteMongo = MongoClients.create("mongodb://localhost:27017");
        baseDatos = clienteMongo.getDatabase("BaseDatosChurreria");
        gestorBD = new Gestiona_BaseDatos_Empleados(clienteMongo, "BaseDatosChurreria");

        //datosDePrueba();
        //datosPruebaPassword();
        //datosPruebaEmpleados();
        //crearProductoYAgregarAMesa();
        //crearProductoYAgregarAMesa2();
        //crearProductoYAgregarAMesa3();
        //actualizarProducto();
        //inicializarventana();
        //inicializarventana2();
        inicializarventana3();
    }

    private static void inicializarventana3()
    {
        SwingUtilities.invokeLater(() ->
        {
            Ventana3 ventana = new Ventana3();
            ventana.setVisible(true);
        });
    }

    private static void inicializarventana2()
    {
        SwingUtilities.invokeLater(() ->
        {
            Ventana2 ventana = new Ventana2();
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setVisible(true);
        });
    }

    private static void inicializarventana()
    {
        SwingUtilities.invokeLater(() ->
        {
            Ventana ventana = new Ventana();
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setVisible(true);
        });
    }

    private static void crearProductoYAgregarAMesa3()
    {
        Gestiona_BaseDatos_Churreria gestorBD = new Gestiona_BaseDatos_Churreria(baseDatos);

        Producto churro = new Producto("Churro", 0.45, "Delicioso churro", 0);

        JsonObject respuestaAddChurro = gestorBD.añadirProductoAColeccion(churro, "Churros");
        Gson gsong = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Respuesta añadir Churro: " + gsong.toJson(respuestaAddChurro));

        Mesa mesa1 = new Mesa(1);

        Producto productoMesa = gestorBD.buscarProductoPorNombre("Churro", "Churros");

        if (productoMesa != null)
        {
            mesa1.agregarProductoConCantidad(productoMesa, 4);
        }

        System.out.println("Productos en la mesa:\n" + gsong.toJson(mesa1.toJson()));

        gestorBD.cerrarConexion();
    }

    private static void crearProductoYAgregarAMesa2()
    {
        Gestiona_BaseDatos_Churreria gestorBD = new Gestiona_BaseDatos_Churreria(baseDatos);

        Producto churro = new Producto("Churro", 0.45, "Delicioso churro", 0);

        JsonObject respuestaAddChurro = gestorBD.añadirProductoAColeccion(churro, "Churros");
        Gson gsong = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Respuesta añadir Churro: " + gsong.toJson(respuestaAddChurro));

        Mesa mesa1 = new Mesa(1);

        Producto productoMesa = gestorBD.buscarProductoPorNombre("Churro", "Churros");

        if (productoMesa != null)
        {
            mesa1.agregarProductoConCantidad(productoMesa, 4);
        }

        System.out.println("Productos en la mesa:\n" + gsong.toJson(mesa1));

        gestorBD.cerrarConexion();
    }

    private static void crearProductoYAgregarAMesa()
    {
        Gestiona_BaseDatos_Churreria gestorBD = new Gestiona_BaseDatos_Churreria(baseDatos);

        Producto churro = new Producto("Churro", 0.45, "Delicioso churro", 0);

        JsonObject respuestaAddChurro = gestorBD.añadirProductoAColeccion(churro, "Churros");
        Gson gsong = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Respuesta añadir Churro: " + gsong.toJson(respuestaAddChurro));

        Mesa mesa1 = new Mesa(1);

        Producto productoMesa = gestorBD.buscarProductoPorNombre("Churro", "Churros");

        if (productoMesa != null)
        {
            mesa1.agregarProductoConCantidad(productoMesa, 4);
        }

        String mesaJson = gsong.toJson(mesa1);
        System.out.println("Productos en la mesa:\n" + mesaJson);

        gestorBD.cerrarConexion();
    }

    private static void actualizarProducto()
    {
        Gestiona_BaseDatos_Churreria gestorBD = new Gestiona_BaseDatos_Churreria(baseDatos);

        JsonObject respuestaActualizar = gestorBD.actualizarProducto("Churro", "Churro Grande", 0.60, "Delicioso churro grande", "Churros");
        Gson gsong = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Respuesta actualizar Churro: " + gsong.toJson(respuestaActualizar));

        gestorBD.cerrarConexion();
    }

    private static void datosPruebaEmpleados()
    {
        MongoClient clienteMongo = MongoClients.create("mongodb://localhost:27017");
        Gestiona_BaseDatos_Empleados gestorBD = new Gestiona_BaseDatos_Empleados(clienteMongo, "BaseDatosChurreria");

        Gson gsong = new GsonBuilder().setPrettyPrinting().create();

        JsonObject respuestaAdd = gestorBD.añadirEmpleado("Ireee", "M3ajogo!");

        System.out.println("respuesta añadir: " + gsong.toJson(respuestaAdd));

        JsonObject loginResponse = gestorBD.login("Ireee", "M3ajogo!");
        System.out.println("Login response: " + gsong.toJson(loginResponse));

        JsonObject datos = loginResponse.getAsJsonObject("datos");
        String token = datos.get("token").getAsString();
        boolean isValid = gestorBD.validarToken(token);
        System.out.println("Token válido: " + isValid);

        gestorBD.cerrarConexion();
    }

    private static void datosPruebaPassword()
    {
        String[] passwords = {
                "password",
                "Password1",
                "Password1!",
                "passW1!",
                "sol1!",
                "Valid@123",
                "noCaracterSpecial1",
                "NoNumero!",
                null,
        };

        for (String password : passwords)
        {
            boolean resultado = Configuraciones.esPasswordValido(password);
            System.out.println("Contraseña: " + password + " | Con resultado: " + resultado);
        }
    }

    private static void datosDePrueba()
    {
        Gestiona_BaseDatos_Churreria gestorDB = new Gestiona_BaseDatos_Churreria(baseDatos);
        gestorDB.crearNuevaColeccion("Churros");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try
        {
            ProductoDao productoDao = new ProductoDaoMongo(gestorDB);

            Producto producto1 = new Producto("Churro", 0.80, "Churro caliente y crujiente.", 1);
            Producto producto2 = new Producto("Churro de Chocolate", 1.00, "Churro cubierto con chocolate.", 1);

            JsonObject respuestaInsertar1 = productoDao.insertar(producto1, "Churros");
            System.out.println("Insertar producto 1: " + gson.toJson(respuestaInsertar1));

            JsonObject respuestaInsertar2 = productoDao.insertar(producto2, "Churros");
            System.out.println("Insertar producto 2: " + gson.toJson(respuestaInsertar2));

            JsonObject respuestaBuscar = productoDao.buscarPorNombre("Churro");
            System.out.println("Buscar producto 'Churro': " + gson.toJson(respuestaBuscar));

            JsonObject respuestaActualizar = productoDao.actualizar("Churro de Chocolate", "Churro de Chocolate Blanco", 1.50, "Delicioso churro de chocolate blanco", "Churros");
            System.out.println("Actualizar producto: " + gson.toJson(respuestaActualizar));

            JsonObject respuestaListar = productoDao.listarTodos("Churros");
            System.out.println("Lista de productos en 'Churros': " + gson.toJson(respuestaListar));

            JsonObject respuestaEliminar = productoDao.eliminarPorNombre("Churro de Chocolate Blanco", "Churros");
            System.out.println("Eliminar producto 'Churro de Chocolate Blanco': " + gson.toJson(respuestaEliminar));
        }
        catch (IllegalArgumentException e)
        {
            System.err.println("Error al añadir producto: " + e.getMessage());
        }
    }
}
