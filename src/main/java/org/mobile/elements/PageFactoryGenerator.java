package org.mobile.elements;

import org.mobile.PageInitiationFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class PageFactoryGenerator implements PageInitiationFactory {
    @Override
    public <TPage extends PageFactoryGenerator> TPage init(WebDriver driver, Class<TPage> pageClass) {
        return PageFactory.initElements(driver, pageClass);
    }
}
