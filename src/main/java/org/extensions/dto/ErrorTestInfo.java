package org.extensions.dto;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"className", "testName", "status", "status", "error","testInfo"})
public class ErrorTestInfo {
    private final String className;
    private final String testName;
    private final Status[] status;
    private final String error;
    private final TestInfoData testInfo;

    public ErrorTestInfo(String className, String testName, TestInfoData testInfo, Status[] status, String error) {
        this.className = className;
        this.testName = testName;
        this.status = status;
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

    public TestInfoData getTestInfo() {
        return testInfo;
    }

}
