package org.utils.mongo.legacy;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.HashMap;
import java.util.List;

public interface MongoRepo {
    void deleteElement(Bson bson);
    void insertElement(Document document);
    <T> void insertElement(MongoCollection<T> collection, T document);
    void updateElement(Bson from, Bson to);
    void insertElements(List<Document> documentList);
    <T> void insertElements(MongoCollection<T> collection, List<T> document);
    void replaceElement(String key, Object oldObject, Document document);
    void replaceElements(HashMap<String,Object> documents, Document document);
    String getText(Document document, String name);
    Document findElementBy(BasicDBObject searchQuery);
    Document findElementBy(Bson query);
    FindIterable<Document> findElementsBy(BasicDBObject searchQuery);
    List<Document> documentsGetAllElements();
    FindIterable<Document> iterableGetAllElements();
    void dropDataBase();
    void close();
}
