package org.utils.files.jsonReader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesHelper {

    public boolean isFileExists(File file) {
        try {
            return file.exists();
        } catch (Exception exception) {
            return false;
        }
    }

    public void createDirectory(String path) {
        try {
            Files.createDirectory(Path.of(path));
        } catch (Exception ignore) {}
    }

    public void createDirectories(String path) {
        try {
            Files.createDirectories(Path.of(path));
        } catch (Exception ignore) {}
    }
}
