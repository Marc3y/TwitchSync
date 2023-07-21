package de.kenjih.twitchsync.mongodb.data;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import de.kenjih.twitchsync.mongodb.MongoDB;
import org.bson.Document;
import org.bson.conversions.Bson;

public class Collection {

    private String collection;
    private MongoDB mongoDB;

    public Collection(String collectionName, MongoDB mongoDB){
        this.mongoDB = mongoDB;
        this.collection = collectionName;
    }

    public MongoCollection<Document> getDocument(){
        return this.mongoDB.getCollectionDocument(this.collection);
    }

    public Document find(String path, Object value){
        Document find = new Document(path, value);
        return this.mongoDB.getClient().getDatabase(this.mongoDB.getDatabase()).getCollection(collection).find(find).first();
    }

    public void set(Document document){
        this.mongoDB.getCollectionDocument(collection).insertOne(document);
    }

    public void update(Document search, Bson updates){
        UpdateOptions options = new UpdateOptions().upsert(true);
        try {
            this.mongoDB.getCollectionDocument(collection).updateOne(search, updates, options);
        } catch (MongoException e){
            e.printStackTrace();
        }
    }

}
