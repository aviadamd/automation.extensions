package org.extensions.mongo.pojo;

import com.mongodb.BasicDBObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public final class FailTestAdapter {

    public static Document toDocument(final FailTestInfoMongo failTest) {
        BasicDBObject append = failTest
                .append("_id", failTest.getId())
                .append("className", failTest.getClassName())
                .append("error", failTest.getError())
                .append("testInfo", failTest.getTestInfo());
        return DocumentAdapter.toDocument(append);
    }

    public static List<Document> toDocuments(final List<FailTestInfoMongo> failTest) {
        List<Document> documents = new ArrayList<>();
        for (FailTestInfoMongo failTestInfoMongo: failTest) {
            BasicDBObject append = failTestInfoMongo
                    .append("_id", failTestInfoMongo.getId())
                    .append("className", failTestInfoMongo.getClassName())
                    .append("error", failTestInfoMongo.getError())
                    .append("testInfo", failTestInfoMongo.getTestInfo());
            documents.add(DocumentAdapter.toDocument(append));
        }
        return documents;
    }
}
