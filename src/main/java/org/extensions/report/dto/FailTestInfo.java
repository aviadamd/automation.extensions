package org.extensions.report.dto;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Date;

@JsonPropertyOrder({ "id","className", "testName", "status", "error","testInfo"})
public class FailTestInfo {
    private Date id;
    private String className;
    private String testName;
    private Status[] status;
    private String error;
    private TestMetaData testInfo;
    public FailTestInfo() {}
    public FailTestInfo(Date id, String className, String testName, TestMetaData testInfo, String error) {
        this.id = id;
        this.className = className;
        this.testName = testName;
        this.error = error;
        this.testInfo = testInfo;
    }

    public Date getId() { return id; }
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
    public void setId(Date id) {
        this.id = id;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }
    public void setError(String error) {
        this.error = error;
    }
    public void setTestInfo(TestMetaData testInfo) {
        this.testInfo = testInfo;
    }
}
