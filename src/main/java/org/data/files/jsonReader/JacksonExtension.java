package org.data.files.jsonReader;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Assertions;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class JacksonExtension<T> {
    private final File file;
    private final Class<T> object;
    private final ObjectMapper objectMapper;
    private final ObjectWriter objectWriter;

    /**
     * JacksonExtensions
     * @param file the file
     * @param object your class object
     */
    public JacksonExtension(String dir, File file, Class<T> object) {
        FilesHelper filesHelper = new FilesHelper();
        filesHelper.createDirectory(dir);
        this.object = object;
        this.file = file;
        this.objectMapper = new ObjectMapper();
        this.objectWriter = this.objectMapper.writerWithDefaultPrettyPrinter();
    }

    private void createDir(String path) {
        try {
            Files.createDirectory(Path.of(path));
        } catch (Exception ignore) {}
    }
    /**
     * readAllFromJson
     * @return List<T> all yours class objects
     */
    public T readJson() {
        try {
            return this.objectMapper.readerFor(this.object).readValue(this.file);
        } catch (Exception exception) {
            Assertions.fail("read from json exception " + exception.getMessage());
            return null;
        }
    }

    /**
     * readAllFromJson
     * @return List<T> all yours class objects
     */
    public List<T> readAllFromJson() {
        try {
            MappingIterator<T> mappingIterator = this.objectMapper
                    .readerFor(this.object)
                    .readValues(this.file);
            return mappingIterator.readAll();
        } catch (Exception exception) {
            Assertions.fail("read from json exception", exception);
            return new ArrayList<>();
        }
    }

    /**
     * writeToJson
     * @param insertList your class object
     */
    public void writeToJson(T insertList) {
        try {
            List<T> jsonObjectList = this.readAllFromJson();
            jsonObjectList.add(insertList);
            this.objectWriter.writeValue(this.file, jsonObjectList);
        } catch (Exception exception) {
            Assertions.fail("write to json exception", exception);
        }
    }

    /**
     * writeToJson
     * @param insertList your class object
     */
    public void writeToJson(List<T> insertList) {
        try {
            this.objectWriter.writeValue(this.file, insertList);
        } catch (Exception exception) {
            Assertions.fail("write to json exception", exception);
        }
    }

    /**
     * writeToJson
     * @param insertList your class objects
     */
    public void readAndWriteToJson(List<T> insertList) {
        try {
            List<T> jsonObjectList = this.readAllFromJson();
            jsonObjectList.addAll(insertList);
            this.objectWriter.writeValue(this.file, jsonObjectList);
        } catch (Exception exception) {
            Assertions.fail("write to json exception", exception);
        }
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
