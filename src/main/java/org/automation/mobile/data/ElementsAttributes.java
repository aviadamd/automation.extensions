package org.automation.mobile.data;


public class ElementsAttributes {

    public enum SharedElementAttributes {
        ENABLED("enabled"),
        SELECTED("selected"),
        VISIBLE("visible");

        private final String setTag;
        SharedElementAttributes(String getTag) { this.setTag = getTag; }
        public String getTag() { return setTag; }
    }

    public enum SharedElementTextAttr {
        TEXT("text"),
        CONTENT_DESC("content-desc"),
        LABEL("label"),
        VALUE("value"),
        NAME("name");

        private final String setTag;
        SharedElementTextAttr(String getTag) { this.setTag = getTag; }
        public String getTag() { return setTag; }
    }

    public enum AndroidElementsAttributes {
        CHECKABLE("checkable"),
        CHECKED("checked"),
        CLICKABLE("clickable"),
        CONTENT_DESC("content-desc"),
        ENABLED("enabled"),
        FOCUSABLE("focusable"),
        FOCUSED("focused"),
        LONG_CLICKABLE("long-clickable"),
        SELECTED("selected"),
        RESOURCE_ID("resource-id"),
        SCROLLABLE("scrollable"),
        TEXT("text"),
        CLASS("class");

        private final String setTag;
        AndroidElementsAttributes(String getTag) { this.setTag = getTag; }
        public String getTag() { return setTag; }
    }

    public enum IosElementsAttributes {
        ENABLED("enabled"),
        LABEL("label"),
        NAME("name"),
        SELECTED("selected"),
        VALUE("value"),
        VISIBLE("visible"),
        CLASS("class");

        private final String setTag;
        IosElementsAttributes(String getTag) { this.setTag = getTag; }
        public String getTag() { return setTag; }
    }
}
