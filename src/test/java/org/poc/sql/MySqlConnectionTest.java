package org.poc.sql;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.extensions.anontations.mySql.MySqlConnector;
import org.extensions.sql.MySqlDbExtension;
import org.extensions.anontations.Repeat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.sql.MySqlSharedConnector;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;

@Slf4j
@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(value = { MySqlDbExtension.class })
@ReportConfiguration(
        reportPath = "project.report.path",
        extraReportsBy = { FAIL, SKIP },
        reportSettingsPath = "project.report.config",
        analysisStrategy = AnalysisStrategy.TEST,
        mongoConnection = "project.mongo.connection"
)
public class MySqlConnectionTest {

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    @MySqlConnector(connection = "jdbc:mysql://127.0.0.1:3306", userName = "root", userPassword = "5311072BsAviad")
    public void a_mySqlConnectionTest(@Autowired MySqlSharedConnector mySqlSharedConnector) {
        List<Country> countriesList = mySqlSharedConnector
                .setQuery(new SQL() {{ SELECT("*").FROM("world.city");}})
                .queryToObjectsList(Country.class);

        for (Country countries: countriesList) {
            log.info("id: " + countries.getId());
            log.info("name: " + countries.getName());
            log.info("code: " + countries.getCountryCode());
            log.info("district: " + countries.getDistrict());
            log.info("population: " + countries.getPopulation());
            break;
        }
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    @MySqlConnector(connection = "jdbc:mysql://127.0.0.1:3306", userName = "root", userPassword = "5311072BsAviad")
    public void b_mySqlConnectionTest(@Autowired MySqlSharedConnector mySqlSharedConnector) {
        List<Country> countriesList = mySqlSharedConnector
                .setQuery(new SQL() {{ SELECT("*").FROM("world.city"); }})
                .queryToObjectsList(Country.class);

        for (Country countries: countriesList) {
            log.info("id: " + countries.getId());
            log.info("name: " + countries.getName());
            log.info("code: " + countries.getCountryCode());
            log.info("district: " + countries.getDistrict());
            log.info("population: " + countries.getPopulation());
            break;
        }
    }

    @Test
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    @TestReportInfo(testId = 3, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    @MySqlConnector(connection = "jdbc:mysql://127.0.0.1:3306", userName = "root", userPassword = "5311072BsAviad")
    public void c_mySqlConnectionTest(@Autowired MySqlSharedConnector mySqlSharedConnector) {
        List<Country> countriesList = mySqlSharedConnector
                .setQuery(new SQL() {{ SELECT("*").FROM("world.city"); }})
                .queryToObjectsList(Country.class);

        for (Country countries: countriesList) {
            log.info("id: " + countries.getId());
            log.info("name: " + countries.getName());
            log.info("code: " + countries.getCountryCode());
            log.info("district: " + countries.getDistrict());
            log.info("population: " + countries.getPopulation());
            break;
        }
    }
}

