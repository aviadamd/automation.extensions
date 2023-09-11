package org.base.mobile.gestures;

import io.appium.java_client.AppiumBy;
import org.assertj.core.api.Assertions;
import org.base.mobile.MobileDriverProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedCondition;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;

public class MobileSwipeExtensions {
    private final MobileDriverProvider mobileDriverProvider;

    public MobileSwipeExtensions(MobileDriverProvider mobileDriverProvider) {
        this.mobileDriverProvider = mobileDriverProvider;
    }

    /**
     * swipe
     * @param scrollDirection
     */
    public void swipe(ScrollDirection scrollDirection) {
        Dimension dimension = this.mobileDriverProvider.getMobileDriver().manage().window().getSize();
        Point pointOptionStart = new Point(dimension.width / 2, dimension.height /2), pointOptionEnd = null;

        switch (scrollDirection) {
            case UP -> pointOptionEnd = new Point(dimension.width / 2, dimension.height / 2 + dimension.height / 2 / 2);
            case UP_LARGE -> pointOptionEnd = new Point(dimension.width / 2, dimension.height - 10);
            case DOWN -> pointOptionEnd = new Point(dimension.width / 2, 0);
            case DOWN_LARGE -> pointOptionEnd = new Point(dimension.width / 2, 10);
            case LEFT -> pointOptionEnd = new Point(10, dimension.height -2);
            case RIGHT -> pointOptionEnd = new Point(dimension.width - 10, dimension.height / 2);
        }

        Assertions.assertThat(pointOptionEnd != null).isTrue();
        this.swipe(pointOptionStart, pointOptionEnd);
    }

    /**
     * swipe
     * @param start
     * @param end
     */
    public void swipe(Point start, Point end) {
        try {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "first-finger");

            Sequence sequence = new Sequence(finger,1)
                    .addAction(finger.createPointerMove(Duration.ofMillis(10), PointerInput.Origin.viewport(), start.x, start.y))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(600)))
                    .addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), end.x, end.y));

            this.mobileDriverProvider.getAppiumDriver().perform(Collections.singletonList(sequence));
        } catch (Exception exception) {
            Assertions.fail("swipe fails " + exception.getMessage(), exception);
        }
    }

    /**
     * swipeByText
     * @param text
     */
    public void swipeByText(String text) {
        try {
            if (this.mobileDriverProvider.isAndroid()) {
                String mySelector = "new UiSelector().text(\"" + text + "\").instance(0)";
                String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(" + mySelector + ");";
                this.mobileDriverProvider.getAndroidDriver().findElement(AppiumBy.androidUIAutomator(command));
            } else {
                final HashMap<String, String> scrollObject = new HashMap<>();
                scrollObject.put("predicateString", "value == '" + text + "'");
                scrollObject.put("toVisible", "true");
                this.mobileDriverProvider.getIosDriver().executeScript("mobile: scroll", scrollObject);
            }
        } catch (Exception exception) {
            Assertions.fail("swipe fails " + exception.getMessage(), exception);
        }
    }

    /**
     * swipeToWebElement
     * @param scrollDirection
     * @param attempts
     * @param expectedCondition
     */
    public void swipeToWebElement(ScrollDirection scrollDirection, int attempts, ExpectedCondition<WebElement> expectedCondition) {
        boolean find = false;

        for (int i = 1; i < attempts; i++) {
            try {

                WebElement element = this.mobileDriverProvider
                        .getWebDriverWait()
                        .getWebDriverWait()
                        .ignoreAll(this.mobileDriverProvider.sessionExceptions())
                        .until(expectedCondition);

                if (element != null) {
                    find = true;
                    break;
                } else this.swipe(scrollDirection);

            } catch (Exception ignore) {}
        }

        Assertions.assertThat(find).isTrue();
    }

    /**
     * swipeToByElement
     * @param scrollDirection
     * @param attempts
     * @param expectedCondition
     */
    public void swipeToByElement(ScrollDirection scrollDirection, int attempts, ExpectedCondition<By> expectedCondition) {
        boolean find = false;

        for (int i = 1; i < attempts; i++) {
            try {

                By by = this.mobileDriverProvider
                        .getWebDriverWait()
                        .getWebDriverWait()
                        .ignoreAll(this.mobileDriverProvider.sessionExceptions())
                        .until(expectedCondition);

                if (by != null) {
                    find = true;
                    break;
                } else this.swipe(scrollDirection);

            } catch (Exception ignore) {}
        }

        Assertions.assertThat(find).isTrue();
    }

}
