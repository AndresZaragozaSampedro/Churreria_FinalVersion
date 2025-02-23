package Churreria;

import DataProductos.Producto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Churreria
{
    private Caja caja;
    private List<Ticket> tickets;
    private MongoDatabase baseDatos;

    public Churreria(MongoDatabase baseDatos)
    {
        this.caja = new Caja();
        this.tickets = new ArrayList<>();
        this.baseDatos = baseDatos;
        cargarTickets(); // Cargar tickets al inicio
    }

    public Ticket cobrar(Mesa mesa)
    {
        double total = mesa.calcularTotal();
        caja.agregarImporte(total);

        List<Producto> productos = new ArrayList<>(mesa.getProductos()); // Clonar la lista de productos
        Ticket ticket = new Ticket(productos, new Date());
        tickets.add(ticket);
        guardarTicket(ticket); // Guarda el ticket en la base de datos para persistencia
        mesa.limpiarMesa();
        return ticket;
    }

    public double verCaja()
    {
        return caja.getTotal();
    }

    public List<Ticket> getTickets()
    {
        return tickets;
    }

    public void cargarTickets()
    {
        MongoCollection<Document> coleccionTickets = baseDatos.getCollection("tickets");
        for (Document doc : coleccionTickets.find())
        {
            tickets.add(Ticket.fromDocument(doc));
        }
    }

    private void guardarTicket(Ticket ticket)
    {
        MongoCollection<Document> coleccionTickets = baseDatos.getCollection("tickets");
        coleccionTickets.insertOne(ticket.toDocument());
    }

    @Override
    public String toString()
    {
        return "Churreria{" +
                "caja=" + caja +
                ", tickets=" + tickets +
                '}';
    }
}
