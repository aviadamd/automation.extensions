package org.poc.mongo;

import com.aventstack.extentreports.Status;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.experimental.filters.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.extensions.report.ExtentReportListener;
import org.extensions.mongo.MongoMorphiaDbExtension;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.extensions.anontations.mongo.MongoMorphiaManager;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static dev.morphia.query.experimental.filters.Filters.eq;
import static org.extensions.mongo.MongoMorphiaDbExtension.repository;

@Slf4j
@ExtendWith(value = { ExtentReportListener.class, MongoMorphiaDbExtension.class })
@ReportConfiguration(
        reportPath = "target/reports",
        generateExtraReportsBy = { Status.FAIL, Status.SKIP },
        reportSettingsPath = "src/main/resources/reportConfig.json")
@MongoMorphiaManager(host = "mongodb://localhost:27017", mongoConnectors = {
        @MongoMorphiaConnector(dbId = 1, dbName = "ingredientsDb"),
        @MongoMorphiaConnector(dbId = 2, dbName = "ingredientsDb")
})
public class MongoMorphiaDbExtensionTest {

    @Test
    @TestInfo(assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    public void morphiaTest() {
        Ingredient bread = new Ingredient(new ObjectId(),"Bread", true);
        Ingredient jelly = new Ingredient(new ObjectId(),"Jelly",false);
        Ingredient flour = new Ingredient(new ObjectId(),"Flour",false);

        repository.get(1).insert(bread);
        repository.get(1).insert(jelly);
        repository.get(1).insert(flour);

        repository.get(2).insert(bread);
        repository.get(2).insert(jelly);
        repository.get(2).insert(flour);

        jelly.setHealthy(true);
        repository.get(1).save(jelly);
        repository.get(1).delete(bread);
        repository.get(1).insert(bread);

        List<Ingredient> list1 = repository.get(1).findsBy(Ingredient.class, new FindOptions().sort(Sort.descending("name")));
        List<Ingredient> list2 = repository.get(1).findsBy(Ingredient.class, eq("healthy", true));
        List<Ingredient> list3 = repository.get(1).findByAll(Ingredient.class);

        Query<Ingredient> query1 = repository.get(1).query(Ingredient.class).filter(Filters.exists("_id"));
        List<Ingredient> query2 = repository.get(1).query(Ingredient.class)
                .field("name")
                .contains("Bread")
                .find()
                .toList();
        log.info(query1.toDocument().toJson());
        log.info(query2.toString());
        repository.get(1).clearAll(Ingredient.class);
    }
}
