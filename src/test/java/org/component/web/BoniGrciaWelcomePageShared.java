package org.component.web;

import org.assertj.core.api.StringAssert;
import org.base.web.ScrollDirection;
import org.base.web.WebConfiguration;
import org.extensions.web.WebSharedObjects;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class BoniGrciaWelcomePageShared {
    private final BoniGrciaWelcomePage boniGrciaWelcomePage;
    private final WebSharedObjects webSharedObjects;

    public BoniGrciaWelcomePageShared(WebSharedObjects webSharedObjects) {
        this.webSharedObjects = webSharedObjects;
        this.boniGrciaWelcomePage = new BoniGrciaWelcomePage(webSharedObjects.getDriverManager().getDriver());
    }

    public BoniGrciaWelcomePageShared openPage(WebConfiguration webConfiguration) {
        this.webSharedObjects
                .getDriverManager()
                .get(webConfiguration.projectUrl());
        return this;
    }

    public BoniGrciaWelcomePageShared resumeTab() {
        this.webSharedObjects
                .getDriverManager()
                .click(boniGrciaWelcomePage.resumeTab);
        return this;
    }

    public BoniGrciaWelcomePageShared homeTab() {
        this.webSharedObjects
                .getDriverManager()
                .click(boniGrciaWelcomePage.homeTab);
        return this;
    }

    public BoniGrciaWelcomePageShared gitHubLink() {
        this.webSharedObjects
                .getDriverManager()
                .getScrollExtension()
                .scrollToElement(2, ScrollDirection.DOWN, elementToBeClickable(boniGrciaWelcomePage.gitHubLink))
                .click(boniGrciaWelcomePage.gitHubLink);
        return this;
    }

    public BoniGrciaWelcomePageShared linkedinLink() {
        this.webSharedObjects
                .getDriverManager()
                .getScrollExtension()
                .scrollToElement(2, ScrollDirection.DOWN, elementToBeClickable(boniGrciaWelcomePage.gitHubLink))
                .click(boniGrciaWelcomePage.linkedinLink);
        return this;
    }

    public void assertHomeTab(String expected) {
        String homeTab = this.webSharedObjects.getDriverManager().getText(boniGrciaWelcomePage.homeTab);
        this.webSharedObjects.getAssertionsManager()
                .proxy(StringAssert.class, String.class, homeTab)
                .isEqualTo(expected);
    }
}
