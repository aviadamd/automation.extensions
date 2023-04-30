package org.component.web;

import org.assertj.core.api.StringAssert;
import org.base.web.ScrollDirection;
import org.base.web.SeleniumWebDriverProvider;
import org.base.web.WebConfiguration;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.utils.assertions.AssertionsLevel;
import org.utils.assertions.AssertionsManager;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class BoniGrciaWelcomePageShared {
    private final BoniGrciaWelcomePage boniGrciaWelcomePage;
    private final SeleniumWebDriverProvider seleniumWebDriverProvider;
    private final AssertionsManager assertionsManager;

    public BoniGrciaWelcomePageShared(SeleniumWebDriverProvider seleniumWebDriverProvider) {
        this.seleniumWebDriverProvider = seleniumWebDriverProvider;
        this.boniGrciaWelcomePage = new BoniGrciaWelcomePage(seleniumWebDriverProvider.getDriver());
        this.assertionsManager = new AssertionsManager();
        this.assertionsManager.setWebElementAssertion(seleniumWebDriverProvider);
    }

    public BoniGrciaWelcomePageShared setAssertionLevel(AssertionsLevel level) {
        this.assertionsManager.setAssertionLevel(level);
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
        return this.assertionsManager.assertElementText(elementToBeClickable(boniGrciaWelcomePage.homeTab));
    }
}
