package DAO;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import data.Gestiona_BaseDatos_Empleados;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAOMongo implements EmpleadoDAO {
    private Gestiona_BaseDatos_Empleados gestorDB;

    public EmpleadoDAOMongo(Gestiona_BaseDatos_Empleados gestorDB) {
        this.gestorDB = gestorDB;
    }

    @Override
    public JsonObject insertarEmpleado(String username, String password) {
        return gestorDB.añadirEmpleado(username, password);
    }

    @Override
    public JsonObject actualizarEmpleado(String username, String nuevaPassword) {
        return gestorDB.actualizarEmpleado(username, nuevaPassword);
    }

    @Override
    public JsonObject buscarPorNombre(String username) {
        return gestorDB.buscarEmpleados(username);
    }

    @Override
    public JsonObject listarTodos() {
        return gestorDB.verTodosEmpleados();
    }

    @Override
    public JsonObject eliminarPorNombre(String username) {
        return gestorDB.eliminarEmpleadoPorNombre(username, "Empleados");
    }

    @Override
    public JsonObject login(String username, String password) {
        return gestorDB.login(username, password);
    }

    @Override
    public boolean validarToken(String token) {
        return gestorDB.validarToken(token);
    }

    public List<String> obtenerTodosLosNombresDeEmpleados() {
        List<String> nombres = new ArrayList<>();
        JsonObject resultado = listarTodos();

        if (resultado != null && resultado.has("datos")) {
            JsonArray empleadosArray = resultado.getAsJsonArray("datos");
            for (int i = 0; i < empleadosArray.size(); i++) {
                JsonObject empleado = empleadosArray.get(i).getAsJsonObject();
                nombres.add(empleado.get("username").getAsString());
            }
        }
        return nombres;
    }
}
