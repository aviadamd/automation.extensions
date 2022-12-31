package org.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.HashMap;
import java.util.List;

public interface MongoCollectionRepo {
    void deleteElement(Bson bson);
    void insertElement(Document document);
    void updateElement(Bson from, Bson to);
    void insertElements(List<Document> documentList);
    void replaceElement(String key, Object oldObject, Document document);
    void replaceElements(HashMap<String,Object> documents, Document document);
    String getText(Document document, String name);
    Document findElementBy(BasicDBObject searchQuery);
    FindIterable<Document> findElementsBy(BasicDBObject searchQuery);
    List<Document> documentsGetAllElements();
    FindIterable<Document> iterableGetAllElements();
    void dropDataBase();
    void close();
}
