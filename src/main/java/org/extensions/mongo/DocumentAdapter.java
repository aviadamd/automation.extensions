package org.extensions.mongo;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import java.util.HashMap;

public class DocumentAdapter extends BasicDBObject {

    private DocumentAdapter() { }

    public static Document toDocument(HashMap<String,Object> vtHashMap) {
        return new Document(vtHashMap);
    }
}
