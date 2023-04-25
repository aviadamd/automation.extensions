package org.component.jackson.misc;

import lombok.extern.slf4j.Slf4j;
import org.base.web.SeleniumWebDriverManager;
import org.base.web.SeleniumWebDriverProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

@Slf4j
public class JsonReaderTest {

    @Test
    @DisplayName("readJsonTestA")
    public void readJsonTestA() throws IOException {
        String path = "C:\\Users\\Lenovo\\IdeaProjects\\com.automation.mobile\\src\\test\\resources\\webCommands.json";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readValue(new File(path), JsonNode.class);

        String id = jsonNode.findValue("id").textValue();
        String url = jsonNode.findValue("url").textValue();
        String element = jsonNode.findValue("element").textValue();

        SeleniumWebDriverManager webDriverManager = new SeleniumWebDriverManager();
        WebDriver driver = webDriverManager.setWebDriver("chrome", new DesiredCapabilities());
        SeleniumWebDriverProvider provider = new SeleniumWebDriverProvider(url, Duration.ofSeconds(5), driver);
        provider.click(ExpectedConditions.elementToBeClickable(By.xpath(element)));
    }
}
