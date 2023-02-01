package org.extensions.report.dto;

import org.extensions.anontations.report.TestReportInfo;

public class TestMetaData {
    private final String assignCategory;
    private final String assignAuthor;
    private final String assignDevice;

    public TestMetaData(TestReportInfo testInfo) {
        this.assignCategory = testInfo.assignCategory();
        this.assignAuthor = testInfo.assignAuthor();
        this.assignDevice = testInfo.info();
    }

    public String getAssignAuthor() { return assignAuthor; }
    public String getAssignDevice() { return assignDevice; }
    public String getAssignCategory() { return assignCategory; }

}
