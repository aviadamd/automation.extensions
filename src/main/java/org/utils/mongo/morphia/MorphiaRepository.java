package org.utils.mongo.morphia;

import dev.morphia.DeleteOptions;
import dev.morphia.mapping.Mapper;
import dev.morphia.query.FindOptions;
import dev.morphia.query.experimental.filters.Filter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class MorphiaRepository {
    private final MorphiaMongoConnection repository;
    public synchronized MorphiaMongoConnection getRepository() { return this.repository; }
    public synchronized Mapper getMapper() { return this.repository.getMapper(); }

    public MorphiaRepository(String uri, String dbName) {
        this.repository = new MorphiaMongoConnection(uri, dbName);
    }

    public <T> void insert(T t) {
        this.repository.getDatastore().insert(t);
    }
    public <T> void insert(List<T> t) {
        this.repository.getDatastore().insert(t);
    }
    public <T> void save(T t) {
        this.repository.getDatastore().save(t);
    }
    public <T> void delete(T t) {
        this.repository.getDatastore().delete(t);
    }
    public <T> Optional<T> findBy(Filter filter, Class<T> tClass) {
        return Optional.ofNullable(this.repository.getDatastore().find(tClass).filter(filter).first());
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
    public void close() {
        this.repository.getDatastore().getDatabase().drop();
    }
}
