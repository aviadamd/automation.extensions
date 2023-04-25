package org.component.jackson.misc;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({  "url" })
public class AutomationNavigationAction {
    private String url;

    public AutomationNavigationAction() {}

    public AutomationNavigationAction(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "AutomationNavigationAction{" +
                "url='" + url + '\'' +
                '}';
    }
}
