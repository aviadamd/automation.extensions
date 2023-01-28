package org.mobile;

import org.mobile.elements.PageFactoryGenerator;
import org.openqa.selenium.WebDriver;

public interface PageInitiationFactory {
    <TPage extends PageFactoryGenerator> TPage init(WebDriver driver, Class<TPage> pageClass);
}
