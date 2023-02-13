package org.automation.configuration;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.FIRST)
@Config.Sources({"classpath:report.properties"})
public interface ReportConfigurations extends Config {

    @Config.Key("project.report.path")
    String reportPath();
    @Config.Key("project.report.config")
    String reportConfiguration();

    @Config.Key("project.mongo.connection")
    String mongoConnection();

    void setProperty(String key, String value);

}
