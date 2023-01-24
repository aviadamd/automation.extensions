package org.mobile.driver;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.events.WebDriverListener;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
public class DriverEventListener implements WebDriverListener {

    @Override
    public void beforeGet(WebDriver driver, String url) {

    }

    @Override
    public void afterGet(WebDriver driver, String url) {

    }
    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        WebDriverListener.super.beforeFindElement(driver, locator);
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        WebDriverListener.super.afterFindElement(driver, locator, result);
    }

    @Override
    public void beforeFindElements(WebDriver driver, By locator) {
        WebDriverListener.super.beforeFindElements(driver, locator);
    }

    @Override
    public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {
        WebDriverListener.super.afterFindElements(driver, locator, result);
    }

    @Override
    public void beforeGetPageSource(WebDriver driver) {
        WebDriverListener.super.beforeGetPageSource(driver);
    }

    @Override
    public void afterGetPageSource(WebDriver driver, String result) {
        WebDriverListener.super.afterGetPageSource(driver, result);
    }

    @Override
    public void beforeClose(WebDriver driver) {
        WebDriverListener.super.beforeClose(driver);
    }

    @Override
    public void afterClose(WebDriver driver) {
        WebDriverListener.super.afterClose(driver);
    }

    @Override
    public void beforeQuit(WebDriver driver) {
        WebDriverListener.super.beforeQuit(driver);
    }

    @Override
    public void afterQuit(WebDriver driver) {
        WebDriverListener.super.afterQuit(driver);
    }

    @Override
    public void beforeExecuteScript(WebDriver driver, String script, Object[] args) {
        WebDriverListener.super.beforeExecuteScript(driver, script, args);
    }

    @Override
    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {
        WebDriverListener.super.afterExecuteScript(driver, script, args, result);
    }

    @Override
    public void beforeExecuteAsyncScript(WebDriver driver, String script, Object[] args) {
        WebDriverListener.super.beforeExecuteAsyncScript(driver, script, args);
    }

    @Override
    public void afterExecuteAsyncScript(WebDriver driver, String script, Object[] args, Object result) {
        WebDriverListener.super.afterExecuteAsyncScript(driver, script, args, result);
    }

    @Override
    public void beforePerform(WebDriver driver, Collection<Sequence> actions) {
        WebDriverListener.super.beforePerform(driver, actions);
    }

    @Override
    public void afterPerform(WebDriver driver, Collection<Sequence> actions) {
        WebDriverListener.super.afterPerform(driver, actions);
    }

    @Override
    public void beforeResetInputState(WebDriver driver) {
        WebDriverListener.super.beforeResetInputState(driver);
    }

    @Override
    public void afterResetInputState(WebDriver driver) {
        WebDriverListener.super.afterResetInputState(driver);
    }

    @Override
    public void beforeClick(WebElement element) {
        WebDriverListener.super.beforeClick(element);
    }

    @Override
    public void afterClick(WebElement element) {
        WebDriverListener.super.afterClick(element);
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        WebDriverListener.super.beforeSendKeys(element, keysToSend);
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        WebDriverListener.super.afterSendKeys(element, keysToSend);
    }

    @Override
    public void beforeClear(WebElement element) {
        WebDriverListener.super.beforeClear(element);
    }

    @Override
    public void afterClear(WebElement element) {
        WebDriverListener.super.afterClear(element);
    }

    @Override
    public void beforeGetAttribute(WebElement element, String name) {
        WebDriverListener.super.beforeGetAttribute(element, name);
    }

    @Override
    public void afterGetAttribute(WebElement element, String name, String result) {
        WebDriverListener.super.afterGetAttribute(element, name, result);
    }

    @Override
    public void beforeIsSelected(WebElement element) {
        WebDriverListener.super.beforeIsSelected(element);
    }

    @Override
    public void afterIsSelected(WebElement element, boolean result) {
        WebDriverListener.super.afterIsSelected(element, result);
    }

    @Override
    public void beforeIsEnabled(WebElement element) {
        WebDriverListener.super.beforeIsEnabled(element);
    }

    @Override
    public void afterIsEnabled(WebElement element, boolean result) {
        WebDriverListener.super.afterIsEnabled(element, result);
    }

    @Override
    public void beforeGetText(WebElement element) {
        WebDriverListener.super.beforeGetText(element);
    }

    @Override
    public void afterGetText(WebElement element, String result) {
        WebDriverListener.super.afterGetText(element, result);
    }

    @Override
    public void beforeFindElement(WebElement element, By locator) {
        WebDriverListener.super.beforeFindElement(element, locator);
    }

    @Override
    public void afterFindElement(WebElement element, By locator, WebElement result) {
        WebDriverListener.super.afterFindElement(element, locator, result);
    }

    @Override
    public void beforeFindElements(WebElement element, By locator) {
        WebDriverListener.super.beforeFindElements(element, locator);
    }

    @Override
    public void afterFindElements(WebElement element, By locator, List<WebElement> result) {
        WebDriverListener.super.afterFindElements(element, locator, result);
    }

    @Override
    public void beforeIsDisplayed(WebElement element) {
        WebDriverListener.super.beforeIsDisplayed(element);
    }

    @Override
    public void afterIsDisplayed(WebElement element, boolean result) {
        WebDriverListener.super.afterIsDisplayed(element, result);
    }

    @Override
    public void beforeGetLocation(WebElement element) {
        WebDriverListener.super.beforeGetLocation(element);
    }

    @Override
    public void afterGetLocation(WebElement element, Point result) {
        WebDriverListener.super.afterGetLocation(element, result);
    }

    @Override
    public void beforeGetSize(WebElement element) {
        WebDriverListener.super.beforeGetSize(element);
    }

    @Override
    public void beforeBack(WebDriver.Navigation navigation) {

    }

    @Override
    public void afterBack(WebDriver.Navigation navigation) {

    }

    @Override
    public void beforeForward(WebDriver.Navigation navigation) {

    }

    @Override
    public void afterForward(WebDriver.Navigation navigation) {

    }

    @Override
    public void beforeAnyAlertCall(Alert alert, Method method, Object[] args) {

    }

    @Override
    public void afterAnyAlertCall(Alert alert, Method method, Object[] args, Object result) {

    }

    @Override
    public void beforeAccept(Alert alert) {

    }

    @Override
    public void afterAccept(Alert alert) {

    }

    @Override
    public void beforeDismiss(Alert alert) {

    }

    @Override
    public void afterDismiss(Alert alert) {

    }

    @Override
    public void beforeGetText(Alert alert) {

    }

    @Override
    public void afterGetText(Alert alert, String result) {

    }

    @Override
    public void beforeSendKeys(Alert alert, String text) {

    }

    @Override
    public void afterSendKeys(Alert alert, String text) {

    }

    @Override
    public void beforeAddCookie(WebDriver.Options options, Cookie cookie) {

    }

    @Override
    public void afterAddCookie(WebDriver.Options options, Cookie cookie) {

    }

    @Override
    public void beforeDeleteCookieNamed(WebDriver.Options options, String name) {

    }

    @Override
    public void afterDeleteCookieNamed(WebDriver.Options options, String name) {

    }

    @Override
    public void beforeDeleteCookie(WebDriver.Options options, Cookie cookie) {

    }

    @Override
    public void afterDeleteCookie(WebDriver.Options options, Cookie cookie) {
        WebDriverListener.super.afterDeleteCookie(options, cookie);
    }

    @Override
    public void beforeDeleteAllCookies(WebDriver.Options options) {
        WebDriverListener.super.beforeDeleteAllCookies(options);
    }

    @Override
    public void afterDeleteAllCookies(WebDriver.Options options) {
        WebDriverListener.super.afterDeleteAllCookies(options);
    }

    @Override
    public void beforeGetCookies(WebDriver.Options options) {
        WebDriverListener.super.beforeGetCookies(options);
    }

    @Override
    public void afterGetCookies(WebDriver.Options options, Set<Cookie> result) {
        WebDriverListener.super.afterGetCookies(options, result);
    }

    @Override
    public void beforeGetCookieNamed(WebDriver.Options options, String name) {
        WebDriverListener.super.beforeGetCookieNamed(options, name);
    }

    @Override
    public void afterGetCookieNamed(WebDriver.Options options, String name, Cookie result) {
        WebDriverListener.super.afterGetCookieNamed(options, name, result);
    }
}