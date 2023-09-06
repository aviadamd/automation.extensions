package org.component.sql;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.component.sql.pojo.Country;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.utils.sql.MySqlSharedConnector;
import java.util.List;

@Slf4j
@ReportConfiguration()
@Execution(ExecutionMode.SAME_THREAD)
public class MySqlDbTest {

    @Test
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "pixel")
    public void a_mySqlConnectionTest() {
        MySqlSharedConnector mySqlSharedConnector =
                new MySqlSharedConnector("jdbc:mysql://127.0.0.1:3306","root","5311072BsAviad");

        List<Country> countriesList = mySqlSharedConnector
                .setQuery(new SQL() {{ SELECT("*").FROM("world.city");}})
                .findsBy(Country.class);

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
