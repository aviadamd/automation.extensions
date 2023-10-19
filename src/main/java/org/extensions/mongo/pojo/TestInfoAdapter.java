package org.extensions.mongo.pojo;

import com.mongodb.BasicDBObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public final class TestInfoAdapter {

    public static Document toDocument(final TestInfoMongo testInfo) {
        BasicDBObject append = testInfo
                .append("_id", testInfo.getId())
                .append("className", testInfo.getClassName())
                .append("assignAuthor", testInfo.getAssignAuthor())
                .append("assignCategory", testInfo.getAssignCategory())
                .append("testStatus", testInfo.getStatus())
                .append("testLogs", testInfo.getTestLogs());
        return DocumentAdapter.toDocument(append);
    }

    public static List<Document> toDocuments(final List<TestInfoMongo> testInfoList) {
        List<Document> documents = new ArrayList<>();
        for (TestInfoMongo testInfo : testInfoList) {
            BasicDBObject append = testInfo
                    .append("_id", testInfo.getId())
                    .append("className", testInfo.getClassName())
                    .append("assignAuthor", testInfo.getAssignAuthor())
                    .append("assignCategory", testInfo.getAssignCategory())
                    .append("testStatus", testInfo.getStatus())
                    .append("testLogs", testInfo.getTestLogs());
            documents.add(DocumentAdapter.toDocument(append));
        }
        return documents;
    }
}
