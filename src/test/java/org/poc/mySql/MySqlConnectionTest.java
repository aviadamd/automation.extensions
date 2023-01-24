package org.poc.mySql;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.extensions.sql.MySqlDbExtension;
import org.extensions.report.ExtentReportListener;
import org.extensions.anontations.Repeat;
import org.extensions.anontations.mySql.MySqlConnector;
import org.extensions.anontations.mySql.MySqlConnectorManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestInfo;
import org.base.BaseMobile;
import java.util.List;

@Slf4j
@ReportConfiguration(
        reportPath = "target/reports",
        generateExtraReportsBy = { Status.FAIL, Status.SKIP },
        reportSettingsPath = "src/main/resources/reportConfig.json")
@ExtendWith(value = { ExtentReportListener.class, MySqlDbExtension.class })
@MySqlConnectorManager(connector = { @MySqlConnector(connection = "jdbc:mysql://127.0.0.1:3306", dbId = 1, userName = "root", userPassword = "5311072BsAviad") })
public class MySqlConnectionTest extends BaseMobile {

    @Test
    @TestInfo(assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    @Repeat(onStatus = { Status.FAIL, Status.SKIP })
    public void mySqlConnectionTest() {
        this.mySqlDbExtension()
                .getMySqlRepo()
                .get(1)
                .step(action -> {
                    List<Country> countriesList = action
                            .setQuery(new SQL() {{ SELECT("*").FROM("world.city"); }})
                            .queryToObjectsList(Country.class);

                    for (Country countries: countriesList) {
                        log.info("id: " + countries.getId());
                        log.info("name: " + countries.getName());
                        log.info("code: " + countries.getCountryCode());
                        log.info("district: " + countries.getDistrict());
                        log.info("population: " + countries.getPopulation());
                    }
                })
                .build();
    }
}

