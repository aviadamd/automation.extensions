package org.filesUtils.jsonReader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JsonReaderExtensions {

    private final File file;
    private final ObjectMapper objectMapper;

    /**
     *
     * @param file
     */
    public JsonReaderExtensions(File file) {
        this.file = file;
        this.objectMapper = new ObjectMapper();
    }

    /**
     *
     * @param setLogEntryDto
     * @param <T>
     * @return
     */
    public <T> List<T> readAndReturnJsonListOf(Class<T> setLogEntryDto) {
        List<T> entryDtoList = new ArrayList<>();

        try {

            if (this.file != null && this.file.exists()) {
                List<T> entryOldDtoList = this.readAllJson(setLogEntryDto);
                if (!entryOldDtoList.isEmpty()) entryDtoList.addAll(entryOldDtoList);
                return entryDtoList;
            }

        } catch (Exception ex) {
            log.error("readAndReturnJsonListOf ex:  " + ex.getMessage());
        }

        return entryDtoList;
    }

    /**
     *
     * @param dtoTypeClass
     * @param <T>
     * @return
     */
    public <T> List<T> readAllJson(Class<T> dtoTypeClass) {
        try {
            return this.readJson(dtoTypeClass).readAll();
        } catch (IOException io) {
            log.info("readAll IOException error " + io.getMessage());
        } catch (ClassCastException cc) {
            log.info("readAll ClassCastException error " + cc.getMessage());
        } catch (Exception e) {
            log.info("readAll Exception error " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     *
     * @param dtoTypeClass
     * @param <T>
     * @return
     */
    public <T> MappingIterator<T> readJson(Class<T> dtoTypeClass) {
        try {

            if (this.file.exists()) {
                ObjectReader objectReader = this.objectMapper.readerFor(dtoTypeClass);
                objectReader.createParser(this.file)
                        .enable(JsonParser.Feature.ALLOW_COMMENTS)
                        .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
                objectReader.readValues(this.file);
                return objectReader.readValues(this.file);
            }

        } catch (IOException io) {
            log.info("readAll IOException error " + io.getMessage());
        } catch (Exception ex) {
            log.info("readAll Exception error " + ex.getMessage());
        }
        return MappingIterator.emptyIterator();
    }
}
