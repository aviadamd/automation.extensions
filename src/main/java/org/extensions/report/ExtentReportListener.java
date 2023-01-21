package org.extensions.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.extensions.anontations.Repeat;
import org.extensions.dto.FailTestInfo;
import org.extensions.dto.PassTestInfo;
import org.extensions.dto.TestMetaData;
import org.files.jsonReader.JsonReadAndWriteExtensions;
import org.junit.jupiter.api.extension.*;
import org.extensions.anontations.report.ReportConfiguration;
import org.extensions.anontations.report.TestInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static java.util.Arrays.deepToString;

@Aspect
@Slf4j
@Component
@Configuration
public class ExtentReportListener implements
        TestWatcher,
        BeforeAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        AfterAllCallback {
    public static ExtentTest extentTest;
    private ExtentSparkReporter sparkReporter;
    private ExtentReports extentReports = new ExtentReports();
    private static final List<FailTestInfo> failTests = new ArrayList<>();
    private static final List<PassTestInfo> passTests = new ArrayList<>();

    @Override
    public void beforeAll(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestClass()).isPresent() && context.getElement().isPresent()) {
            Optional<ReportConfiguration> configuration = Optional.ofNullable(context.getElement().get().getAnnotation(ReportConfiguration.class));
            if (configuration.isPresent()) {
                try {
                    this.extentReports = new ExtentReports();
                    this.sparkReporter = new ExtentSparkReporter(configuration.get().reportPath() + "/Spark.html");
                    this.extentReports.attachReporter(this.sparkReporter);
                    this.sparkReporter.loadJSONConfig(new File(configuration.get().reportSettingsPath()));
                } catch (Exception exception) {
                    log.error("sparkReporter.loadJSONConfig error " + exception.getMessage());
                }
            }
        }
    }
    @Override
    public void beforeEach(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestMethod()).isPresent() && context.getElement().isPresent()) {
            Optional<TestInfo> reportTest = Optional.ofNullable(context.getElement().get().getAnnotation(TestInfo.class));
            if (reportTest.isPresent()) {
                extentTest = this.extentReports.createTest(context.getRequiredTestMethod().getName());
                extentTest.assignCategory(reportTest.get().assignCategory());
                extentTest.assignAuthor(reportTest.get().assignAuthor());
                extentTest.assignDevice(reportTest.get().assignDevice());
                extentTest.info(context.getRequiredTestMethod().getName() + " started");
            }
        }
    }
    @Override
    public void afterEach(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestMethod()).isPresent()) {
            extentTest.info("method " + context.getRequiredTestMethod().getName() + " finished");
        }
    }
    @Override
    public void testSuccessful(ExtensionContext context) {
        if (Optional.ofNullable(context.getRequiredTestClass()).isPresent() && context.getElement().isPresent()) {
            Optional<TestInfo> reportTest = Optional.ofNullable(context.getElement().get().getAnnotation(TestInfo.class));
            reportTest.ifPresent(testInfo ->
                    passTests.add(new PassTestInfo(
                            context.getRequiredTestClass().getSimpleName(),
                            context.getRequiredTestMethod().getName(),
                            new TestMetaData(testInfo)
                    ))
            );
            extentTest.pass(context.getRequiredTestClass().getSimpleName() + " pass");
            extentTest.pass(context.getRequiredTestMethod().getName() + " pass");
        }
    }
    @Override
    public void testFailed(ExtensionContext context, Throwable throwable) {
        if (Optional.ofNullable(context.getRequiredTestClass()).isPresent() && context.getExecutionException().isPresent()) {
            Throwable error = context.getExecutionException().get();

            context.getElement().ifPresent(element -> {
                Optional<TestInfo> reportTest = Optional.ofNullable(element.getAnnotation(TestInfo.class));
                Optional<Repeat> repeat = Optional.ofNullable(element.getAnnotation(Repeat.class));
                reportTest.ifPresent(testInfo -> {
                    FailTestInfo failTestInfo = new FailTestInfo(
                            context.getRequiredTestClass().getSimpleName(),
                            context.getRequiredTestMethod().getName(),
                            new TestMetaData(testInfo),
                            error.getMessage()
                    );
                    repeat.ifPresent(value -> failTestInfo.setStatus(value.onStatus()));
                    failTests.add(failTestInfo);
                });
            });

            extentTest.fail("fail from class " + context.getRequiredTestClass().getSimpleName());
            extentTest.fail("fail from method " + context.getRequiredTestMethod().getName());
            extentTest.fail("error message " + error.getMessage());
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (context.getElement().isPresent()) {

            Optional<ReportConfiguration> reportConfiguration = Optional.ofNullable(context.getElement().get().getAnnotation(ReportConfiguration.class));

            if (reportConfiguration.isPresent()) {
                for (Status status : reportConfiguration.get().generateExtraReportsBy()) {
                    String reportPath = reportConfiguration.get().reportPath() + "/" + status.toString() + ".html";
                    this.sparkReporter = new ExtentSparkReporter(reportPath);
                    this.extentReports.attachReporter(this.sparkReporter.filter().statusFilter().as(new Status[]{status}).apply());
                }
            }

            this.extentReports.flush();

            String className = context.getRequiredTestClass().getSimpleName();
            if (passTests.size() > 0) {
                String path = "pass".concat(className).concat(".json");
                JsonReadAndWriteExtensions pass = new JsonReadAndWriteExtensions(path,1);
                pass.readAndWrite(passTests, PassTestInfo.class);
            }

            if (failTests.size() > 0) {
                String path = "fail".concat(className).concat(".json");
                JsonReadAndWriteExtensions fails = new JsonReadAndWriteExtensions(path,2);
                fails.readAndWrite(failTests, FailTestInfo.class);
            }
        }
    }
    @Before(value = "execution(* *(..)) && @annotation(org.springframework.context.annotation.Description)")
    public void before(JoinPoint joinPoint) {
        String step = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(Description.class).value();
        String methodName = joinPoint.getSignature().getName();
        String classParams = deepToString(((MethodSignature) joinPoint.getSignature()).getParameterNames());
        List<Object> methodParams = new ArrayList<>(Arrays.asList(joinPoint.getArgs()));
        extentTest.info("1.STEP METHODS : " + methodName + "  |  STEP DESC : " + step + "");
        extentTest.info("2. STEP DATA : " + classParams + "");
        extentTest.info("3. STEP PARAMS : " + methodParams + "");
    }
}
