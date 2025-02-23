package data;

import Utilidades.Configuraciones;
import Utilidades.GeneradorID;
import Utilidades.JSONToken;
import Utilidades.Respuestas;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

public class Gestiona_BaseDatos_Empleados {

    //ATRIBUTOS
    private MongoClient clienteMongo;
    private MongoDatabase baseDatos;
    private GeneradorID generadorID;

    //CONSTRUCTOR
    // Constructor que acepta MongoDatabase
    public Gestiona_BaseDatos_Empleados(MongoDatabase baseDatos)
    {
        this.baseDatos = baseDatos;
        this.generadorID = new GeneradorID(baseDatos);
        System.out.println("Conectado a la base de datos: " + baseDatos.getName());
    }

    public Gestiona_BaseDatos_Empleados(MongoClient clienteMongo, String nombreBaseDatos) {
        this.clienteMongo = clienteMongo;
        this.baseDatos = clienteMongo.getDatabase(nombreBaseDatos);
        this.generadorID = new GeneradorID(baseDatos);
        System.out.println("Conectado a la base de datos: " + nombreBaseDatos);
    }

    public Gestiona_BaseDatos_Empleados() {
        this.generadorID = new GeneradorID(baseDatos);
    }

    public void conectarConBaseDatos(String uri, String nombreBaseDatos) {
        if (Configuraciones.esNombreValido(nombreBaseDatos)) {
            // Establecer conexión con MongoDB
            clienteMongo = MongoClients.create(uri);

            // Seleccionar la base de datos
            baseDatos = clienteMongo.getDatabase(nombreBaseDatos);
            System.out.println("Conectado a la base de datos: " + nombreBaseDatos);
        } else {
            System.out.println("Nombre de base de datos inválido: " + nombreBaseDatos);
        }
    }


    public void crearNuevaColeccion(String nombreColeccion) {
        // Validamos que no puedan poner un nombre vacio.
        if (nombreColeccion == null || nombreColeccion.trim().isEmpty()) {
            System.out.println("El nombre de la colección no puede estar vacío.");
            return;
        }

        // Validamos que la coleccion no tenga simbolos que no contemplamos.
        if (nombreColeccion.matches(".*[/.\"$*<>:|? ].*")) {
            System.out.println("El nombre de la colección contiene caracteres no permitidos.");
            return;
        }

        // Hemos investigado y resulta que mongo no deja que las colecciones tengan mas de 64 caracteres
        if (nombreColeccion.length() > 64) {
            System.out.println("El nombre de la colección es demasiado largo.");
            return;
        }

        // Verificamos si existe ya esta coleccion, (ignoramos entre mayusculas y minusculas)
        MongoIterable<String> colecciones = baseDatos.listCollectionNames();

        for (String nombre : colecciones) {
            if (nombre.equalsIgnoreCase(nombreColeccion)) {
                System.out.println("La colección ya existe: " + nombreColeccion);
                return;
            }
        }

        // Creamos la coleccion
        baseDatos.createCollection(nombreColeccion);
        System.out.println("Colección creada con éxito: " + nombreColeccion);
    }


