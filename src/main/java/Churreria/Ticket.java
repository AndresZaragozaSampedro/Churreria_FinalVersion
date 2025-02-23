package Churreria;

import DataProductos.Producto;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Ticket
{
    private List<Producto> productos;
    private Date fecha;
    private double importeTotal;

    public Ticket(List<Producto> productos, Date fecha)
    {
        this.productos = productos;
        this.fecha = fecha;
        this.importeTotal = calcularImporteTotal();
    }

    public List<Producto> getProductos()
    {
        return productos;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public double getImporteTotal()
    {
        return importeTotal;
    }

    private double calcularImporteTotal()
    {
        return productos.stream().mapToDouble(p -> p.getPrecio() * p.getCantidad()).sum();
    }

    public Document toDocument()
    {
        List<Document> productosDocs = new ArrayList<>();
        for (Producto producto : productos)
        {
            Document productoDoc = new Document("nombre", producto.getNombre())
                    .append("precio", producto.getPrecio())
                    .append("descripcion", producto.getDescripcion())
                    .append("cantidad", producto.getCantidad());
            productosDocs.add(productoDoc);
        }

        return new Document("productos", productosDocs)
                .append("fecha", fecha)
                .append("importeTotal", importeTotal);
    }

    public static Ticket fromDocument(Document document)
    {
        List<Document> productosDocs = (List<Document>) document.get("productos");
        List<Producto> productos = new ArrayList<>();
        for (Document productoDoc : productosDocs)
        {
            Producto producto = new Producto(
                    productoDoc.getString("nombre"),
                    productoDoc.getDouble("precio"),
                    productoDoc.getString("descripcion"),
                    productoDoc.getInteger("cantidad")
            );
            productos.add(producto);
        }

        Date fecha = document.getDate("fecha");
        double importeTotal = document.getDouble("importeTotal");

        Ticket ticket = new Ticket(productos, fecha);
        ticket.importeTotal = importeTotal;
        return ticket;
    }

    @Override
    public String toString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm:ss", new Locale("es", "ES"));
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket:\n");
        sb.append("Fecha: ").append(sdf.format(fecha)).append("\n");
        sb.append("Productos:\n");
        for (Producto producto : productos)
        {
            sb.append(" - ").append(producto.getCantidad()).append(" x ").append(producto.getNombre()).append(": ")
                    .append(String.format("%.2f€", producto.getPrecio() * producto.getCantidad())).append("\n");
        }
        sb.append("Importe Total: ").append(String.format("%.2f€", importeTotal)).append("\n");
        return sb.toString();
    }
}
