package org.utils.mongo.morphia;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.utils.mongo.manager.MongoConnectionPoolListener;
import org.utils.mongo.manager.MongoDbListener;

public class MorphiaMongoConnection {
    private final Datastore datastore;

    public MorphiaMongoConnection(String uri, String dbName) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .addCommandListener(new MongoDbListener())
                .applyToConnectionPoolSettings(builder -> builder.addConnectionPoolListener(new MongoConnectionPoolListener()))
                .build();

        this.datastore = Morphia.createDatastore(MongoClients.create(settings), dbName);
        this.datastore.ensureIndexes();
    }

    public Datastore getDatastore() { return this.datastore; }
}
