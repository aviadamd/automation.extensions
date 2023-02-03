package org.poc.mongo.morphia;

import com.aventstack.extentreports.AnalysisStrategy;
import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.bson.types.ObjectId;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.mongo.morphia.MongoMorphiaDbExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mongo.morphia.MorphiaRepository;
import org.poc.mongo.pojos.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, MongoMorphiaDbExtension.class })
@ReportConfiguration(
        reportPath = "project.report.path",
        extraReportsBy = { FAIL, SKIP },
        reportSettingsPath = "project.report.config",
        analysisStrategy = AnalysisStrategy.CLASS,
        mongoConnection = "project.mongo.connection"
)
public class MongoMorphiaDbTest {

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "ingredientsDbNew")
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MongoMorphiaDbTest")
    public void a_mobileTest(@Autowired MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Aviad", true);
        morphiaRepository.getRepository().getDatastore().insert(bread);
    }

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "ingredientsDbNew")
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "MongoMorphiaDbTest")
    public void b_mobileTest(@Autowired MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Ella", true);
        morphiaRepository.getRepository().getDatastore().insert(bread);
    }

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "ingredientsDbNew")
    @TestReportInfo(testId = 3, assignCategory = "poc", assignAuthor = "aviad", info = "MongoMorphiaDbTest")
    public void c_mobileTest(@Autowired MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Asaf", true);
        morphiaRepository.getRepository().getDatastore().insert(bread);
    }

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "ingredientsDbNew")
    @TestReportInfo(testId = 4, assignCategory = "poc", assignAuthor = "aviad", info = "MongoMorphiaDbTest")
    public void d_mobileTest(@Autowired MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Yael", true);
        morphiaRepository.getRepository().getDatastore().insert(bread);
    }

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "ingredientsDbNew")
    @TestReportInfo(testId = 5, assignCategory = "poc", assignAuthor = "aviad", info = "MongoMorphiaDbTest")
    public void e_mobileTest(@Autowired MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Dvir", true);
        morphiaRepository.getRepository().getDatastore().insert(bread);
    }

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "ingredientsDbNew")
    @TestReportInfo(testId = 6, assignCategory = "poc", assignAuthor = "aviad", info = "MongoMorphiaDbTest")
    public void f_mobileTest(@Autowired MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Michal", true);
        morphiaRepository.getRepository().getDatastore().insert(bread);
    }

}
