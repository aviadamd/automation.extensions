package org.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public MongoConnection(String host, String dbName) {
        this.mongoClient = new MongoClient(host);
        this.mongoDatabase = mongoClient.getDatabase(dbName);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
