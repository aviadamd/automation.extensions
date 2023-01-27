package org.extensions.mongo.pojo;

import com.aventstack.extentreports.Status;
import com.mongodb.BasicDBObject;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;
import org.extensions.dto.TestMetaData;

import java.util.Arrays;

@Entity("failTestInfo")
public class FailTestInfoMongo extends BasicDBObject {
    @Id
    private ObjectId id;
    private String className;
    private String testName;
    private Status[] status;
    private String error;
    private TestMetaData testInfo;

    public FailTestInfoMongo() {}
    public FailTestInfoMongo(ObjectId id, String className, String testName, TestMetaData testInfo, String error) {
        this.id = id;
        this.className = className;
        this.testName = testName;
        this.error = error;
        this.testInfo = testInfo;
    }

    public ObjectId getId() { return id; }
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
    public void setId(ObjectId id) {
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

    @Override
    public String toString() {
        return "FailTestInfoMongo{" +
                "id=" + id +
                ", className='" + className + '\'' +
                ", testName='" + testName + '\'' +
                ", status=" + Arrays.toString(status) +
                ", error='" + error + '\'' +
                ", testInfo=" + testInfo +
                '}';
    }
}
