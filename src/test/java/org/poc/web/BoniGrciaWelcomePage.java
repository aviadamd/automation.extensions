package org.poc.web;

import org.automation.DurationOf;
import org.extensions.anontations.TimeOut;
import org.automation.elements.ObjectFactoryGenerator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

@TimeOut(durationOf = DurationOf.MINUTES, duration = 10)
public class BoniGrciaWelcomePage extends ObjectFactoryGenerator {

    @FindBy(how = How.XPATH, using = "//*[@id=\"divMenuRight\"]/div/div/ul/li[1]/a")
    public WebElement homeTab;

    @FindBy(how = How.XPATH, using = "//*[@id=\"divMenuRight\"]/div/div/ul/li[2]/a")
    public WebElement resumeTab;

    @FindBy(how = How.XPATH, using = "<a href=\"teaching.html\">Teaching</a>")
    public WebElement homeTeaching;

    public BoniGrciaWelcomePage(WebDriver driver) {
        this.instantiateWebPage(driver, this);
    }

}
