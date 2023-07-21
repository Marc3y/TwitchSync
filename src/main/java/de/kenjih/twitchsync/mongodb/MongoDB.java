package de.kenjih.twitchsync.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import de.kenjih.twitchsync.mongodb.data.Collection;
import org.bson.Document;

public class MongoDB {

    private String connectionStr;
    private String database;
    private MongoClient client;

    public MongoDB(String connectionStr, String database){
        this.connectionStr = connectionStr;
        this.database = database;
    }

    public void openConnection(){
        this.client = MongoClients.create(connectionStr);
    }
    public void closeConnection(){
        this.client.close();
    }

    public MongoCollection<Document> getCollectionDocument(String collectionName){
        return this.client.getDatabase(this.database).getCollection(collectionName);
    }

    public Collection getCollection(String collectionName){
        return new Collection(collectionName, this);
    }

    public MongoClient getClient() {
        return client;
    }

    public String getDatabase() {
        return database;
    }
}
