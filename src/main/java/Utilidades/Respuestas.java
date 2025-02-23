package Utilidades;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class Respuestas
{
    /*
    public static JSONObject respuestaExitosa(JSONObject data)
    {
        JSONObject respuesta = new JSONObject();
        respuesta.put("estado", "éxito");
        respuesta.put("datos", data);
        return respuesta;
    }

    public static JSONObject respuestaExitosa(JSONArray data)
    {
        JSONObject respuesta = new JSONObject();
        respuesta.put("estado", "éxito");
        respuesta.put("datos", data);
        return respuesta;
    }

    public static JSONObject respuestaError(String mensaje)
    {
        JSONObject respuesta = new JSONObject();
        respuesta.put("estado", "error");
        respuesta.put("mensaje", mensaje);
        return respuesta;
    }
    */

    /**
     * respuestaExitosaGson(JsonObject data) = devuelve el Objeto individual
     *
     * respuestaExitosaGson(JsonArray data)  = el array de Objetos
     */
    public static JsonObject respuestaExitosaGson(JsonObject data)
    {
        JsonObject respuesta = new JsonObject();
        respuesta.addProperty("estado", "éxito");
        respuesta.add("datos", data);
        return respuesta;
    }

    public static JsonObject respuestaExitosaGson(JsonArray data)
    {
        JsonObject respuesta = new JsonObject();
        respuesta.addProperty("estado", "éxito");
        respuesta.add("datos", data);
        return respuesta;
    }

    public static JsonObject respuestaErrorGson(String mensaje) {
        JsonObject respuesta = new JsonObject();
        respuesta.addProperty("estado", "error");
        respuesta.addProperty("mensaje", mensaje);
        return respuesta;
    }

}
