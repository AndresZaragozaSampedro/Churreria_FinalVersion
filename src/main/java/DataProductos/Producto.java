package DataProductos;

public class Producto {

    // ATRIBUTOS
    private String nombre;
    private double precio;
    private String descripcion;
    private int cantidad;

    // CONSTRUCTOR
    public Producto(String nombre, double precio, String descripcion, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    // SETTERS & GETTERS

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    @Override
    public String toString() {
        return String.format("Producto(nombre='%s', precio=%.2f, descripcion='%s', cantidad=%d)",
                nombre, precio, descripcion, cantidad);
    }
}
