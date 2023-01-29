package org.automation.enums;

public enum GetValuesBy {
    HEBREW("search value by hebrew"),
    ENGLISH("search value by english"),
    HEBREW_AND_ENGLISH("search value by english or hebrew"),
    NUMBERS("search value by numbers"),
    DATE("search value by date"),
    IGNORE("ignore get value by english or hebrew");
    private final String desc;
    GetValuesBy(String desc) { this.desc = desc; }
    public String getDesc() { return desc; }
}
