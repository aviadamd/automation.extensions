package org.extensions.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"className", "testName", "testInfo"})
public class PassTestInfo {
    private final String className;
    private final String testName;
    private final TestMetaData testInfo;

    public PassTestInfo(String className, String testName, TestMetaData testInfo) {
        this.className = className;
        this.testName = testName;
        this.testInfo = testInfo;
    }

    public String getClassName() {
        return className;
    }

    public String getTestName() {
        return testName;
    }

    public TestMetaData getTestInfo() {
        return testInfo;
    }
}