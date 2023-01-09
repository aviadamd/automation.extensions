package org.files;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class JsonFileExtensions {

    private String targetPath = "/target/jsonFiles";

    public JsonFileExtensions setTargetPath(String targetPath) {
        this.targetPath = targetPath;
        return this;
    }

    /**
     * verifyInitiation of the file path
     * @param path path file
     * @param file file
     */
    public void verifyInitiation(String path, FileGeneratorExtensions file, Status status) {
        if (file == null || file.getFile() == null || file.getPath() == null) {
            log.error("File params ate not valid " + path);
        }
    }
    /**
     * @param fileName fileName
     * @param fileId file id
     * @param jsonLimitSize jsonLimitSize
     * @return FileGeneratorExtensions
     */
    public FileGeneratorExtensions register(String fileName, int fileId, long jsonLimitSize) {
        String userDir = System.getProperty("user.dir");
        String dir = this.createDir(userDir + this.targetPath);
        log.info("create json dir: " + dir);

        FileGeneratorExtensions register = this.registerOldFile(fileId, userDir, fileName, jsonLimitSize);
        if (register.getIsHaveOldFile()) return register;

        return this.registerNewFile(fileId, userDir, fileName);
    }

    /**
     * @param fileId fileId
     * @param userDir userDir
     * @param fileName fileName
     * @return
     * new FileGeneratorExtensions(setPath, fileId)
     * new FileGeneratorExtensions()
     */
    public FileGeneratorExtensions registerNewFile(int fileId, String userDir, String fileName) {
        String setPath = this.generatePath(userDir, fileName, fileId);
        log.debug("create new file: " + setPath + "");
        FileGeneratorExtensions.saveRegistrationsEntries(setPath, fileId);
        return new FileGeneratorExtensions(setPath, fileId, true);
    }

    /**
     * @param fileId fileId
     * @param userDir userDir
     * @param fileName fileName
     * @param jsonLimitSize jsonLimitSize
     * @return
     * Pair.of(true, new FileGeneratorExtensions(setPath, fileId))
     * Pair.of(false, new FileGeneratorExtensions());
     */
    public FileGeneratorExtensions registerOldFile(int fileId, String userDir, String fileName, long jsonLimitSize) {
        if (FileGeneratorExtensions.getRegistrationsEntries() != null && !FileGeneratorExtensions.getRegistrationsEntries().isEmpty()) {
            long fileSize;
            HashMap<Integer, String> registration = FileGeneratorExtensions.getRegistrationsEntries();

            for (Map.Entry<Integer, String> entry : registration.entrySet()) {
                if (this.checkCondition(entry.getKey(), fileId, entry.getValue())) {
                    fileSize = this.getFileSize(entry.getValue());
                    if (fileSize > jsonLimitSize) {
                        String setPath = this.generatePath(userDir, fileName, fileId);
                        FileGeneratorExtensions.saveRegistrationsEntries(setPath, fileId);
                        log.debug("exist file: " + entry.getValue() + ", create a new json " + setPath + ", file is to big " + fileSize + "kb");
                        return new FileGeneratorExtensions(setPath, fileId, true);
                    } else {
                        log.debug("exist file: " + entry.getValue() + ", stay with json " + fileSize + "kb");
                        FileGeneratorExtensions.saveRegistrationsEntries(entry.getValue(), entry.getKey());
                        return new FileGeneratorExtensions(entry.getValue(), entry.getKey(), true);
                    }
                }
            }
        }

        return new FileGeneratorExtensions("", 0, false);
    }

    /**
     * @param userDir userDir
     * @param fileName fileName
     * @param fileId fileId
     * @return fix path
     */
    public String generatePath(String userDir, String fileName, int fileId) {
        return userDir + "/target/jsonFiles/"
                + this.generateUUID(0,8)
                + "-" + fileId
                + "-" + fileName;
    }

    /**
     * @param from 0
     * @param to 8
     * @return uuid
     */
    public String generateUUID(int from, int to) {
        return UUID.randomUUID()
                .toString()
                .substring(from, to);
    }

    /**
     * create new dir
     * @param userDir dir
     * @return path
     */
    public String createDir(String userDir) {
        try {
            Path dir = Paths.get(userDir);
            Files.createDirectories(dir);
            return dir.getFileName().toString();
        } catch (Exception e) {
            log.error("createDir ex: " + e.getMessage());
        }
        return "";
    }

    /**
     * @param fileName full path file name
     * @return if the file is bigger from the long kb
     */
    public long getFileSize(String fileName) {
        try {
            return Files.size(Paths.get(fileName)) / 1024;
        } catch (IOException ioException) {
            log.error("getFileSize" + ioException.getMessage());
            return 0;
        }
    }

    /**
     * @param key expected the file id number
     * @param fileId actual the file id number
     * @param value file name file
     * @return true/false
     */
    private boolean checkCondition(Integer key, int fileId, String value) {
        return key == fileId
                && System.getProperty(String.valueOf(key)) != null
                && System.getProperty(String.valueOf(key)).equals(value);
    }
}
