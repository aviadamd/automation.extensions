package org.component.web;

import org.assertj.core.api.StringAssert;
import org.base.web.ScrollDirection;
import org.base.web.SeleniumWebDriverProvider;
import org.base.web.WebConfiguration;
import org.utils.assertions.AssertionsLevel;
import org.utils.assertions.AssertJHandler;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class BoniGrciaWelcomePageShared {
    private final BoniGrciaWelcomePage boniGrciaWelcomePage;
    private final SeleniumWebDriverProvider seleniumWebDriverProvider;
    private final AssertJHandler assertJHandler;

    public BoniGrciaWelcomePageShared(SeleniumWebDriverProvider seleniumWebDriverProvider) {
        this.seleniumWebDriverProvider = seleniumWebDriverProvider;
        this.boniGrciaWelcomePage = new BoniGrciaWelcomePage(seleniumWebDriverProvider.getDriver());
        this.assertJHandler = new AssertJHandler();
    }

    public BoniGrciaWelcomePageShared setAssertionLevel(AssertionsLevel level) {
        this.assertJHandler.setAssertionLevel(level);
        return this;
    }
    public BoniGrciaWelcomePageShared openPage(WebConfiguration webConfiguration) {
        this.seleniumWebDriverProvider.get(webConfiguration.projectUrl());
        return this;
    }

    public BoniGrciaWelcomePageShared resumeTab() {
        this.seleniumWebDriverProvider.click(boniGrciaWelcomePage.resumeTab);
        return this;
    }

    public BoniGrciaWelcomePageShared homeTab() {
        this.seleniumWebDriverProvider.click(boniGrciaWelcomePage.homeTab);
        return this;
    }

    public BoniGrciaWelcomePageShared gitHubLink() {
        this.seleniumWebDriverProvider
                .getScrollExtension()
                .scrollToElement(2, ScrollDirection.DOWN, elementToBeClickable(boniGrciaWelcomePage.gitHubLink))
                .click(boniGrciaWelcomePage.gitHubLink);
        return this;
    }

    public BoniGrciaWelcomePageShared linkedinLink() {
        this.seleniumWebDriverProvider
                .getScrollExtension()
                .scrollToElement(2, ScrollDirection.DOWN, elementToBeClickable(boniGrciaWelcomePage.gitHubLink))
                .click(boniGrciaWelcomePage.linkedinLink);
        return this;
    }

    public StringAssert assertHomeTab() {
        return null;
                //this.assertionsManager.assertElementText(elementToBeClickable(boniGrciaWelcomePage.homeTab));
    }
}
