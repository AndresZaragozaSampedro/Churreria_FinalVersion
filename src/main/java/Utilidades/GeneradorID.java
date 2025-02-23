package Utilidades;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;

public class GeneradorID
{
    private MongoDatabase database;

    public GeneradorID(MongoDatabase database)
    {
        this.database = database;
    }

    public int generate(String collectionName)
    {
        MongoCollection<Document> counters = database.getCollection("counters");
        Document counter = counters.findOneAndUpdate(
                eq("_id", collectionName),
                inc("seq", 1),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER).upsert(true)
        );
        return counter != null ? counter.getInteger("seq") : 1;
    }
}
