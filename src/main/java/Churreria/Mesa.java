package Churreria;

import DataProductos.Producto;
import Utilidades.Configuraciones;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una mesa en la churrería.
 * Maneja la lista de productos y sus cantidades.
 */
public class Mesa
{
    private int numero;
    private List<Producto> productos;

    public Mesa(int numero)
    {
        this.numero = numero;
        this.productos = new ArrayList<>();
    }

    public int getNumero()
    {
        return numero;
    }

    public List<Producto> getProductos()
    {
        return productos;
    }

    /**
     * Agrega un producto con una cantidad específica a la mesa.
     * Si el producto ya existe, suma la cantidad.
     * @param producto Producto a agregar.
     * @param cantidad Cantidad del producto.
     */
    public void agregarProductoConCantidad(Producto producto, int cantidad) {
        for (Producto p : productos)
        {
            if (Configuraciones.sonNombresSimilares(p.getNombre(), producto.getNombre()))
            {
                p.setCantidad(p.getCantidad() + cantidad);
                return;
            }
        }
        productos.add(new Producto(producto.getNombre(), producto.getPrecio(), producto.getDescripcion(), cantidad));
    }

    /**
     * Calcula el total de la mesa sumando el precio por cantidad de cada producto.
     * @return Total de la mesa.
     */
    public double calcularTotal()
    {
        return productos.stream().mapToDouble(p -> p.getPrecio() * p.getCantidad()).sum();
    }

    /**
     * Limpia la lista de productos de la mesa.
     */
    public void limpiarMesa()
    {
        productos.clear();
    }

    /**
     * Convierte la mesa a un objeto JSON para representación.
     * @return Representación JSON de la mesa.
     */
    public JsonObject toJson()
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("numero", numero);

        JsonArray productosArray = new JsonArray();
        for (Producto producto : productos)
        {
            JsonObject productoJson = new JsonObject();
            productoJson.addProperty("nombre", producto.getNombre());
            productoJson.addProperty("precio", producto.getPrecio());
            productoJson.addProperty("descripcion", producto.getDescripcion());
            productoJson.addProperty("cantidad", producto.getCantidad());
            productosArray.add(productoJson);
        }
        jsonObject.add("productos", productosArray);

        return jsonObject;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Mesa ").append(numero).append(":\n");
        for (Producto p : productos)
        {
            sb.append("\t").append(p.toString()).append("\n");
        }
        return sb.toString();
    }
}
