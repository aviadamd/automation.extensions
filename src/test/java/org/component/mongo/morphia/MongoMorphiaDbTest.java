package org.component.mongo.morphia;

import com.aventstack.extentreports.AnalysisStrategy;
import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.bson.types.ObjectId;
import org.extensions.anontations.report.ReportSetUp;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.mongo.morphia.MongoMorphiaDbExtension;
import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.utils.mongo.morphia.MorphiaRepository;
import org.component.mongo.pojos.Ingredient;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class, MongoMorphiaDbExtension.class })
@ReportSetUp(
        reportPath = "project.report.path",
        extraReportsBy = { FAIL, SKIP },
        reportSettingsPath = "project.report.config",
        analysisStrategy = AnalysisStrategy.CLASS,
        mongoConnection = "project.mongo.connection"
)
public class MongoMorphiaDbTest {

    @Test
    @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "ingredientsDbNew")
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "MongoMorphiaDbTest")
    public void a_mobileTest(MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(), "Aviad", true);
        morphiaRepository.getRepository().getDatastore().insert(bread);
    }

    @Test
    @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "ingredientsDbNew")
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "MongoMorphiaDbTest")
    public void b_mobileTest(MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Ella", true);
        morphiaRepository.insert(bread);
    }

    @Test
    @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "ingredientsDbNew")
    @TestReportInfo(testId = 3, assignCategory = "poc", assignAuthor = "aviad", info = "MongoMorphiaDbTest")
    public void c_mobileTest(MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Asaf", true);
        morphiaRepository.insert(bread);
    }

    @Test
    @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "ingredientsDbNew")
    @TestReportInfo(testId = 4, assignCategory = "poc", assignAuthor = "aviad", info = "MongoMorphiaDbTest")
    public void d_mobileTest(MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Yael", true);
        morphiaRepository.insert(bread);
    }

    @Test
    @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "ingredientsDbNew")
    @TestReportInfo(testId = 5, assignCategory = "poc", assignAuthor = "aviad", info = "MongoMorphiaDbTest")
    public void e_mobileTest(MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Dvir", true);
        morphiaRepository.insert(bread);
    }

    @Test
    @MongoMorphiaConnector(host = "mongodb://localhost:27017", dbName = "ingredientsDbNew")
    @TestReportInfo(testId = 6, assignCategory = "poc", assignAuthor = "aviad", info = "MongoMorphiaDbTest")
    public void f_mobileTest(MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Michal", true);
        morphiaRepository.insert(bread);
    }

}
