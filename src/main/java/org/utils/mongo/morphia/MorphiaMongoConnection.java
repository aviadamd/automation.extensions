package org.utils.mongo.morphia;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.Mapper;
import dev.morphia.mapping.MapperOptions;
import org.utils.mongo.MongoDbListener;

public class MorphiaMongoConnection {
    private final Datastore datastore;
    private final Mapper mapper;

    public MorphiaMongoConnection(String uri, String dbName) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .addCommandListener(new MongoDbListener())
                .build();

        this.datastore = Morphia.createDatastore(MongoClients.create(settings), dbName);
        this.mapper = new Mapper(this.datastore, this.datastore.getDatabase().getCodecRegistry(), MapperOptions.builder().build());
        this.datastore.ensureIndexes();


    }
    public Datastore getDatastore() { return this.datastore; }
    public Mapper getMapper() { return this.mapper; }
}
