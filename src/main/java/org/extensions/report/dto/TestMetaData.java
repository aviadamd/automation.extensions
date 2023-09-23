package org.extensions.report.dto;

import com.aventstack.extentreports.model.Log;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.extensions.anontations.report.TestReportInfo;
import java.util.List;

public class TestMetaData {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String assignCategory;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String assignAuthor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Log> testLogs;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Throwable throwable;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    public TestMetaData() {}

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
    public void setAssignAuthor(String assignAuthor) { this.assignAuthor = assignAuthor; }
    public void setAssignCategory(String assignCategory) { this.assignCategory = assignCategory; }
    public void setError(String error) {this.error = error;}
    public void setTestLogs(List<Log> testLogs) { this.testLogs = testLogs; }
    public void setThrowable(Throwable throwable) { this.throwable = throwable; }

}
