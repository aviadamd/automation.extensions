package org.component.jackson;

import com.aventstack.extentreports.AnalysisStrategy;
import lombok.extern.slf4j.Slf4j;
import org.extensions.jackson.JacksonProviderExtension;
import org.extensions.anontations.JacksonProvider;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestReportInfo;
import org.extensions.automation.mobile.CapabilitiesObject;
import org.extensions.report.ExtentReportExtension;
import org.data.files.jsonReader.JacksonExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.SKIP;
@Slf4j
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(value = { ExtentReportExtension.class , JacksonProviderExtension.class })
@ReportConfiguration(extraReportsBy = { FAIL, SKIP }, analysisStrategy = AnalysisStrategy.TEST)
public class JacksonExtensionReaderTest {

    @Test
    @DisplayName("readJsonTestA")
    @TestReportInfo(testId = 1, assignCategory = "poc", assignAuthor = "aviad", info = "JacksonReaderTest")
    @JacksonProvider(dir = "src/test/resources", fileName = "someJson.json", classObject = ObjectPojo.class)
    public void readJsonTestA(JacksonExtension<ObjectPojo> jacksonHelper) {
        List<ObjectPojo> objectPojo = jacksonHelper.readAllFromJson();
        objectPojo.forEach(pojo -> log.info(pojo.toString()));
        jacksonHelper.writeToJson(new ArrayList<>(Arrays.asList(new ObjectPojo("2","ella"), new ObjectPojo("3", "adva"))));
        jacksonHelper.writeToJson(new ObjectPojo("5","orit"));
        List<ObjectPojo> byMany = jacksonHelper.findsBy(test -> test.getId().equals("3"));
        byMany.forEach(pojo -> log.info(pojo.toString()));
        Optional<ObjectPojo> bySingle = jacksonHelper.findBy(test -> test.getId().equals("2"));
        bySingle.ifPresent(value -> log.info(value.toString()));
    }

    @Test
    @DisplayName("readJsonTestB")
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "JacksonReaderTest")
    @JacksonProvider(dir = "src/test/resources", fileName = "someJson.json", classObject = ObjectPojo.class)
    public void readJsonTestB(JacksonExtension<ObjectPojo> jacksonHelper) {
        List<ObjectPojo> objectPojo = jacksonHelper.readAllFromJson();
        objectPojo.forEach(pojo -> log.info(pojo.toString()));
        jacksonHelper.writeToJson(new ArrayList<>(Arrays.asList(new ObjectPojo("2","ella"), new ObjectPojo("3", "adva"))));
        jacksonHelper.writeToJson(new ObjectPojo("5","orit"));
        List<ObjectPojo> byMany = jacksonHelper.findsBy(test -> test.getId().equals("3"));
        byMany.forEach(pojo -> log.info(pojo.toString()));
        Optional<ObjectPojo> bySingle = jacksonHelper.findBy(test -> test.getId().equals("1"));
        bySingle.ifPresent(value -> log.info(value.toString()));
    }

    @Test
    @DisplayName("readJsonTestD")
    @TestReportInfo(testId = 3, assignCategory = "poc", assignAuthor = "aviad", info = "JacksonReaderTest")
    @JacksonProvider(dir = "src/test/resources", fileName = "someJson.json", classObject = ObjectPojo.class)
    public void readJsonTestC(JacksonExtension<ObjectPojo> jacksonHelper) {
        List<ObjectPojo> objectPojo = jacksonHelper.readAllFromJson();
        objectPojo.forEach(pojo -> log.info(pojo.toString()));
        jacksonHelper.writeToJson(new ArrayList<>(Arrays.asList(new ObjectPojo("2","ella"), new ObjectPojo("3", "adva"))));
        jacksonHelper.writeToJson(new ObjectPojo("5","orit"));
        List<ObjectPojo> byMany = jacksonHelper.findsBy(test -> test.getId().equals("3"));
        byMany.forEach(pojo -> log.info(pojo.toString()));
        Optional<ObjectPojo> bySingle = jacksonHelper.findBy(test -> test.getId().equals("6"));
        bySingle.ifPresent(value -> log.info(value.toString()));
    }

    @Test
    @DisplayName("readJsonTestD")
    @TestReportInfo(testId = 2, assignCategory = "poc", assignAuthor = "aviad", info = "JacksonReaderTest")
    @JacksonProvider(dir = "/src/test/resources", fileName = "androidCaps1.json", classObject = CapabilitiesObject.class)
    public void readJsonTestD(JacksonExtension<CapabilitiesObject> jacksonHelper) {
        log.info(jacksonHelper.readJson().getAvd());
        log.info(jacksonHelper.readJson().getClient());
        log.info(jacksonHelper.readJson().getAppPath());
        log.info(jacksonHelper.readJson().getAppBundleId());
        log.info(jacksonHelper.readJson().getAppiumBasePath());
    }
}
