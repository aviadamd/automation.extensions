package org.poc.web;

import org.automation.DurationOf;
import org.extensions.anontations.TimeOut;
import org.automation.elements.ObjectFactoryGenerator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

@TimeOut(durationOf = DurationOf.MINUTES, duration = 10)
public class BoniGarciaPage extends ObjectFactoryGenerator {
    @FindBy(how = How.XPATH, using = "/html/body/main/div/div[3]/div/p/a[1]")
    public WebElement link;

}
