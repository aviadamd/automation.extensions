package org.extensions.dto;

import org.extensions.anontations.report.TestInfo;

public class TestMetaData {
    private final String assignCategory;
    private final String assignAuthor;
    private final String assignDevice;

    public TestMetaData(TestInfo testInfo) {
        this.assignCategory = testInfo.assignCategory();
        this.assignAuthor = testInfo.assignAuthor();
        this.assignDevice = testInfo.assignDevice();
    }

    public String getAssignAuthor() { return assignAuthor; }
    public String getAssignDevice() { return assignDevice; }
    public String getAssignCategory() { return assignCategory; }

}
