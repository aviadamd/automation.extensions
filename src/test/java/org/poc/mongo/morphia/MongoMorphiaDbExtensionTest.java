package org.poc.mongo.morphia;

import com.aventstack.extentreports.Status;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.experimental.filters.Filters;
import lombok.extern.slf4j.Slf4j;
import org.base.BaseMobile;
import org.bson.types.ObjectId;
import org.extensions.report.ExtentReportExtension;
import org.extensions.mongo.MongoMorphiaDbExtension;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.mongo.MongoMorphiaConnector;
import org.extensions.anontations.mongo.MongoMorphiaManager;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.poc.mongo.pojos.Ingredient;

import java.util.List;
import java.util.Optional;

import static dev.morphia.query.experimental.filters.Filters.eq;


@SuppressWarnings("removal")
@Slf4j
@ExtendWith(value = { ExtentReportExtension.class, MongoMorphiaDbExtension.class })
@ReportConfiguration(
        reportPath = "target/reports",
        extraReportsBy = { Status.FAIL, Status.SKIP },
        reportSettingsPath = "src/main/resources/reportConfig.json")
@MongoMorphiaManager(host = "mongodb://localhost:27017", mongoConnectors = {
        @MongoMorphiaConnector(dbId = 1, dbName = "ingredientsDb"),
        @MongoMorphiaConnector(dbId = 2, dbName = "ingredientsDb")
})
public class MongoMorphiaDbExtensionTest extends BaseMobile {

    @Test
    @TestInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    public void morphiaTest() {
        Ingredient bread = new Ingredient(new ObjectId(),"Bread", true);
        Ingredient jelly = new Ingredient(new ObjectId(),"Jelly",false);
        Ingredient flour = new Ingredient(new ObjectId(),"Flour",false);

        this.morphiaDbExtension().getRepository().get(1).insert(bread);
        this.morphiaDbExtension().getRepository().get(1).insert(jelly);
        this.morphiaDbExtension().getRepository().get(1).insert(flour);

        this.morphiaDbExtension().getRepository().get(2).insert(bread);
        this.morphiaDbExtension().getRepository().get(2).insert(jelly);
        this.morphiaDbExtension().getRepository().get(2).insert(flour);

        jelly.setHealthy(true);
        this.morphiaDbExtension().getRepository().get(1).save(jelly);
        this.morphiaDbExtension().getRepository().get(1).delete(bread);
        this.morphiaDbExtension().getRepository().get(1).insert(bread);

        List<Ingredient> list1 = this.morphiaDbExtension()
                .getRepository()
                .get(1)
                .findsBy(Ingredient.class, new FindOptions().sort(Sort.descending("name")));
        list1.forEach(e -> log.info(e.toString()));

        List<Ingredient> list2 = this.morphiaDbExtension()
                .getRepository()
                .get(1)
                .findsBy(Ingredient.class, eq("healthy", true));
        list2.forEach(e -> log.info(e.toString()));

        List<Ingredient> list3 = this.morphiaDbExtension()
                .getRepository()
                .get(1)
                .findByAll(Ingredient.class);
        list2.forEach(e -> log.info(e.toString()));

        Optional<Ingredient> ingredient = this.morphiaDbExtension().getRepository()
                .get(1)
                .findBy(Filters.eq("name","Jelly"), Ingredient.class);
        ingredient.ifPresent(obj -> log.info(obj.toString()));

        Query<Ingredient> query1 = this.morphiaDbExtension()
                .getRepository()
                .get(1)
                .query(Ingredient.class);
        Optional<Ingredient> ingredient1 = Optional.ofNullable(query1.filter(Filters.exists("_id")).field("isRunning").equal(true).first());
        ingredient1.ifPresent(obj -> log.info(obj.toString()));

        List<Ingredient> query2 = this.morphiaDbExtension()
                .getRepository().
                get(1)
                .query(Ingredient.class)
                .field("name")
                .contains("Bread")
                .find()
                .toList();
        log.info(query1.toDocument().toJson());
        log.info(query2.toString());
        this.morphiaDbExtension().getRepository().get(1).clearAll(Ingredient.class);
    }
}
