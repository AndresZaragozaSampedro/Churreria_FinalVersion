package DAO;

import data.Gestiona_BaseDatos_Churreria;
import DataProductos.Producto;
import com.google.gson.JsonObject;

/**
 * Implementación específica de MongoDB para el acceso y manipulación de datos de productos.
 */

public class ProductoDaoMongo implements ProductoDao
{
    private Gestiona_BaseDatos_Churreria gestorDB;

    /**
     * Constructor que inicializa con una instancia de la clase de gestión de base de datos.
     *
     * @param gestorDB Instancia del gestor de base de datos para operaciones de MongoDB.
     */

//CONSTRUCTOR
    public ProductoDaoMongo(Gestiona_BaseDatos_Churreria gestorDB)
    {
        this.gestorDB = gestorDB;
    }


    /**
     * Inserta un nuevo producto en la colección especificada.
     *
     * @param producto        El producto a insertar.
     * @param nombreColeccion El nombre de la colección donde se debe insertar el producto.
     * @return JsonObject que representa el resultado de la operación, éxito o error.
     */


    @Override
    public JsonObject insertar(Producto producto, String nombreColeccion)
    {
        return gestorDB.añadirProductoAColeccion(producto, nombreColeccion);
    }

    /**
     * Actualiza un producto existente, identificado por su nombre original.
     *
     * @param nombreOriginal   El nombre actual del producto.
     * @param nuevoNombre      El nuevo nombre del producto.
     * @param nuevoPrecio      El nuevo precio del producto.
     * @param nuevaDescripcion La nueva descripción del producto.
     * @param nombreColeccion  La colección donde reside el producto.
     * @return JsonObject que representa el resultado de la operación, éxito o error.
     */

    @Override
    public JsonObject actualizar(String nombreOriginal, String nuevoNombre, double nuevoPrecio, String nuevaDescripcion, String nombreColeccion)
    {
        return gestorDB.actualizarProducto(nombreOriginal, nuevoNombre, nuevoPrecio, nuevaDescripcion, nombreColeccion);
    }

    /**
     * Busca un producto por su nombre en todas las colecciones de la base de datos.
     *
     * @param nombre El nombre del producto a buscar.
     * @return JsonObject con los detalles del producto encontrado o un mensaje de error si no se encuentra.
     */

    @Override
    public JsonObject buscarPorNombre(String nombre)
    {
        return gestorDB.buscarProductoEnTodasLasColecciones(nombre);
    }

    /**
     * Lista todos los productos en una colección específica.
     *
     * @param nombreColeccion El nombre de la colección de la cual obtener todos los productos.
     * @return JsonObject que contiene una lista de todos los productos o un mensaje de error si la colección está vacía.
     */

    @Override
    public JsonObject listarTodos(String nombreColeccion)
    {
        return gestorDB.listarTodos(nombreColeccion);
    }

    /**
     * Elimina un producto por su nombre de una colección específica.
     *
     * @param nombre          El nombre del producto a eliminar.
     * @param nombreColeccion La colección de donde se debe eliminar el producto.
     * @return JsonObject que indica el resultado de la operación, éxito o error.
     */

    @Override
    public JsonObject eliminarPorNombre(String nombre, String nombreColeccion)
    {
        return gestorDB.eliminarProductoPorNombre(nombre, nombreColeccion);
    }

}
