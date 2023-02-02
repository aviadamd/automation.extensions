package org.extensions.report.dto;

import com.aventstack.extentreports.model.Log;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.extensions.anontations.report.TestReportInfo;
import java.util.List;

public class TestMetaData {
    private final String assignCategory;
    private final String assignAuthor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Log> testLogs;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Throwable throwable;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    public TestMetaData(TestReportInfo testInfo, String error) {
        this.assignCategory = testInfo.assignCategory();
        this.assignAuthor = testInfo.assignAuthor();
        this.error = error;
    }
    public TestMetaData(TestReportInfo testInfo, Throwable throwable) {
        this.assignCategory = testInfo.assignCategory();
        this.assignAuthor = testInfo.assignAuthor();
        this.throwable = throwable;
    }
    public TestMetaData(TestReportInfo testInfo, List<Log> testLogs) {
        this.assignCategory = testInfo.assignCategory();
        this.assignAuthor = testInfo.assignAuthor();
        this.testLogs = testLogs;
    }

    public TestMetaData(TestReportInfo testInfo, List<Log> testLogs, String error) {
        this.assignCategory = testInfo.assignCategory();
        this.assignAuthor = testInfo.assignAuthor();
        this.testLogs = testLogs;
        this.error = error;
    }

    public String getAssignAuthor() { return this.assignAuthor; }
    public String getAssignCategory() { return assignCategory; }
    public List<Log> getTestLogs() { return testLogs; }
    public Throwable getThrowable() { return throwable; }
    public String getError() { return error; }
}
