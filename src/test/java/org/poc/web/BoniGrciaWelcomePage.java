package org.poc.web;

import org.base.elements.ObjectFactoryGenerator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class BoniGrciaWelcomePage extends ObjectFactoryGenerator {
    @FindBy(how = How.XPATH, using = "//*[@id=\"divMenuRight\"]/div/div/ul/li[1]/a")
    public WebElement homeTab;

    @FindBy(how = How.XPATH, using = "//*[@id=\"divMenuRight\"]/div/div/ul/li[2]/a")
    public WebElement resumeTab;

    @FindBy(how = How.CSS, using = ".social_bookmarks > a:nth-child(1)")
    public WebElement gitHubLink;

    public BoniGrciaWelcomePage(WebDriver driver) {
        this.instantiateWebPage(driver, this);
    }

}
