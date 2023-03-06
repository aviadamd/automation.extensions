package org.utils.mongo.morphia;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.Mapper;
import dev.morphia.mapping.MapperOptions;
import dev.morphia.mapping.NamingStrategy;

public class MorphiaMongoConnection {
    private final Datastore datastore;
    private final Mapper mapper;

    public MorphiaMongoConnection(String uri, String dbName) {
        this.datastore = Morphia.createDatastore(MongoClients.create(uri), dbName);
        this.mapper = new Mapper(this.datastore, this.datastore.getDatabase().getCodecRegistry(), MapperOptions.builder().build());
        this.datastore.ensureIndexes();
    }
    public Datastore getDatastore() { return this.datastore; }
    public Mapper getMapper() { return this.mapper; }
}
