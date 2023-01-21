package org.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Slf4j
public class MongoConnectionManager {
    private MongoDatabase database;
    public MongoConnectionManager(String uri, String dbName) {
        try {
            MongoClient mongoClient = MongoClients.create(uri);
            PojoCodecProvider provider = PojoCodecProvider.builder().automatic(true).build();
            CodecRegistry registry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromProviders(provider));
            this.database = mongoClient.getDatabase(dbName).withCodecRegistry(registry);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }
    public MongoDatabase getDatabase() { return database; }
    public <TDocument> MongoCollection<TDocument> mongoCollection(String collection, Class<TDocument> tDocumentClass) {
        return this.getDatabase().getCollection(collection, tDocumentClass);
    }
}
