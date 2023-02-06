package org.files.jsonReader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Assertions;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
public class JacksonHelper<T> {
    private final File file;
    private final Class<T> object;
    private final ObjectMapper objectMapper;
    private final ObjectWriter objectWriter;

    /**
     * JacksonExtensions single constructor
     * @param dir path to your dir
     * @param fileName the file name
     * @param object your class object
     */
    public JacksonHelper(String dir, String fileName, Class<T> object) {
        this.object = object;
        this.file = this.initFile(dir, fileName);
        this.objectMapper = new ObjectMapper();
        this.objectWriter = this.objectMapper.writerWithDefaultPrettyPrinter();
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
     * @param insertList your class objects
     */
    public void writeToJson(List<T> insertList) {
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
        for (T object: this.readAllFromJson()) {
            if (tPredicate.test(object)) {
                collector.add(object);
            }
        }
        return collector;
    }

    /**
     * removeFiled
     * clean field from json
     * @param filed String
     */
    public void removeFiled(String filed) {
        try {
            for (JsonNode jsonNode: this.objectMapper.readTree(this.file)) {
                ((ObjectNode) jsonNode).remove(filed);
            }
        } catch (Exception exception) {
            Assertions.fail("fail to remove filed from json ", exception);
        }
    }

    /**
     * removeFields
     * clean collection of fields from json
     * @param fields Collection<String>
     */
    public void removeFields(Collection<String> fields) {
        try {
            for (JsonNode jsonNode: this.objectMapper.readTree(this.file)) {
                ((ObjectNode) jsonNode).remove(fields);
            }
        } catch (Exception exception) {
            Assertions.fail("fail to remove filed from json ", exception);
        }
    }

    /*** clean all fields from json */
    public void removeAllFields() {
        try {
            this.objectMapper.readTree(this.file).fields().remove();
        } catch (Exception exception) {
            Assertions.fail("fail to remove filed from json ", exception);
        }
    }

    /**
     * initFile will create new file if not exists
     * @param dir dir location
     * @param fileName the file name
     * @return new or exist file
     */
    private File initFile(String dir, String fileName) {
        try {
            Path dirPath = Paths.get(dir);
            Path path = Path.of(Paths.get(dirPath + "/" + fileName).toString());
            return Files.exists(path) ? path.toFile() : Files.createFile(path).toFile();
        } catch (Exception exception) {
            Assertions.fail("init file is thrown exception", exception);
            return null;
        }
    }
}
