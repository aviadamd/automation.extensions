package org.data.files.jsonReader;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Assertions;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JacksonUtils <T> {

    private final ObjectWriter writer;
    private final ObjectMapper mapper;

    public JacksonUtils(final ObjectMapper objectMapper) {
        this.mapper = objectMapper;
        this.writer = this.mapper.writerWithDefaultPrettyPrinter();
    }

    public ObjectMapper getMapper() {
        return this.mapper;
    }

    public T readJson(Class<T> tClass, String resource) {
        try {
            return this.mapper.readerFor(tClass).readValue(resource);
        } catch (Exception exception) {
            Assertions.fail("read from json exception " + exception.getMessage());
            return null;
        }
    }

    public T readJson(Class<T> tClass, File resource) {
        try {
            return this.mapper.readerFor(tClass).readValue(resource);
        } catch (Exception exception) {
            Assertions.fail("read from json exception " + exception.getMessage());
            return null;
        }
    }

    public List<T> readAllFromJson(Class<T> tClass, String resource) {
        try {
            MappingIterator<T> mappingIterator = this.mapper
                    .readerFor(tClass)
                    .readValues(resource);
            return mappingIterator.readAll();
        } catch (Exception exception) {
            Assertions.fail("read from json exception", exception);
            return new ArrayList<>();
        }
    }

    public List<T> readAllFromJson(Class<T> tClass, File resource) {
        try {
            MappingIterator<T> mappingIterator = this.mapper
                    .readerFor(tClass)
                    .readValues(resource);
            return mappingIterator.readAll();
        } catch (Exception exception) {
            Assertions.fail("read from json exception", exception);
            return new ArrayList<>();
        }
    }

    public void writeToJson(T insertList, File resource) {
        try {
            this.writer.writeValue(resource, insertList);
        } catch (Exception exception) {
            Assertions.fail("write to json exception", exception);
        }
    }

    public void writeToJson(List<T> insertList, File resource) {
        try {
            this.writer.writeValue(resource, insertList);
        } catch (Exception exception) {
            Assertions.fail("write to json exception", exception);
        }
    }
}
