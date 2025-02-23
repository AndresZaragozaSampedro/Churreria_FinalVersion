package Churreria;

/**
 * Clase que representa la caja de la churrería.
 * Maneja el total acumulado de dinero.
 */

public class Caja
{
    private double total;

    public Caja()
    {
        this.total = 0.0;
    }

    public double getTotal()
    {
        return total;
    }

    public void agregarImporte(double importe)
    {
        total += importe;
    }

    public void reiniciarCaja()
    {
        total = 0.0;
    }

    @Override
    public String toString()
    {
        return "Caja{" +
                "total=" + total +
                '}';
    }
}
