package org.files.jsonReader;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
public class JacksonWriterExtension {
    private final File file;
    private final ObjectMapper objectMapper;

    /**
     * @param file
     */
    public JacksonWriterExtension(File file) {
        this.file = file;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * @return
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public ObjectWriter objectWriter(boolean ignoreNullValues) {
        try {
            if (ignoreNullValues) {
                return this.objectMapper
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .enable(SerializationFeature.INDENT_OUTPUT)
                        .writerWithDefaultPrettyPrinter();
            } else return this.objectMapper
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .writerWithDefaultPrettyPrinter();
        } catch (Exception exception) {
            throw new RuntimeException("object writer error " + exception);
        }
    }

    /**
     * @param dtoObject
     * @param dtoObjectClass
     * @param <T>
     * @return
     */
    public <T> JacksonWriterExtension readAndWrite(T dtoObject, Class<T> dtoObjectClass, boolean ignoreNullValues) {
        try {
            return this.readAndWrite(Collections.singletonList(dtoObject), dtoObjectClass, ignoreNullValues);
        } catch (Exception exception) {
            Assertions.fail("jackson and write write error ", exception);
        }

        return this;
    }

    /**
     * @param dtoObjectList
     * @param dtoObjectClass
     * @param <T>
     * @return
     */
    public <T> JacksonWriterExtension readAndWrite(List<T> dtoObjectList, Class<T> dtoObjectClass, boolean ignoreNullValues) {
        try {

            JacksonReaderExtension jsonReaderExtensions = new JacksonReaderExtension(this.file);
            List<T> dataList = new ArrayList<>(jsonReaderExtensions.readAndReturnJsonListOf(dtoObjectClass));
            int jsonUpdateList1 = dataList.size();
            dataList.addAll(dtoObjectList);
            if (jsonUpdateList1 < dataList.size()) {
                log.debug("file " + this.file.getName() + " has updated");
            }
            this.writeToJson(dataList, ignoreNullValues);
            return this;

        } catch (Exception exception) {
            Assertions.fail("jackson read and write error ", exception);
        }

        return this;
    }

    /**
     * @param newDataList
     * @param <T>
     */
    public <T> void writeToJson(T newDataList, boolean ignoreNullValues) {
        try {
            this.objectWriter(ignoreNullValues).writeValue(this.file, newDataList);
        } catch (Exception exception) {
            Assertions.fail("jackson write error ", exception);
        }
    }

    /**
     * @param newDataList
     * @param <T>
     */
    public <T> void writeToJson(List<T> newDataList, boolean ignoreNullValues) {
        try {
            this.objectWriter(ignoreNullValues).writeValue(this.file, newDataList);
        } catch (Exception exception) {
            Assertions.fail("jackson write error ", exception);
        }
    }

    /**
     * @param newDataList
     * @param <T>
     */
    public <T> void writeToJsonAsString(List<T> newDataList, boolean ignoreNullValues) {
        try {

            List<String> collector = new ArrayList<>();
            for (T value: newDataList) {
                collector.add(objectMapper.writeValueAsString(value));
            }
            this.objectWriter(ignoreNullValues).writeValue(this.file, collector);

        } catch (Exception exception) {
            Assertions.fail("jackson write error ", exception);
        }
    }
}
