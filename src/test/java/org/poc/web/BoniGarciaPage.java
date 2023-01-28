package org.poc.web;

import org.mobile.elements.PageFactoryGenerator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
public class BoniGarciaPage extends PageFactoryGenerator {
    @FindBy(how = How.XPATH, using = "/html/body/main/div/div[3]/div/p/a[1]")
    public WebElement link;

}
