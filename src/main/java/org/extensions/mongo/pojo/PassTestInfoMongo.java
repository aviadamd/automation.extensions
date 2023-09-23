package org.extensions.mongo.pojo;

import com.mongodb.BasicDBObject;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;
import org.extensions.report.dto.TestMetaData;

import java.util.Date;

@Entity("passTestInfo")
public class PassTestInfoMongo extends BasicDBObject {
    @Id
    private ObjectId id;
    private String className;
    private TestMetaData testInfo;
    public PassTestInfoMongo() {}

    public PassTestInfoMongo(ObjectId id, String className, TestMetaData testInfo) {
        this.id = id;
        this.className = className;
        this.testInfo = testInfo;
    }

    public ObjectId getId() { return id; }
    public String getClassName() { return className;}
    public TestMetaData getTestInfo() {return testInfo;}
    public void setId(ObjectId id) {
        this.id = id;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public void setTestInfo(TestMetaData testInfo) {
        this.testInfo = testInfo;
    }

    @Override
    public String toString() {
        return "PassTestInfoMongo{" +
                "id=" + id +
                ", className='" + className + '\'' +
                ", testInfo=" + testInfo +
                '}';
    }
}
