package org.mongo.morphia;

import dev.morphia.DeleteOptions;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filter;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

public class MorphiaRepository {

    private final MorphiaMongoConnection repository;

    public MorphiaRepository(String uri, String dbName) {
        this.repository = new MorphiaMongoConnection(uri, dbName);
    }

    public <T> Query<T> query(Class<T> tClass) { return this.repository.getDatastore().createQuery(tClass); }

    public <T> void insert(T t) { this.repository.getDatastore().insert(t); }

    public <T> void save(T t) {
        this.repository.getDatastore().save(t);
    }

    public <T> void delete(T t) {
        this.repository.getDatastore().delete(t);
    }

    public <T> List<T> findsBy(Class<T> tClass, FindOptions findOptions) {
        return this.repository.getDatastore()
                .find(tClass)
                .iterator(findOptions)
                .toList();
    }

    public <T> List<T> findsBy(Class<T> tClass, Filter filterBy) {
        return this.repository.getDatastore()
                .find(tClass)
                .filter(filterBy)
                .iterator()
                .toList();
    }
    public <T> List<T> findByAll(Class<T> tClass) {
        return this.repository.getDatastore()
                .find(tClass)
                .stream()
                .collect(Collectors.toList());
    }

    public <T> void clear(Class<T> tClass, Document document) {
        this.repository.getDatastore()
                .find(tClass)
                .delete(new DeleteOptions().hint(document));
    }

    public <T> void clearAll(Class<T> tClass) {
        this.repository.getDatastore()
                .find(tClass)
                .delete(new DeleteOptions().multi(true));
    }

}
