package org.utils.mongo.legacy;

import com.mongodb.BasicDBObject;
import org.bson.Document;

import java.util.HashMap;

public final class DocumentAdapter extends BasicDBObject {

    private DocumentAdapter() { }

    public static Document toDocument(HashMap<String,Object> vtHashMap) {
        return new Document(vtHashMap);
    }
}
