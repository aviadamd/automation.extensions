package org.extensions.web;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.Arrays;

public class WebDriverOptions {

    public ChromeOptions chrome() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--dns-prefetch-disable");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--disable-web-security");
        chromeOptions.addArguments("--allow-running-insecure-content");
        chromeOptions.addArguments("enable-automation");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("--disable-web-security");
        chromeOptions.addArguments("--allow-insecure-localhost");
        chromeOptions.addArguments("--ignore-urlfetcher-cert-requests");
        chromeOptions.addArguments(Arrays.asList("--no-sandbox","--ignore-certificate-errors","--homepage=about:blank","--no-first-run"));
        chromeOptions.addArguments("disable-infobars");
        return chromeOptions;
    }
    public FirefoxOptions firefox() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addPreference("network.automatic-ntlm-auth.trusted-uris", "http://,https://");
        firefoxOptions.addPreference("network.automatic-ntlm-auth.allow-non-fqdn", true);
        firefoxOptions.addPreference("network.negotiate-auth.delegation-uris", "http://,https://");
        firefoxOptions.addPreference("network.negotiate-auth.trusted-uris", "http://,https://");
        firefoxOptions.addPreference("network.http.phishy-userpass-length", 255);
        firefoxOptions.addPreference("security.csp.enable", false);
        firefoxOptions.addArguments("--disable-web-security");
        firefoxOptions.addArguments("--allow-insecure-localhost");
        firefoxOptions.addArguments("--ignore-urlfetcher-cert-requests");
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setAcceptUntrustedCertificates(true);
        return firefoxOptions;
    }
}
