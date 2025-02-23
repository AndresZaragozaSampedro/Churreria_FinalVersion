package Utilidades;

public class Configuraciones
{

    public static boolean esNombreValido(String nombre)
    {
        // Validar longitud mínima
        if (nombre.length() < 3)
        {
            return false;
        }
        // Validar caracteres no permitidos
        if (nombre.matches(".*[/.\"$*<>:|? ].*"))
        {  // La expresión regular busca cualquier carácter no permitido
            return false;
        }
        // Validar que no sea una palabra reservada
        String[] palabrasReservadas = {"admin", "local", "config"};
        for (String reservada : palabrasReservadas)
        {
            if (nombre.equalsIgnoreCase(reservada))
            {
                return false;
            }
        }
        return true;
    }


    public static boolean esPasswordValido(String password)
    {
        // Validar longitud mínima
        if (password == null)
        {
            return false;
        }

        // Validar longitud mínima de 7 caracteres
        if (password.length() < 7)
        {
            return false;
        }

        // Validar que contenga al menos una letra mayúscula, una letra minúscula, un número y un carácter especial
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$";
        if (!password.matches(regex))
        {
            return false;
        }

        // Si pasa todas las validaciones, es válida
        return true;

	}

    public static boolean sonNombresSimilares(String nombre1, String nombre2)
    {
        // Normalizar nombres a minúsculas y quitar la "s" final si existe
        String nombre1Normalizado = nombre1.toLowerCase().replaceAll("s$", "");
        String nombre2Normalizado = nombre2.toLowerCase().replaceAll("s$", "");
        return nombre1Normalizado.equals(nombre2Normalizado);
    }

}
