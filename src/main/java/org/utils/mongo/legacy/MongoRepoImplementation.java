package org.utils.mongo.legacy;

import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.event.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Assertions;
import org.utils.mongo.manager.MongoConnectionPoolListener;
import org.utils.mongo.manager.MongoDbListener;

import java.util.*;
import static com.mongodb.MongoClient.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Slf4j
public class MongoRepoImplementation {

    private String dbName;
    private String collectionName;
    private final ThreadLocal<MongoClient> mongoClient = new ThreadLocal<>();
    private final ThreadLocal<MongoDatabase> mongoDatabase = new ThreadLocal<>();

    public MongoRepoImplementation(String stringConnection, String dbName, String collectionName) {
        try {

            CodecProvider provider = PojoCodecProvider.builder().automatic(true).build();
            CodecRegistry registry = fromRegistries(getDefaultCodecRegistry(), fromProviders(provider));

            this.dbName = dbName;
            this.collectionName = collectionName;

            this.mongoClient.set(MongoClients.create(MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(stringConnection))
                    .addCommandListener(new MongoDbListener())
                    .applyToConnectionPoolSettings(builder -> builder.addConnectionPoolListener(new MongoConnectionPoolListener()))
                    .build()));
            this.mongoDatabase.set(this.mongoClient.get().getDatabase(dbName).withCodecRegistry(registry));

        } catch (Exception exception) {
            Assertions.fail("MongoRepoImplementation connection error", exception);
        }
    }

    public <T> MongoCollection<T> createObject(Class<T> objectClass) {
        return this.mongoDatabase.get().getCollection(this.collectionName, objectClass);
    }

    public Document findElementBy(BasicDBObject searchQuery) {
        try {
            return this.mongoDatabase.get().getCollection(this.collectionName).find(searchQuery).first();
        } catch (Exception e) {
            log.info("findElementBy error: " + e.getMessage());
            return new Document();
        }
    }

    public FindIterable<Document> findElementsBy(BasicDBObject searchQuery) {
        try {
            return this.mongoDatabase.get().getCollection(this.collectionName).find(searchQuery);
        } catch (Exception e) {
            log.info("findElementsBy error: " + e.getMessage());
            return this.mongoDatabase.get().getCollection(this.collectionName).find();
        }
    }
    public Document findElementBy(Bson query) {
        try {
            return this.mongoDatabase.get().getCollection(this.collectionName).find(query).first();
        } catch (Exception e) {
            log.info("findElementBy error: " + e.getMessage());
            return new Document();
        }
    }
    public List<Document> documentsGetAllElements() {
        List<Document> documentList = new ArrayList<>();
        try {
            for (Document document : this.iterableGetAllElements()) {
                documentList.add(document);
            }
        } catch (Exception e) {
            log.info("documentsGetAllElements error: " + e.getMessage());
            return documentList;
        }
        return documentList;
    }

    public FindIterable<Document> iterableGetAllElements() {
        return this.mongoDatabase.get().getCollection(this.collectionName).find();
    }

    public void deleteElement(Bson bson) {
        try {
            this.mongoDatabase.get().getCollection(this.collectionName).deleteOne(bson);
        } catch (Exception e) {
            log.error("deleteElement error: " + e.getMessage());
        }
    }

    public void updateElement(Bson from, Bson to) {
        try {
            this.mongoDatabase.get().getCollection(this.collectionName).updateOne(from, to);
        } catch (Exception e) {
            log.error("updateElement error: " + e.getMessage());
        }
    }

    public void insertElement(Document document) {
        try {
            this.mongoDatabase.get().getCollection(this.collectionName).insertOne(document);
        } catch (Exception e) {
            log.error("insertElement error: " + e.getMessage());
        }
    }

    public <T> void insertElement(MongoCollection<T> collection, T document) {
        try {
            collection.insertOne(document);
        } catch (Exception e) {
            log.error("insertElement error: " + e.getMessage());
        }
    }

    public void insertElements(List<Document> documentList) {
        try {
            this.mongoDatabase
                    .get()
                    .getCollection(this.collectionName)
                    .insertMany(documentList);
        } catch (Exception e) {
            log.error("insertElements error: " + e.getMessage());
        }
    }

    public <T> void insertElements(MongoCollection<T> collection, List<T> document) {
        try {
            collection.insertMany(document);
        } catch (Exception e) {
            log.error("insertElement error: " + e.getMessage());
        }
    }

    public void replaceElement(String key, Object oldObject, Document document) {
        try {
            Document find = new Document(key, oldObject);
            this.mongoDatabase.get().getCollection(this.collectionName).replaceOne(find, document);
        } catch (Exception e) {
            log.error("replaceElement error: " + e.getMessage());
        }
    }

    public void replaceElements(HashMap<String,Object> documents, Document document) {
        try {
            for (Map.Entry<String, Object> entry : documents.entrySet()) {
                Document find = new Document(entry.getKey(), entry.getValue());
                this.mongoDatabase.get().getCollection(this.collectionName).replaceOne(find, document);
            }
        } catch (Exception e) {
            log.error("replaceElements error: " + e.getMessage());
        }
    }

    public String getText(Document document, String name) {
        try {
            return document.getString(name);
        } catch (Exception e) {
            return "";
        }
    }

    public void dropDataBase() {
        this.mongoClient.get().getDatabase(this.dbName).drop();
    }

    public void close() {
        this.mongoClient.get().close();
    }
}
