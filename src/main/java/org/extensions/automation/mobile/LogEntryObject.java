package org.extensions.automation.mobile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.openqa.selenium.logging.LogEntry;

import java.util.List;

@JsonPropertyOrder({"logEntries"})
public class LogEntryObject {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LogEntry> logEntries;

    public LogEntryObject() {

    }
    public LogEntryObject(List<LogEntry> logEntries) {
        this.logEntries = logEntries;
    }

    public List<LogEntry> getLogEntries() {
        return logEntries;
    }

    public void setLogEntries(List<LogEntry> logEntries) {
        this.logEntries = logEntries;
    }

    @Override
    public String toString() {
        return "LogEntryObject{" +
                "logEntries=" + logEntries +
                '}';
    }
}