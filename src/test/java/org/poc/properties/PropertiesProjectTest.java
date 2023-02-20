//package org.poc.properties;
//
//import lombok.extern.slf4j.Slf4j;
//import org.automation.configuration.PropertiesManager;
//import org.automation.configuration.ReportConfiguration;
//import org.automation.mobile.MobileConfiguration;
//import org.automation.web.WebConfiguration;
//import org.junit.jupiter.api.Test;
//
//@Slf4j
//public class PropertiesProjectTest {
//
//    @Test
//    public void testPropertiesReader() {
//        PropertiesManager propertiesManager = new PropertiesManager();
//
//        MobileConfiguration mobileConfiguration = propertiesManager.getOrCreate(MobileConfiguration.class);
//        log.info(mobileConfiguration.appiumExecutable());
//        log.info(mobileConfiguration.nodeJsExecutable());
//        log.info(mobileConfiguration.mobileSecondJson());
//        log.info(mobileConfiguration.mobileFirstJson());
//        log.info(mobileConfiguration.mobileClient());
//
//        WebConfiguration webConfiguration = propertiesManager.getOrCreate(WebConfiguration.class);
//        log.info(webConfiguration.projectUrl());
//        log.info(webConfiguration.projectClient());
//
//        ReportConfiguration reportConfiguration = propertiesManager.getOrCreate(ReportConfiguration.class);
//        log.info(reportConfiguration.reportPath());
//        log.info(reportConfiguration.reportConfiguration());
//        log.info(reportConfiguration.mongoConnection());
//    }
//}
