package org.poc.web;

import org.base.elements.ObjectFactoryGenerator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class WallaWelcomePage extends ObjectFactoryGenerator {

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/div[2]/header/div[1]/div[1]/div[1]/h1/a/img")
    public WebElement homeTab;

    @FindBy(how = How.XPATH, using = "//*[@id=\"root\"]/div/div[2]/header/div[2]/div[1]/ul/li[1]/a")
    public WebElement news;

    @FindBy(how = How.CSS, using = "//*[@id=\"travelSection\"]/div[1]/header/div/a")
    public WebElement travelSection;

    public WallaWelcomePage(WebDriver driver) {
        this.instantiateWebPage(driver, this);
    }
}
