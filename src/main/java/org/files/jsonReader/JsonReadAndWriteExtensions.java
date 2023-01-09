package org.files.jsonReader;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.MappingIterator;
import org.files.FileGeneratorExtensions;
import org.files.JsonFileExtensions;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JsonReadAndWriteExtensions {

    private final JsonReaderExtensions jsonReaderExtensions;
    private final JsonWriterExtensions jsonWriterExtensions;

    private String targetPath = "/target/jsonFiles";

    public JsonReadAndWriteExtensions setTargetPath(String targetPath) {
        this.targetPath = targetPath;
        return this;
    }

    public JsonReadAndWriteExtensions(String filePath) {
        File file = new File(filePath);
        this.jsonWriterExtensions = new JsonWriterExtensions(file);
        this.jsonReaderExtensions = new JsonReaderExtensions(file);
    }
    /**
     *
     * @param fileName
     * @param fileId
     */
    public JsonReadAndWriteExtensions(String fileName, int fileId) {
        this(fileName, fileId, 6000);
    }

    /**
     *
     * @param fileName
     * @param fileId
     * @param jsonFileSizeLimit
     */
    public JsonReadAndWriteExtensions(String fileName, int fileId, long jsonFileSizeLimit) {
        JsonFileExtensions jsonFileExtensions = new JsonFileExtensions();

        FileGeneratorExtensions createNewFile = jsonFileExtensions
                .setTargetPath(this.targetPath)
                .register(fileName, fileId, jsonFileSizeLimit);
        jsonFileExtensions.verifyInitiation(fileName, createNewFile, Status.INFO);

        this.jsonReaderExtensions = new JsonReaderExtensions(createNewFile.getFile());
        this.jsonWriterExtensions = new JsonWriterExtensions(createNewFile.getFile());
    }

    /**
     *
     * @param dtoObjectList
     * @param <V>
     * @return
     */
    public <V> JsonReadAndWriteExtensions write(V dtoObjectList) {
        this.jsonWriterExtensions.writeToJson(dtoObjectList);
        return this;
    }

    /**
     *
     * @param dtoObjectList
     * @param <V>
     * @return
     */
    public <V> JsonReadAndWriteExtensions write(List<V> dtoObjectList) {
        this.jsonWriterExtensions.writeToJson(dtoObjectList);
        return this;
    }

    /**
     *
     * @param dtoObjectList
     * @param dtoTypeClass
     * @param <V>
     * @return
     */
    public <V> JsonReadAndWriteExtensions readAndWrite(V dtoObjectList, Class<V> dtoTypeClass) {
        this.jsonWriterExtensions.readAndWrite(dtoObjectList, dtoTypeClass);
        return this;
    }

    /**
     *
     * @param dtoObjectList
     * @param dtoTypeClass
     * @param <V>
     * @return
     */
    public <V> JsonReadAndWriteExtensions readAndWrite(List<V> dtoObjectList, Class<V> dtoTypeClass) {
        this.jsonWriterExtensions.readAndWrite(dtoObjectList, dtoTypeClass);
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
