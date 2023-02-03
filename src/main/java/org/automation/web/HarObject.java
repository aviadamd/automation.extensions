package org.automation.web;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.lightbody.bmp.core.har.HarEntry;

import java.util.List;

@JsonPropertyOrder({"testName", "harEntries"})
public class HarObject {
    private List<HarEntry> harEntries;
    private String testName;

    public HarObject() {}

    public HarObject(String testName, List<HarEntry> harEntries) {
        this.harEntries = harEntries;
        this.testName = testName;
    }

    public void setHarEntries(List<HarEntry> harEntries) { this.harEntries = harEntries; }
    public List<HarEntry> getHarEntries() { return harEntries; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }

}
