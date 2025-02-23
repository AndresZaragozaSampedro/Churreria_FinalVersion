package data;

import Utilidades.Configuraciones;
import DataProductos.Producto;
import Utilidades.Respuestas;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class Gestiona_BaseDatos_Churreria
{
    private MongoClient clienteMongo;
    private MongoDatabase baseDatos;


    public Gestiona_BaseDatos_Churreria(MongoDatabase baseDatos)
    {
        this.baseDatos = baseDatos;
    }

    /**
     * Conecta con la base de datos MongoDB.
     *
     * @param uri             El URI de conexión a MongoDB.
     * @param nombreBaseDatos El nombre de la base de datos a la que conectar (Compass)
     */
    public void conectarConBaseDatos(String uri, String nombreBaseDatos)
    {
        if (Configuraciones.esNombreValido(nombreBaseDatos))
        {
            // Establecer conexión con MongoDB
            clienteMongo = MongoClients.create(uri);

            // Seleccionar la base de datos
            baseDatos = clienteMongo.getDatabase(nombreBaseDatos);
            System.out.println("Conectado a la base de datos: " + nombreBaseDatos);
        }
        else
        {
            System.out.println("Nombre de base de datos inválido: " + nombreBaseDatos);
        }
    }

    /**
     * Crea una nueva colección en la base de datos.
     *
     * @param nombreColeccion El nombre de la nueva colección a crear.
     */
    public void crearNuevaColeccion(String nombreColeccion)
    {
        // Validamos que no puedan poner un nombre vacio.
        if (nombreColeccion == null || nombreColeccion.trim().isEmpty())
        {
            System.out.println("El nombre de la colección no puede estar vacío.");
            return;
        }

        // Validamos que la coleccion no tenga simbolos que no contemplamos.
        if (nombreColeccion.matches(".*[/.\"$*<>:|? ].*"))
        {
            System.out.println("El nombre de la colección contiene caracteres no permitidos.");
            return;
        }

        // Hemos investigado y resulta que mongo no deja que las colecciones tengan mas de 64 caracteres
        if (nombreColeccion.length() > 64)
        {
            System.out.println("El nombre de la colección es demasiado largo.");
            return;
        }

        // Verificamos si existe ya esta coleccion, (ignoramos entre mayusculas y minusculas)
        MongoIterable<String> colecciones = baseDatos.listCollectionNames();

        for (String nombre : colecciones)
        {
            if (nombre.equalsIgnoreCase(nombreColeccion))
            {
                System.out.println("La colección ya existe: " + nombreColeccion);
                return;
            }
        }

        // Creamos la coleccion
        baseDatos.createCollection(nombreColeccion);
        System.out.println("Colección creada con éxito: " + nombreColeccion);
    }

    /**
     * Añadimos un producto a una colección específica en la base de datos de MongoDB.
     *
     * @param producto        El producto a añadir a la colección.
     * @param nombreColeccion El nombre de la colección donde se añadirá el producto.
     * @return JsonObject Representa el estado de la operación, éxito o error con mensaje correspondiente.
     */
//    public JsonObject añadirProductoAColeccion(Producto producto, String nombreColeccion) {
//        try {
//            // Accedemos a la colección especificada dentro de la base de datos.
//            MongoCollection<Document> coleccion = baseDatos.getCollection(nombreColeccion);
//
//            // Verificamos que el nombre del producto no esté vacío y que tenga una longitud adecuada.
//            if (producto.getNombre() == null || producto.getNombre().isEmpty() || producto.getNombre().length() <= 3) {
//                return Respuestas.respuestaErrorGson("El nombre del producto debe tener más de 3 caracteres y no puede estar vacío.");
//            }
//
//            // Confirmamos que el precio del producto esté dentro del rango permitido.
//            if (producto.getPrecio() < 0.30 || producto.getPrecio() > 50.00) {
//                return Respuestas.respuestaErrorGson("El precio del producto debe estar entre 0.30 y 50.00 euros.");
//            }
//
//            // Verificamos que la cantidad sea válida
//            if (producto.getCantidad() < 0) {
//                return Respuestas.respuestaErrorGson("La cantidad del producto no puede ser negativa.");
//            }
//
//            // Buscamos si ya existe un producto con el mismo nombre en la colección.
//            Document productoExistente = coleccion.find(Filters.eq("nombre", producto.getNombre())).first();
//            if (productoExistente != null) {
//                return Respuestas.respuestaErrorGson("El producto ya existe: " + producto.getNombre());
//            }
//
//            // Creamos un nuevo documento con los detalles del producto para insertarlo en la base de datos.
//            Document nuevoProducto = new Document("nombre", producto.getNombre())
//                    .append("precio", producto.getPrecio())
//                    .append("descripcion", producto.getDescripcion())
//                    .append("cantidad", producto.getCantidad());
//
//            // Insertamos el nuevo documento en la colección.
//            coleccion.insertOne(nuevoProducto);
//
//            // Preparamos los datos del producto añadido para devolverlos como parte de la respuesta exitosa.
//            JsonObject datosProducto = new JsonObject();
//            datosProducto.addProperty("nombre", producto.getNombre());
//            datosProducto.addProperty("precio", producto.getPrecio());
//            datosProducto.addProperty("descripcion", producto.getDescripcion());
//            datosProducto.addProperty("cantidad", producto.getCantidad());
//
//            // Devolvemos un objeto JSON con la confirmación del éxito de la operación.
//            return Respuestas.respuestaExitosaGson(datosProducto);
//        } catch (MongoException me) {
//            // Manejamos errores específicos de MongoDB.
//            return Respuestas.respuestaErrorGson("Error al interactuar con la base de datos: " + me.getMessage());
//        } catch (Exception e) {
//            // Manejamos cualquier otro tipo de error inesperado.
//            return Respuestas.respuestaErrorGson("Error inesperado: " + e.getMessage());
//        }
//    }
    public JsonObject añadirProductoAColeccion(Producto producto, String nombreColeccion)
    {
        try
        {
            MongoCollection<Document> coleccion = baseDatos.getCollection(nombreColeccion);

            // Leer los valores mínimos y máximos desde la colección 'constantes'
            MongoCollection<Document> coleccionConstantes = baseDatos.getCollection("constantes");
            Document documentoMinimo = coleccionConstantes.find(Filters.eq("nombre", "minimo")).first();
            Document documentoMaximo = coleccionConstantes.find(Filters.eq("nombre", "maximo")).first();

            if (documentoMinimo == null || documentoMaximo == null)
            {
                return Respuestas.respuestaErrorGson("No se encontraron los valores mínimos y máximos en la colección 'constantes'.");
            }

            double precioMinimo;
            double precioMaximo;

            if(documentoMinimo.get("valor") instanceof String)
            {
                precioMinimo = Double.parseDouble((String) documentoMinimo.get("valor"));
            }
            else
            {
                precioMinimo = documentoMinimo.getDouble("valor");
            }

            if(documentoMaximo.get("valor") instanceof String)
            {
                precioMaximo = Double.parseDouble((String) documentoMaximo.get("valor"));
            }
            else
            {
                precioMaximo = documentoMaximo.getDouble("valor");
            }

            // Eliminamos espacios en blanco en el campo nombre
            String nombreProducto = producto.getNombre() != null ? producto.getNombre().trim() : null;

            if (nombreProducto == null || nombreProducto.isEmpty() || nombreProducto.length() <= 3)
            {
                return Respuestas.respuestaErrorGson("El nombre del producto debe tener más de 3 caracteres y no puede estar vacío.");
            }
            if (producto.getPrecio() < precioMinimo || producto.getPrecio() > precioMaximo)
            {
                return Respuestas.respuestaErrorGson("El precio del producto debe estar entre " + precioMinimo + " y " + precioMaximo + " euros.");
            }

            Document productoExistente = coleccion.find(Filters.eq("nombre", nombreProducto)).first();
            if (productoExistente != null)
            {
                return Respuestas.respuestaErrorGson("El producto ya existe: " + nombreProducto);
            }

            // Asegurarse de que la descripción no sea nula
            String descripcion = (producto.getDescripcion() != null) ? producto.getDescripcion().trim() : "";

            Document nuevoProducto = new Document("nombre", nombreProducto)
                    .append("precio", producto.getPrecio())
                    .append("descripcion", descripcion);

            coleccion.insertOne(nuevoProducto);

            JsonObject datosProducto = new JsonObject();
            datosProducto.addProperty("nombre", nombreProducto);
            datosProducto.addProperty("precio", producto.getPrecio());
            datosProducto.addProperty("descripcion", descripcion);

            return Respuestas.respuestaExitosaGson(datosProducto);
        }
        catch (MongoException me)
        {
            return Respuestas.respuestaErrorGson("Error al interactuar con la base de datos: " + me.getMessage());
        }
        catch (Exception e)
        {
            return Respuestas.respuestaErrorGson("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Actualiza los detalles de un producto existente en la base de datos.
     *
     * @param nombreOriginal   = nombre actual del producto antes de la actualización.
     * @param nuevoNombre      = nuevo nombre que se asignará al producto.
     * @param nuevoPrecio      = nuevo precio que se asignará al producto.
     * @param nuevaDescripcion = nueva descripción que se asignará al producto.
     * @param nombreColeccion  = nombre de la colección donde se encuentra el producto a actualizar.
     * @return JsonObject Un objeto JSON que indica el resultado de la operación, ya sea éxito o error.
     */
    public JsonObject actualizarProducto(String nombreOriginal, String nuevoNombre, double nuevoPrecio, String nuevaDescripcion, String nombreColeccion)
    {
        try
        {
            //Eliminamos los espacios que nos puedan meter al principio y al final del nuveoNombre
            nuevoNombre = nuevoNombre != null ? nuevoNombre.trim() : null;

            // Validamos que el nuevo nombre no esté vacío ni contenga caracteres no permitidos.
            if (nuevoNombre == null || nuevoNombre.isEmpty() || nuevoNombre.matches("[/\\.\"$*<>:|?]+"))
            {
                return Respuestas.respuestaErrorGson("Nombre del producto contiene caracteres inválidos o está vacío.");
            }

            // Leer los valores mínimos y máximos desde la colección 'constantes'
            MongoCollection<Document> coleccionConstantes = baseDatos.getCollection("constantes");
            Document documentoMinimo = coleccionConstantes.find(Filters.eq("nombre", "minimo")).first();
            Document documentoMaximo = coleccionConstantes.find(Filters.eq("nombre", "maximo")).first();

            if (documentoMinimo == null || documentoMaximo == null)
            {
                return Respuestas.respuestaErrorGson("No se encontraron los valores mínimos y máximos en la colección 'constantes'.");
            }

            double precioMinimo;
            double precioMaximo;

            if(documentoMinimo.get("valor") instanceof String)
            {
                precioMinimo = Double.parseDouble((String) documentoMinimo.get("valor"));
            }
            else
            {
                precioMinimo = documentoMinimo.getDouble("valor");
            }

            if(documentoMaximo.get("valor") instanceof String)
            {
                precioMaximo = Double.parseDouble((String) documentoMaximo.get("valor"));
            }
            else
            {
                precioMaximo = documentoMaximo.getDouble("valor");
            }

            // Verificar el precio contra los valores mínimos y máximos
            if (nuevoPrecio < precioMinimo || nuevoPrecio > precioMaximo)
            {
                return Respuestas.respuestaErrorGson("El precio del producto debe estar entre " + precioMinimo + " y " + precioMaximo + " euros.");
            }

            // Verificamos que la longitud de la descripción no exceda el máximo permitido.
            if (nuevaDescripcion.length() > 256)
            {
                return Respuestas.respuestaErrorGson("La descripción del producto es demasiado larga.");
            }

            // Accedemos a la colección especificada dentro de la base de datos.
            MongoCollection<Document> coleccion = baseDatos.getCollection(nombreColeccion);

            // Buscamos el primer documento que coincide con el nombre original del producto.
            Document productoExistente = coleccion.find(Filters.eq("nombre", nombreOriginal)).first();

            // Confirmamos que el producto exista antes de intentar actualizarlo.
            if (productoExistente == null)
            {
                return Respuestas.respuestaErrorGson("No se puede actualizar porque el producto no existe: " + nombreOriginal);
            }

            // Realizamos la actualización del producto en la base de datos.
            coleccion.updateOne(
                    Filters.eq("nombre", nombreOriginal),
                    Updates.combine(
                            Updates.set("nombre", nuevoNombre), // Actualizamos el nombre del producto.
                            Updates.set("precio", nuevoPrecio), // Ajustamos el precio del producto.
                            Updates.set("descripcion", nuevaDescripcion) // Modificamos la descripción del producto.
                    )
            );

            // Creamos un objeto JSON para registrar los detalles del producto actualizado.
            JsonObject datosProducto = new JsonObject();
            datosProducto.addProperty("nombre_original", nombreOriginal);
            datosProducto.addProperty("nuevo_nombre", nuevoNombre);
            datosProducto.addProperty("precio", nuevoPrecio);
            datosProducto.addProperty("descripcion", nuevaDescripcion);

            // Devolvemos un objeto JSON con los detalles actualizados del producto.
            return Respuestas.respuestaExitosaGson(datosProducto);
        }
        catch (MongoException e)
        {
            // Manejamos cualquier error de MongoDB que pueda ocurrir durante la operación.
            return Respuestas.respuestaErrorGson("Error de base de datos: " + e.getMessage());
        }
        catch (Exception e)
        {
            // Capturamos cualquier otro error no previsto para evitar que la aplicación falle inesperadamente.
            return Respuestas.respuestaErrorGson("Error inesperado: " + e.getMessage());
        }
    }


    /**
     * Buscamos un producto por nombre en todas las colecciones de la base de datos.
     *
     * @param nombreProducto El nombre del producto a buscar.
     * @return JsonObject Resultado de la búsqueda, ya sea el producto encontrado o un mensaje de error.
     */
    public JsonObject buscarProductoEnTodasLasColecciones(String nombreProducto)
    {
        if (nombreProducto == null || nombreProducto.trim().isEmpty())
        {
            return Respuestas.respuestaErrorGson("El nombre del producto no puede estar vacío.");
        }

        try
        {
            MongoIterable<String> nombresColecciones = baseDatos.listCollectionNames();
            for (String nombreColeccion : nombresColecciones)
            {
                MongoCollection<Document> coleccion = baseDatos.getCollection(nombreColeccion);
                Document productoEncontrado = coleccion.find(Filters.eq("nombre", nombreProducto)).first();
                if (productoEncontrado != null)
                {
                    JsonObject productoJSON = new JsonObject();
                    productoJSON.addProperty("nombre", productoEncontrado.getString("nombre"));
                    productoJSON.addProperty("precio", productoEncontrado.getDouble("precio"));
                    productoJSON.addProperty("descripcion", productoEncontrado.getString("descripcion"));
                    productoJSON.addProperty("cantidad", productoEncontrado.getInteger("cantidad"));
                    productoJSON.addProperty("coleccion", nombreColeccion); // Añadir el nombre de la colección

                    return Respuestas.respuestaExitosaGson(productoJSON);
                }
            }
            return Respuestas.respuestaErrorGson("No se encontró ningún producto con el nombre: " + nombreProducto);
        }
        catch (MongoException e)
        {
            return Respuestas.respuestaErrorGson("Error de base de datos al buscar el producto: " + e.getMessage());
        }
        catch (Exception e)
        {
            return Respuestas.respuestaErrorGson("Error inesperado al buscar el producto: " + e.getMessage());
        }
    }


    public Producto buscarProductoPorNombre(String nombreProducto, String nombreColeccion)
    {
        try
        {
            MongoCollection<Document> coleccion = baseDatos.getCollection(nombreColeccion);
            Document doc = coleccion.find(Filters.eq("nombre", nombreProducto)).first();

            if (doc != null)
            {
                return new Producto(
                        doc.getString("nombre"),
                        doc.getDouble("precio"),
                        doc.getString("descripcion"),
                        0  // Inicializamos la cantidad a 0
                );
            }
        }
        catch (MongoException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Listamos todos los productos en una colección específica de la base de datos MongoDB.
     *
     * @param nombreColeccion El nombre de la colección de la cual listar todos los productos.
     * @return JsonObject Un objeto JSON que contiene una lista de productos o un mensaje de error.
     */
    public JsonObject listarTodos(String nombreColeccion)
    {
        // Accedemos a la colección especificada por nombreColeccion.
        MongoCollection<Document> coleccion = baseDatos.getCollection(nombreColeccion);

        JsonArray productosArray = new JsonArray(); // Crear un arreglo JSON para almacenar los productos.

        // Usamos un cursor para iterar a través de todos los documentos en la colección.
        try (MongoCursor<Document> cursor = coleccion.find().iterator())
        {
            while (cursor.hasNext())
            {
                // Mientras haya documentos en la colección.
                Document doc = cursor.next(); // Obtenemos el documento actual del cursor.

                // Creamos un objeto JSON para almacenar los detalles del producto actual.
                JsonObject productoJSON = new JsonObject();
                productoJSON.addProperty("nombre", doc.getString("nombre"));
                productoJSON.addProperty("precio", doc.getDouble("precio"));
                productoJSON.addProperty("descripcion", doc.getString("descripcion"));
                productoJSON.addProperty("cantidad", doc.getInteger("cantidad"));

                // Añadir el objeto JSON del producto a un array de productos.
                productosArray.add(productoJSON);
            }
        }
        catch (MongoException e)
        {
            return Respuestas.respuestaErrorGson("Error de base de datos al buscar el producto: " + e.getMessage());
        }
        catch (Exception e)
        {
            return Respuestas.respuestaErrorGson("Error inesperado al buscar el producto: " + e.getMessage());
        }

        // Verificamos si se encontraron productos y devolver un objeto JSON adecuado.
        if (productosArray.size() > 0)
        {
            // Si hay productos, devolvemos la caja llena de productos.
            return Respuestas.respuestaExitosaGson(productosArray);
        }
        else
        {
            // Si no hemos encontrado nada devolvemos un objeto JSON de error.
            return Respuestas.respuestaErrorGson("No se encontraron productos en la colección: " + nombreColeccion);
        }
    }

    /**
     * Eliminamos un producto por nombre en una colección especificada.
     *
     * @param nombreProducto  El nombre del producto a eliminar.
     * @param nombreColeccion El nombre de la colección donde se encuentra el producto.
     * @return JsonObject Objeto JSON que indica el resultado de la operación, ya sea éxito o error.
     */
    public JsonObject eliminarProductoPorNombre(String nombreProducto, String nombreColeccion)
    {
        try
        {
            // Accedemos a la colección especificada por nombreColeccion.
            MongoCollection<Document> coleccion = baseDatos.getCollection(nombreColeccion);

            // Recogemos el primer documento que coincide con el nombre del producto.
            Document productoExistente = coleccion.find(Filters.eq("nombre", nombreProducto)).first();

            // Verificamos si el producto existe en la base de datos.
            if (productoExistente == null)
            {
                // Devolvemos un mensaje de error si el producto no se encuentra.
                return Respuestas.respuestaErrorGson("No se encontró ningún producto con el nombre: " + nombreProducto);
            }

            // Ejecutamos la operación de eliminación en la base de datos.
            DeleteResult resultado = coleccion.deleteOne(Filters.eq("nombre", nombreProducto));

            // Evaluamos si la eliminación fue exitosa.
            if (resultado.getDeletedCount() > 0)
            {
                // Creamos un objeto JSON para devolver los detalles del producto eliminado.
                JsonObject datosProducto = new JsonObject();
                datosProducto.addProperty("nombre", nombreProducto); // Incluimos el nombre del producto para referencia.

                // Devolvemos un objeto JSON que indica que la eliminación fue exitosa.
                return Respuestas.respuestaExitosaGson(datosProducto);
            }
            else
            {
                // Si no se pudo eliminar el producto, devolvemos un mensaje de error.
                return Respuestas.respuestaErrorGson("No se pudo eliminar el producto: " + nombreProducto);
            }
        }
        catch (MongoException e)
        {
            return Respuestas.respuestaErrorGson("Error de base de datos al buscar el producto: " + e.getMessage());
        }
        catch (Exception e)
        {
            // Capturamos cualquier otro error no previsto para evitar que la aplicación falle inesperadamente.
            return Respuestas.respuestaErrorGson("Error inesperado al buscar el producto: " + e.getMessage());
        }
    }

    public MongoDatabase getDatabase()
    {
        return baseDatos;
    }

    /**
     * Cierra la conexión con MongoDB.
     */
    public void cerrarConexion()
    {
        if (clienteMongo != null)
        {
            clienteMongo.close();
            System.out.println("Conexión con MongoDB cerrada.");
        }
    }
}
