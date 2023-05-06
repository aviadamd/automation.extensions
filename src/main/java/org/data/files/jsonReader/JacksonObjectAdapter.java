package org.data.files.jsonReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class JacksonObjectAdapter<T> {

    private final File file;
    private final Class<T> object;
    private final JacksonUtils<T> jacksonUtils;


    /**
     * JacksonExtensions
     * @param file the file
     * @param object your class object
     */
    public JacksonObjectAdapter(String dir, File file, Class<T> object) {
        new FilesHelper().createDirectory(dir);
        this.object = object;
        this.file = file;
        this.jacksonUtils = new JacksonUtils<>(new ObjectMapper());
    }

    /**
     * readAllFromJson
     * @return List<T> all yours class objects
     */
    public T readJson() {
        return this.jacksonUtils.readJson(this.object, this.file);
    }

    /**
     * readAllFromJson
     * @return List<T> all yours class objects
     */
    public List<T> readAllFromJson() {
        return this.jacksonUtils.readAllFromJson(this.object, this.file);
    }

    /**
     * writeToJson
     * @param insertList your class object
     */
    public void writeToJson(boolean readAndWrite, T insertList) {
        List<T> collectData = new ArrayList<>();
        if (readAndWrite) collectData.addAll(this.readAllFromJson());
        collectData.add(insertList);
        this.jacksonUtils.writeToJson(collectData, this.file);
    }

    /**
     * writeToJson
     * @param insertList your class object
     */
    public void writeToJson(boolean readAndWrite, List<T> insertList) {
        List<T> collectData = new ArrayList<>();
        if (readAndWrite) collectData.addAll(this.readAllFromJson());
        collectData.addAll(insertList);
        this.jacksonUtils.writeToJson(collectData, this.file);
    }

    /**
     * findBy
     * @param tPredicate for filter result
     * @return your class object
     */
    public Optional<T> findBy(Predicate<T> tPredicate) {
        for (T object: this.readAllFromJson()) {
            if (tPredicate.test(object)) {
                return Optional.ofNullable(object);
            }
        }
        return Optional.empty();
    }

    /**
     * findsBy
     * @param tPredicate for filter result
     * @return list of your class object
     */
    public List<T> findsBy(Predicate<T> tPredicate) {
        List<T> collector = new ArrayList<>();
        for (T object : this.readAllFromJson()) {
            if (tPredicate.test(object)) {
                collector.add(object);
            }
        }
        return collector;
    }

}