    //********************************** AÑADIR EMPLEADO *******************************
    public JsonObject añadirEmpleado(String username, String password) {
        try {
            // Validar usr no nulo, no vacio, no pass vacio ni nulo
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                return Respuestas.respuestaErrorGson("Los datos del empleado no pueden estar vacíos.");
            }

            // Validar longitud nom y pass
            if (!Configuraciones.esNombreValido(username)) {
                return Respuestas.respuestaErrorGson("El nombre de usuario no es válido.");
            }

            if (!Configuraciones.esPasswordValido(password)) {
                return Respuestas.respuestaErrorGson("La contraseña no es válida.");
            }

            // Obtener la colección de empleados
            MongoCollection<Document> coleccion = baseDatos.getCollection("Empleados");

            // Comprobar si el nombre de usuario ya existe
            Document usuarioExistente = coleccion.find(Filters.eq("username", username)).first();
            if (usuarioExistente != null) {
                return Respuestas.respuestaErrorGson("El nombre de usuario ya está en uso.");
            }


            // Hasheo de la contraseña//
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Generar ID para el nuevo empleado
            int empleadoId = generadorID.generate("Empleados");

            // Crear el documento del nuevo empleado
            Document nuevoEmpleado = new Document("_id", empleadoId)
                    .append("username", username)
                    .append("password", hashedPassword);  // Guarda la contraseña hasheada

            // Insertar el nuevo empleado en la colección
            coleccion.insertOne(nuevoEmpleado);

            System.out.println("Empleado añadido correctamente con ID: " + empleadoId);

            // Devolver respuesta exitosa
            JsonObject datosEmpleado = new JsonObject();
            datosEmpleado.addProperty("id", empleadoId);
            datosEmpleado.addProperty("username", username);

            return Respuestas.respuestaExitosaGson(datosEmpleado);
        } catch (MongoException me) {
            // Manejo de errores específicos de MongoDB.
            return Respuestas.respuestaErrorGson("Error al interactuar con la base de datos: " + me.getMessage());
        } catch (Exception e) {
            return Respuestas.respuestaErrorGson("Error inesperado: " + e.getMessage());
        }
    }


    /**
     * Método para hashear contraseñas usando una biblioteca de seguridad como BCrypt.
     */
    public boolean verificarContraseña(String passwordPlana, String hashedPassword) {
        return BCrypt.checkpw(passwordPlana, hashedPassword);
    }


/////////////////////////////// Actualizar empleado  ///////////////////////////////////////////////////


    public JsonObject actualizarEmpleado(String username, String nuevaPassword) {
        try {
            // validar que username no esté vacio ni contenga caracteres  no permitidos
            if (username == null || username.isEmpty() || username.matches("[/\\.\"$*<>:|?]+")) {
                return Respuestas.respuestaErrorGson("El nombre del usuario contiene caracteres inválidos o está vacío.");
            }

            //validar que la nueva contraseña no esté vacía y sea valida
            if (nuevaPassword == null || nuevaPassword.isEmpty() || Configuraciones.esPasswordValido((nuevaPassword))) {
                return Respuestas.respuestaErrorGson("La contraseña no es válida");
            }

            //Acceder a colección Empleados
            MongoCollection<Document> coleccionEmpleados = baseDatos.getCollection("empleados");

            //buscar el 1er documento que coincide con el username del usuario

            Document usuarioExistente = coleccionEmpleados.find(Filters.eq("username", username)).first();

            //confirmar que el usuario exista antes de intentar actualizarlo
            if (usuarioExistente == null) {
                return Respuestas.respuestaErrorGson("No se puede actualizar porque el usuario no existe:" + username);
            }

            // Hashear la nueva contraseña
            String hashedPassword = BCrypt.hashpw(nuevaPassword, BCrypt.gensalt());

            // Realizar la actualización del usuario en la base de datos.
            coleccionEmpleados.updateOne(
                    Filters.eq("username", username),
                    Updates.combine(
                            Updates.set("password", hashedPassword) // Actualizamos la contraseña del usuario.
                    )
            );

            JsonObject datosUsuario = new JsonObject();
            datosUsuario.addProperty("username", username);
            datosUsuario.addProperty("password", hashedPassword);

            return Respuestas.respuestaExitosaGson(datosUsuario);

        } catch (MongoException e) {
            // Manejamos errores específicos de MongoDB que pueden incluir problemas de conexión o errores al realizar la consulta.
            return Respuestas.respuestaErrorGson("Error de base de datos al buscar el producto: " + e.getMessage());
        } catch (Exception e) {
            return Respuestas.respuestaErrorGson("Error inesperado al buscar el producto: " + e.getMessage());
        }

    }

