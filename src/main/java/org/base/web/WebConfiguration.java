package org.base.web;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.FIRST)
@Config.Sources({"classpath:selenium.properties"})
public interface WebConfiguration extends Config {
    @Config.Key("project.client")
    String projectClient();
    @Config.Key("project.url")
    String projectUrl();
    @Config.Key("project.db.url")
    String mongoConnection();

    void setProperty(String key, String value);
    String getProperty(String key);

}
