package org.mongo.morphia;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class MorphiaMongoConnection {
    private final Datastore datastore;

    public MorphiaMongoConnection(String uri, String dbName) {
        this.datastore = Morphia.createDatastore(MongoClients.create(uri), dbName);
        this.datastore.ensureIndexes();
    }

    public Datastore getDatastore() { return this.datastore; }
}
