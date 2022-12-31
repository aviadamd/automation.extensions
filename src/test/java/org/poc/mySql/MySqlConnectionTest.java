package org.poc.mySql;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.extensions.MySqlDbExtension;
import org.extensions.ExtentReportListener;
import org.extensions.anontations.MySqlConnector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestInfo;

import java.util.List;

@Slf4j
@ReportConfiguration(
        reportPath = "target/reports",
        generateExtraReportsBy = { Status.FAIL, Status.SKIP },
        reportJsonSettingsPath = "src/main/resources/reportConfig.json")
@ExtendWith(value = { ExtentReportListener.class, MySqlDbExtension.class })
@MySqlConnector(connection = "jdbc:mysql://127.0.0.1:3306", dbId = 1, userName = "root", userPassword = "5311072BsAviad")
public class MySqlConnectionTest {

    @Test
    @TestInfo(assignCategory = "poc", assignAuthor = "aviad", assignDevice = "pixel")
    public void mySqlConnectionTest() {
        MySqlDbExtension.mySqlRepo
                .get(1)
                .step(action -> {
                    action.setQuery(new SQL() {{ SELECT("*").FROM("world.city"); }});
                    List<Countries> countriesList = action.queryToObjectsList(Countries.class);
                    for (Countries countries: countriesList) {
                        log.info(countries.getId());
                        log.info(countries.getName());
                        log.info(countries.getCountryCode());
                        log.info(countries.getDistrict());
                        log.info(countries.getPopulation());
                    }
                })
                .build();
    }
}

