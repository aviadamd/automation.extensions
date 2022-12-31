package org.mongo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoBase {

    private final String dbName;
    private final MongoConnection mongoConnection;
    private final MongoCollection<Document> mongoCollection;

    public MongoBase(MongoConnection mongoConnection, String collectionName) {
        this.mongoConnection = mongoConnection;
        this.mongoCollection = mongoConnection.getMongoDatabase().getCollection(collectionName);
        this.dbName = mongoConnection.getMongoDatabase().getName();
    }

    public String getDbName() { return dbName; }
    public MongoConnection getMongoConnection() { return mongoConnection; }
    public MongoCollection<Document> getMongoCollection() { return mongoCollection; }
}
