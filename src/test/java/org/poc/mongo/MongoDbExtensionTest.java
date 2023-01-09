package org.poc.mongo;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.extensions.MongoDbExtension;
import org.extensions.ExtentReportListener;
import org.extensions.anontations.mongo.MongoConnector;
import org.extensions.anontations.mongo.MongoManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestInfo;
import org.poc.mySql.Countries;
import static org.extensions.ExtentReportListener.extentTest;
import static org.extensions.MongoDbExtension.mongoRepo;
@Slf4j
@ExtendWith(value = { ExtentReportListener.class, MongoDbExtension.class })
@ReportConfiguration(
        reportPath = "target/reports",
        generateExtraReportsBy = { Status.FAIL, Status.SKIP },
        reportJsonSettingsPath = "src/main/resources/reportConfig.json")
@MongoManager(host = "localhost:27017", mongoConnectors = {
        @MongoConnector(dbId = 1, dbName = "mobileDb", collectionName = "mobileCollection1"),
        @MongoConnector(dbId = 2, dbName = "mobileDb", collectionName = "mobileCollection2")
})
public class MongoDbExtensionTest {
    @Test
    @TestInfo(assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    void testMongoConnection() {
        mongoRepo.get(1).insertElement(CountriesDtoAdapter.toDocument(new Countries("1","isreal","2","here","100")));
        mongoRepo.get(1).documentsGetAllElements().forEach(document -> {
            extentTest.info(document.toJson());
        });

        mongoRepo.get(2).insertElement(CountriesDtoAdapter.toDocument(new Countries("2","isreal","3","here","100")));
        mongoRepo.get(2).documentsGetAllElements().forEach(document -> {
            extentTest.info(document.toJson());
        });
    }
}
