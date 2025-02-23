package DAO;

import DataProductos.Producto;
import com.google.gson.JsonObject;
import org.json.JSONObject;

/*
    Esta clase gestiona el CRUD de los productos, está centralizado para la eficiencia del código y del programa
 */

public interface ProductoDao
{
    JsonObject insertar(Producto producto, String nombreColeccion);
    JsonObject actualizar(String nombreOriginal, String nuevoNombre, double nuevoPrecio, String nuevaDescripcion, String nombreColeccion);
    JsonObject buscarPorNombre(String nombre);
    JsonObject listarTodos(String nombreColeccion);
    JsonObject eliminarPorNombre(String nombre, String nombreColeccion);
}
