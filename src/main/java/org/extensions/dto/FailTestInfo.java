package org.extensions.dto;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"className", "testName", "status", "error","testInfo"})
public class FailTestInfo {
    private final String className;
    private final String testName;
    private Status[] status;
    private final String error;
    private final TestMetaData testInfo;
    public FailTestInfo(String className, String testName, TestMetaData testInfo, String error) {
        this.className = className;
        this.testName = testName;
        this.error = error;
        this.testInfo = testInfo;
    }

    public Status[] getStatus() {
        return status;
    }
    public String getClassName() {
        return className;
    }
    public String getTestName() {
        return testName;
    }
    public String getError() { return error; }
    public TestMetaData getTestInfo() {
        return testInfo;
    }
    public void setStatus(Status[] status) { this.status = status; }
}
