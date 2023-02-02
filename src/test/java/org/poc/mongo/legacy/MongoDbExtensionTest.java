package org.poc.mongo.legacy;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import com.mongodb.client.MongoCollection;
import org.bson.types.ObjectId;
import org.extensions.anontations.mongo.MongoLegacyConnector;
import org.extensions.mongo.legacy.MongoDbLegacyExtension;
import org.extensions.anontations.Repeat;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mongo.legacy.MongoRepoImplementation;
import org.poc.mongo.pojos.Countries;
import org.springframework.beans.factory.annotation.Autowired;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, MongoDbLegacyExtension.class })
@ReportConfiguration(
        reportPath = "project.report.path",
        extraReportsBy = { FAIL, SKIP },
        reportSettingsPath = "project.report.config",
        analysisStrategy = AnalysisStrategy.CLASS,
        mongoConnection = "mongodb://localhost:27017"
)
public class MongoDbExtensionTest {
    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @MongoLegacyConnector(host = "mongodb://localhost:27017", dbName = "newCollection", collectionName = "mobile")
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MongoDbExtensionTest")
    void a_testMongoConnection(@Autowired MongoRepoImplementation mongoRepo) {
        MongoCollection<Countries> collection = mongoRepo.createObject(Countries.class);
        mongoRepo.insertElement(collection, new Countries(new ObjectId(),"isreal","2","here","100"));
        mongoRepo.insertElement(collection, new Countries(new ObjectId(),"isreal","3","here","100"));
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @MongoLegacyConnector(host = "mongodb://localhost:27017", dbName = "newCollection", collectionName = "mobile")
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "MongoDbExtensionTest")
    void b_testMongoConnection(@Autowired MongoRepoImplementation mongoRepo) {
        MongoCollection<Countries> collection = mongoRepo.createObject(Countries.class);
        mongoRepo.insertElement(collection, new Countries(new ObjectId(),"isreal","2","here","100"));
        mongoRepo.insertElement(collection, new Countries(new ObjectId(),"isreal","3","here","100"));
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @MongoLegacyConnector(host = "mongodb://localhost:27017", dbName = "newCollection", collectionName = "mobile")
    @TestReportInfo(testId = 3, assignCategory = "poc", assignAuthor = "aviad", info = "MongoDbExtensionTest")
    void c_testMongoConnection(@Autowired MongoRepoImplementation mongoRepo) {
        MongoCollection<Countries> collection = mongoRepo.createObject(Countries.class);
        mongoRepo.insertElement(collection, new Countries(new ObjectId(),"isreal","2","here","100"));
        mongoRepo.insertElement(collection, new Countries(new ObjectId(),"isreal","3","here","100"));
    }
}