////////////////////////////////   BUSCAR EMPLEADO     /////////////////////////////////

    public JsonObject buscarEmpleados(String username) {
        // Verificamos si el nombre del producto es nulo o está vacío antes de proceder.
        if (username == null || username.trim().isEmpty()) {
            return Respuestas.respuestaErrorGson("El nombre del empleado no puede estar vacío.");
        }

        try {
            MongoIterable<String> nombreColecciones = baseDatos.listCollectionNames();

            for (String nombreColeccion : nombreColecciones) {
                MongoCollection<Document> coleccion = baseDatos.getCollection(nombreColeccion);

                // Buscamos el primer documento en la colección que coincida con el nombre del producto.
                Document empleadoEncontrado = coleccion.find(Filters.eq("username", username)).first();

                if (empleadoEncontrado != null) {
                    JsonObject empleadoJSON = new JsonObject();
                    empleadoJSON.addProperty("username", empleadoEncontrado.getString("username"));

                    return Respuestas.respuestaExitosaGson(empleadoJSON);
                }

            }
            return Respuestas.respuestaErrorGson("No se encontró ningún empleado con el nombre: " + username);

        }
        catch (MongoException e)
        {
            return Respuestas.respuestaErrorGson("Error de base de datos al buscar el nombre: " + e.getMessage());
        }
        catch (Exception e)
        {
            return Respuestas.respuestaErrorGson("Error inesperado al buscar el nombre: " + e.getMessage());
        }

    }


    /////////////////////////// VER TODOS LOS EMPLEADOS ///////////////////////////////////////

    public JsonObject verTodosEmpleados() {
        JsonArray empleadosArray = new JsonArray();

        try {
            MongoCollection<Document> coleccion = baseDatos.getCollection("Empleados");
            MongoCursor<Document> cursor = coleccion.find().iterator();

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                JsonObject empleado = new JsonObject();
                empleado.addProperty("id", doc.getInteger("_id"));
                empleado.addProperty("username", doc.getString("username"));
                empleado.addProperty("password", doc.getString("password"));
                empleadosArray.add(empleado);
            }

            return Respuestas.respuestaExitosaGson(empleadosArray);
        } catch (Exception e) {
            return Respuestas.respuestaErrorGson(e.getMessage());
        }
    }




    public JsonObject eliminarEmpleadoPorNombre (String username, String nombreColeccion)
    {
        try
        {
            //acceso a colección
            MongoCollection<Document> coleccion = baseDatos.getCollection(nombreColeccion);


            Document empleadoExistente = coleccion.find(Filters.eq("username", username)).first();

            //verificar si existe empleado
            if(empleadoExistente == null)
            {
                return Respuestas.respuestaErrorGson("No se encontró ningún empleado con el nombre: " + username);
            }

            DeleteResult resultado = coleccion.deleteOne(Filters.eq("username", username));

            // Evaluamos si la eliminación fue exitosa.
            if (resultado.getDeletedCount() > 0)
            {
                // Creamos un objeto JSON para devolver los detalles del producto eliminado.
                JsonObject datosEmpleado = new JsonObject();
                datosEmpleado.addProperty("username", username);  // Incluimos el nombre del producto para referencia.

                // Devolvemos un objeto JSON que indica que la eliminación fue exitosa.
                return Respuestas.respuestaExitosaGson(datosEmpleado);
            }
            else
            {
                // Si no se pudo eliminar el producto, devolvemos un mensaje de error.
                return Respuestas.respuestaErrorGson("No se pudo eliminar el producto: " + username);
            }
        }

        catch(MongoException e)
        {
            return Respuestas.respuestaErrorGson("Error de base de datos al buscar el empleado: " + e.getMessage());

        }
        catch (Exception e)
        {
            // Capturamos cualquier otro error no previsto para evitar que la aplicación falle inesperadamente.
            return Respuestas.respuestaErrorGson("Error inesperado al buscar el empleado: " + e.getMessage());
        }

    }

    public JsonObject login(String username, String password)
    {
        MongoCollection<Document> coleccion = baseDatos.getCollection("Empleados");
        Document usuarioExistente = coleccion.find(Filters.eq("username", username)).first();

        if (usuarioExistente != null && BCrypt.checkpw(password, usuarioExistente.getString("password"))) {
            String token = JSONToken.generateToken(username);
            JsonObject response = new JsonObject();
            response.addProperty("token", token);
            return Respuestas.respuestaExitosaGson(response);
        }
        else
        {
            return Respuestas.respuestaErrorGson("Usuario o contraseña incorrectos.");
        }
    }

    public boolean validarToken(String token)
    {
        try
        {
            String username = JSONToken.extractUsername(token);
            MongoCollection<Document> coleccion = baseDatos.getCollection("Empleados");
            Document usuarioExistente = coleccion.find(Filters.eq("username", username)).first();
            return usuarioExistente != null && JSONToken.validateToken(token, username);
        }
        catch (Exception e)
        {
            return false;
        }
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
