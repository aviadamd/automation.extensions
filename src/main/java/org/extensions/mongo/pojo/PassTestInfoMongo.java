package org.extensions.mongo.pojo;

import com.mongodb.BasicDBObject;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;
import org.extensions.dto.TestMetaData;

@Entity("passTestInfo")
public class PassTestInfoMongo extends BasicDBObject {
    @Id
    private ObjectId id;
    private String className;
    private String testName;
    private TestMetaData testInfo;

    public PassTestInfoMongo() {}

    public PassTestInfoMongo(ObjectId id, String className, String testName, TestMetaData testInfo) {
        this.id = id;
        this.className = className;
        this.testName = testName;
        this.testInfo = testInfo;
    }

    public ObjectId getId() { return id; }
    public String getClassName() { return className;}
    public String getTestName() {
        return testName;
    }
    public TestMetaData getTestInfo() {return testInfo;}
    public void setId(ObjectId id) {
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

    @Override
    public String toString() {
        return "PassTestInfoMongo{" +
                "id=" + id +
                ", className='" + className + '\'' +
                ", testName='" + testName + '\'' +
                ", testInfo=" + testInfo +
                '}';
    }
}
