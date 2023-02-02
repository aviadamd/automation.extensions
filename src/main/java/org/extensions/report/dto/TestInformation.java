package org.extensions.report.dto;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"time", "status", "className", "testName", "testInfo"})
public class TestInformation {
    private String time;
    private String className;
    private Status[] status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TestMetaData testInfo;

    public TestInformation() {}

    public TestInformation(String time, String className, TestMetaData testInfo) {
        this.time = time;
        this.className = className;
        this.testInfo = testInfo;
    }

    public String getTime() { return time; }
    public String getClassName() {
        return className;
    }
    public TestMetaData getTestInfo() {
        return testInfo;
    }
    public Status[] getStatus() { return status; }

    public void setTime(String id) {
        this.time = time;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public void setTestInfo(TestMetaData testInfo) {
        this.testInfo = testInfo;
    }
    public void setStatus(Status[] status) { this.status = status; }
}
