package org.files.jsonReader;

import java.nio.file.Files;
import java.nio.file.Path;

public class FilesHelper {

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
