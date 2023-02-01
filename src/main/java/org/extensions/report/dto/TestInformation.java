package org.extensions.report.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Date;

@JsonPropertyOrder({"id","className", "testName", "testInfo"})
public class TestInformation {
    private Date id;
    private String className;
    private String testName;
    private TestMetaData testInfo;
    public TestInformation() {}
    public TestInformation(Date id, String className, String testName, TestMetaData testInfo) {
        this.id = id;
        this.className = className;
        this.testName = testName;
        this.testInfo = testInfo;
    }

    public Date getId() { return id; }
    public String getClassName() {
        return className;
    }
    public String getTestName() {
        return testName;
    }
    public TestMetaData getTestInfo() {
        return testInfo;
    }
    public void setId(Date id) {
        this.id = id;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }
    public void setTestInfo(TestMetaData testInfo) {
        this.testInfo = testInfo;
    }
}
