package org.poc.mongo.legacy;

import com.aventstack.extentreports.Status;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.extensions.mongo.MongoDbExtension;
import org.extensions.report.ExtentReportExtension;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.mongo.MongoConnector;
import org.extensions.anontations.mongo.MongoManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.poc.mongo.pojos.Countries;
import static org.extensions.mongo.MongoDbExtension.mongoRepo;

@Slf4j
@ExtendWith(value = { ExtentReportExtension.class, MongoDbExtension.class })
@ReportConfiguration(
        reportPath = "target/reports",
        extraReportsBy = { Status.FAIL, Status.SKIP },
        reportSettingsPath = "project.report.config")
@MongoManager(host = "mongodb://localhost:27017", mongoConnectors = {
        @MongoConnector(dbId = 1, dbName = "mobileDb", collectionName = "mobileCollection1"),
        @MongoConnector(dbId = 2, dbName = "mobileDb", collectionName = "mobileCollection2")
})
public class MongoDbExtensionTest {
    @Test
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    void testMongoConnection() {
        MongoCollection<Countries> collection = mongoRepo.get(1).createObject(Countries.class);
        mongoRepo.get(1).insertElement(collection, new Countries(new ObjectId(),"isreal","2","here","100"));
        mongoRepo.get(2).insertElement(collection, new Countries(new ObjectId(),"isreal","3","here","100"));
    }
}
