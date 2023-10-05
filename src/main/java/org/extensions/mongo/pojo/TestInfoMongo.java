package org.extensions.mongo.pojo;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import java.util.List;

public class TestInfoMongo extends BasicDBObject {
    private ObjectId id;
    private String className;
    private Status status;
    private String assignCategory;
    private String assignAuthor;
    private List<Log> testLogs;

    public TestInfoMongo() {}

    public TestInfoMongo(ObjectId id, String className, Status status, String assignCategory, String assignAuthor, List<Log> testLogs) {
        this.id = id;
        this.className = className;
        this.status = status;
        this.assignCategory = assignCategory;
        this.assignAuthor = assignAuthor;
        this.testLogs = testLogs;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAssignCategory() {
        return assignCategory;
    }

    public void setAssignCategory(String assignCategory) {
        this.assignCategory = assignCategory;
    }

    public String getAssignAuthor() {
        return assignAuthor;
    }

    public void setAssignAuthor(String assignAuthor) {
        this.assignAuthor = assignAuthor;
    }

    public List<Log> getTestLogs() { return testLogs; }

    public void setTestLogs(List<Log> testLogs) {
        this.testLogs = testLogs;
    }
}
