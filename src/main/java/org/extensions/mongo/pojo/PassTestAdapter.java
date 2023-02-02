package org.extensions.mongo.pojo;

import com.mongodb.BasicDBObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class PassTestAdapter {

    public static Document toDocument(final PassTestInfoMongo passTest) {
        BasicDBObject append = passTest
                .append("_id", passTest.getId())
                .append("className", passTest.getClassName())
                .append("testInfo", passTest.getTestInfo());
        return DocumentAdapter.toDocument(append);
    }

    public static List<Document> toDocuments(final List<PassTestInfoMongo> passList) {
        List<Document> documents = new ArrayList<>();
        for (PassTestInfoMongo passTestInfoMongo: passList) {
            BasicDBObject append = passTestInfoMongo
                    .append("_id", passTestInfoMongo.getId())
                    .append("className", passTestInfoMongo.getClassName())
                    .append("testInfo", passTestInfoMongo.getTestInfo());
            documents.add(DocumentAdapter.toDocument(append));
        }
        return documents;
    }
}
