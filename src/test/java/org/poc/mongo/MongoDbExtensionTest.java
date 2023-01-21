package org.poc.mongo;

import com.aventstack.extentreports.Status;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.extensions.mongo.MongoDbExtension;
import org.extensions.report.ExtentReportListener;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.mongo.MongoConnector;
import org.extensions.anontations.mongo.MongoManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestInfo;
import org.mongo.Flower;
import org.poc.mySql.Countries;
import java.util.*;
import static java.util.Arrays.asList;
import static org.extensions.report.ExtentReportListener.extentTest;
import static org.extensions.mongo.MongoDbExtension.mongoRepo;

@Slf4j
@ExtendWith(value = { ExtentReportListener.class, MongoDbExtension.class })
@ReportConfiguration(
        reportPath = "target/reports",
        generateExtraReportsBy = { Status.FAIL, Status.SKIP },
        reportSettingsPath = "src/main/resources/reportConfig.json")
@MongoManager(host = "mongodb://localhost:27017", mongoConnectors = {
        @MongoConnector(dbId = 1, dbName = "mobileDb", collectionName = "mobileCollection1"),
        @MongoConnector(dbId = 2, dbName = "mobileDb", collectionName = "mobileCollection2")
})
public class MongoDbExtensionTest {
    @Test
    @TestInfo(assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    void testMongoConnection() {
        MongoCollection<Countries> collection = mongoRepo.get(1).createObject(Countries.class);

        mongoRepo.get(1).insertElement(collection, new Countries(new ObjectId(),"isreal","2","here","100"));
        mongoRepo.get(1).documentsGetAllElements().forEach(document -> {
            extentTest.info(document.toString());
        });

        mongoRepo.get(2).insertElement(collection, new Countries(new ObjectId(),"isreal","3","here","100"));
        mongoRepo.get(2).documentsGetAllElements().forEach(document -> {
            extentTest.info(document.toString());
        });
    }
    @Test
    @TestInfo(assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    void mongoTest() {
        MongoCollection<Flower> collection = mongoRepo.get(1).createObject(Flower.class);

        Flower flower1 = new Flower(new ObjectId(),"flower", asList("red", "yellow"),false,35.4f);

        mongoRepo.get(1).insertElement(collection, flower1);
        Optional<Flower> firstFlower = Optional.ofNullable(collection.find().first());

        if (firstFlower.isPresent()) {
            log.info(firstFlower.get().toString());
            log.info(firstFlower.get().getId().toString());
            log.info(firstFlower.get().getName());
        }

        Flower flower2 = new Flower(new ObjectId(),"flower", asList("red", "yellow"),false,35.4f);
        Flower flower3 = new Flower(new ObjectId(),"flower", asList("red", "yellow"),false,35.4f);
        Flower flower4 = new Flower(new ObjectId(),"flower", asList("red", "yellow"),false,35.4f);
        mongoRepo.get(1).insertElements(collection, asList(flower1, flower2, flower3, flower4));
        List<Flower> flowerList = new ArrayList<>();
        collection.find().into(flowerList);
        flowerList.forEach(e -> log.info(e.toString()));
    }
}
