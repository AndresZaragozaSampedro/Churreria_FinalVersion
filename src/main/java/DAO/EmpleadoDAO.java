package DAO;

import com.google.gson.JsonObject;

import java.util.List;

public interface EmpleadoDAO
{
    JsonObject insertarEmpleado(String username, String password);
    JsonObject actualizarEmpleado(String username, String nuevaPassword);
    JsonObject buscarPorNombre(String username);
    JsonObject listarTodos();
    JsonObject eliminarPorNombre(String username);
    List<String> obtenerTodosLosNombresDeEmpleados();
    JsonObject login(String username, String password);
    boolean validarToken(String token);
}
