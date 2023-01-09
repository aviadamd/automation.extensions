package org.files;

import java.io.File;
import java.util.HashMap;

public class FileGeneratorExtensions {
    private String path;
    private File file;
    private int fileNumber;
    private boolean isHaveOldFile;

    public FileGeneratorExtensions() {

    }

    public FileGeneratorExtensions(String path, int fileNumber, boolean isHaveOldFile) {
        this.fileNumber = fileNumber;
        this.path = path;
        this.file = new File(this.path);
        this.isHaveOldFile = isHaveOldFile;
    }

    public int getFileNumber() { return fileNumber; }
    public File getFile() { return file; }
    public String getPath() { return path; }
    public boolean getIsHaveOldFile() { return isHaveOldFile; }

    private static final HashMap<Integer,String> lastRegistrationEntry = new HashMap<>();
    protected static HashMap<Integer,String> getRegistrationsEntries() {
        return lastRegistrationEntry;
    }

    protected static void saveRegistrationsEntries(String fileName, int fileNumber) {
        if (!fileName.isEmpty() && fileNumber != 0) {
            System.setProperty(""+fileNumber+"", fileName);
            lastRegistrationEntry.put(fileNumber, fileName);
        }
    }
}
