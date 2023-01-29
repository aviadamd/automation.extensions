package org.poc.mongo.morphia;

import org.extensions.anontations.ClassProvider;
import org.extensions.ObjectsParametersResolver;
import org.bson.types.ObjectId;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mongo.morphia.MorphiaRepository;
import org.poc.mongo.pojos.Ingredient;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(ObjectsParametersResolver.class)
@ReportConfiguration(reportPath = "project.report.path", extraReportsBy = { FAIL, SKIP }, reportSettingsPath = "project.report.config")
public class MongoMorphiaDbTest {

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @ClassProvider(classObject = MorphiaRepository.class)
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void a_mobileTest(MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Aviad", true);
        morphiaRepository.setMorphiaRepository("mongodb://localhost:27017", "ingredientsDbNew")
                .getRepository()
                .getDatastore()
                .insert(bread);
    }

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @ClassProvider(classObject = MorphiaRepository.class)
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void b_mobileTest(MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Aviad", true);
        morphiaRepository.setMorphiaRepository("mongodb://localhost:27017", "ingredientsDbNew")
                .getRepository()
                .getDatastore()
                .insert(bread);
    }

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @ClassProvider(classObject = MorphiaRepository.class)
    @TestReportInfo(testId = 3, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void c_mobileTest(MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Aviad", true);
        morphiaRepository.setMorphiaRepository("mongodb://localhost:27017", "ingredientsDbNew")
                .getRepository()
                .getDatastore()
                .insert(bread);
    }

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @ClassProvider(classObject = MorphiaRepository.class)
    @TestReportInfo(testId = 4, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void d_mobileTest(MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Aviad", true);
        morphiaRepository.setMorphiaRepository("mongodb://localhost:27017", "ingredientsDbNew")
                .getRepository()
                .getDatastore()
                .insert(bread);
    }

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @ClassProvider(classObject = MorphiaRepository.class)
    @TestReportInfo(testId = 5, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void e_mobileTest(MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Aviad", true);
        morphiaRepository.setMorphiaRepository("mongodb://localhost:27017", "ingredientsDbNew")
                .getRepository()
                .getDatastore()
                .insert(bread);
    }

    @Test
    @Repeat(onStatus = { FAIL, SKIP })
    @ClassProvider(classObject = MorphiaRepository.class)
    @TestReportInfo(testId = 6, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void f_mobileTest(MorphiaRepository morphiaRepository) {
        Ingredient bread = new Ingredient(new ObjectId(),"Aviad", true);
        morphiaRepository.setMorphiaRepository("mongodb://localhost:27017", "ingredientsDbNew")
                .getRepository()
                .getDatastore()
                .insert(bread);
    }

}
