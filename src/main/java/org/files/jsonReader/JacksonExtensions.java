package org.files.jsonReader;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.MappingIterator;
import org.files.FileGeneratorExtensions;
import org.files.JsonFileExtensions;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JacksonExtensions {
    private final JacksonReaderExtension jsonReaderExtensions;
    private final JacksonWriterExtension jsonWriterExtensions;

    private String targetPath = "/target/jsonFiles";

    public JacksonExtensions setTargetPath(String targetPath) {
        this.targetPath = targetPath;
        return this;
    }

    public JacksonExtensions(String filePath) {
        File file = new File(filePath);
        this.jsonWriterExtensions = new JacksonWriterExtension(file);
        this.jsonReaderExtensions = new JacksonReaderExtension(file);
    }
    /**
     *
     * @param fileName
     * @param fileId
     */
    public JacksonExtensions(String fileName, int fileId) {
        this(fileName, fileId, 6000);
    }

    /**
     *
     * @param fileName
     * @param fileId
     * @param jsonFileSizeLimit
     */
    public JacksonExtensions(String fileName, int fileId, long jsonFileSizeLimit) {
        JsonFileExtensions jsonFileExtensions = new JsonFileExtensions();

        FileGeneratorExtensions createNewFile = jsonFileExtensions
                .setTargetPath(this.targetPath)
                .register(fileName, fileId, jsonFileSizeLimit);
        jsonFileExtensions.verifyInitiation(fileName, createNewFile, Status.INFO);

        this.jsonReaderExtensions = new JacksonReaderExtension(createNewFile.getFile());
        this.jsonWriterExtensions = new JacksonWriterExtension(createNewFile.getFile());
    }

    /**
     *
     * @param dtoObjectList
     * @param <V>
     * @return
     */
    public <V> JacksonExtensions write(V dtoObjectList, boolean ignoreNullValues) {
        this.jsonWriterExtensions.writeToJson(dtoObjectList, ignoreNullValues);
        return this;
    }

    /**
     *
     * @param dtoObjectList
     * @param <V>
     * @return
     */
    public <V> JacksonExtensions write(List<V> dtoObjectList, boolean ignoreNullValues) {
        this.jsonWriterExtensions.writeToJson(dtoObjectList, ignoreNullValues);
        return this;
    }

    /**
     *
     * @param dtoObjectList
     * @param dtoTypeClass
     * @param <V>
     * @return
     */
    public <V> JacksonExtensions readAndWrite(V dtoObjectList, Class<V> dtoTypeClass, boolean ignoreNullValues) {
        this.jsonWriterExtensions.readAndWrite(dtoObjectList, dtoTypeClass, ignoreNullValues);
        return this;
    }

    /**
     *
     * @param dtoObjectList
     * @param dtoTypeClass
     * @param <V>
     * @return
     */
    public <V> JacksonExtensions readAndWrite(List<V> dtoObjectList, Class<V> dtoTypeClass, boolean ignoreNullValues) {
        this.jsonWriterExtensions.readAndWrite(dtoObjectList, dtoTypeClass, ignoreNullValues);
        return this;
    }

    /**
     *
     * @param dtoTypeClass
     * @param <V>
     * @return
     */
    public <V> MappingIterator<V> read(Class<V> dtoTypeClass) {
        return this.jsonReaderExtensions.readJson(dtoTypeClass);
    }

    /**
     *
     * @param dtoClassObject
     * @param <V>
     * @return
     */
    public <V> List<V> readAll(Class<V> dtoClassObject) {
        return this.jsonReaderExtensions.readAllJson(dtoClassObject);
    }

    /**
     *
     * @param dtoObject
     * @param predicate
     * @param <L>
     * @return
     */
    public <L> List<L> filterBy(Class<L> dtoObject, Predicate<L> predicate) {
        return this.readAll(dtoObject)
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
