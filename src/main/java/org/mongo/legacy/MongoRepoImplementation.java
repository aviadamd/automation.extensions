package org.mongo.legacy;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import java.util.*;
import static com.mongodb.MongoClient.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Slf4j
public class MongoRepoImplementation implements MongoRepo {
    private String dbName;
    private String collectionName;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public MongoRepoImplementation(String stringConnection, String dbName, String collectionName) {
        CodecProvider provider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry registry = fromRegistries(getDefaultCodecRegistry(), fromProviders(provider));

        try {
            this.dbName = dbName;
            this.mongoClient = MongoClients.create(stringConnection);
            this.mongoDatabase = this.mongoClient.getDatabase(dbName).withCodecRegistry(registry);
            this.collectionName = collectionName;
        } catch (Exception exception) {
            log.error("MongoRepoImplementation connection error: " + exception.getMessage());
        }
    }
    public MongoDatabase getMongoDatabase() { return mongoDatabase; }

    public <T> MongoCollection<T> createObject(Class<T> objectClass) {
        return this.mongoDatabase.getCollection(this.collectionName, objectClass);
    }

    @Override
    public Document findElementBy(BasicDBObject searchQuery) {
        try {
            return this.mongoDatabase.getCollection(this.collectionName).find(searchQuery).first();
        } catch (Exception e) {
            log.info("findElementBy error: " + e.getMessage());
            return new Document();
        }
    }

    @Override
    public FindIterable<Document> findElementsBy(BasicDBObject searchQuery) {
        try {
            return this.mongoDatabase.getCollection(this.collectionName).find(searchQuery);
        } catch (Exception e) {
            log.info("findElementsBy error: " + e.getMessage());
            return this.mongoDatabase.getCollection(this.collectionName).find();
        }
    }

    @Override
    public Document findElementBy(Bson query) {
        try {
            return this.mongoDatabase.getCollection(this.collectionName).find(query).first();
        } catch (Exception e) {
            log.info("findElementBy error: " + e.getMessage());
            return new Document();
        }
    }
    @Override
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

    @Override
    public FindIterable<Document> iterableGetAllElements() {
        return this.mongoDatabase.getCollection(this.collectionName).find();
    }

    @Override
    public void deleteElement(Bson bson) {
        try {
            this.mongoDatabase.getCollection(this.collectionName).deleteOne(bson);
        } catch (Exception e) {
            log.error("deleteElement error: " + e.getMessage());
        }
    }

    @Override
    public void updateElement(Bson from, Bson to) {
        try {
            this.mongoDatabase.getCollection(this.collectionName).updateOne(from, to);
        } catch (Exception e) {
            log.error("updateElement error: " + e.getMessage());
        }
    }

    @Override
    public void insertElement(Document document) {
        try {
            this.mongoDatabase.getCollection(this.collectionName).insertOne(document);
        } catch (Exception e) {
            log.error("insertElement error: " + e.getMessage());
        }
    }

    @Override
    public <T> void insertElement(MongoCollection<T> collection, T document) {
        try {
            collection.insertOne(document);
        } catch (Exception e) {
            log.error("insertElement error: " + e.getMessage());
        }
    }

    @Override
    public void insertElements(List<Document> documentList) {
        try {
            this.mongoDatabase.getCollection(this.collectionName).insertMany(documentList);
        } catch (Exception e) {
            log.error("insertElements error: " + e.getMessage());
        }
    }

    @Override
    public <T> void insertElements(MongoCollection<T> collection, List<T> document) {
        try {
            collection.insertMany(document);
        } catch (Exception e) {
            log.error("insertElement error: " + e.getMessage());
        }
    }

    @Override
    public void replaceElement(String key, Object oldObject, Document document) {
        try {
            Document find = new Document(key, oldObject);
            this.mongoDatabase.getCollection(this.collectionName).replaceOne(find, document);
        } catch (Exception e) {
            log.error("replaceElement error: " + e.getMessage());
        }
    }

    @Override
    public void replaceElements(HashMap<String,Object> documents, Document document) {
        try {
            for (Map.Entry<String, Object> entry : documents.entrySet()) {
                Document find = new Document(entry.getKey(), entry.getValue());
                this.mongoDatabase.getCollection(this.collectionName).replaceOne(find, document);
            }
        } catch (Exception e) {
            log.error("replaceElements error: " + e.getMessage());
        }
    }

    @Override
    public String getText(Document document, String name) {
        try {
            return document.getString(name);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void dropDataBase() {
        this.mongoClient.getDatabase(this.dbName).drop();
    }

    @Override
    public void close() {
        this.mongoClient.close();
    }
}
