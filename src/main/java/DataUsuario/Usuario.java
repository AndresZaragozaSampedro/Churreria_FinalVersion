package DataUsuario;

import Utilidades.GeneradorID;

public class Usuario
{

//ATRIBUTOS
    private int id;
    private String username;
    private String password;


//CONSTRUCTOR
    public Usuario(String username, String password, GeneradorID generadorID)
    {
        this.id = generadorID.generate("Empleados");
        this.username = username;
        this.password = password;
    }

    // Getters y setters
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
