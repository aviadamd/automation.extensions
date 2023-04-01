package org.base.mobile;

import org.apache.commons.lang3.tuple.Pair;
import org.base.mobile.data.ElementsAttributes;
import org.openqa.selenium.WebElement;

public interface MobileGestures {

    void get(String url);
    void close();
    void quit();
    String getAttribute(WebElement element, Pair<ElementsAttributes.AndroidElementsAttributes, ElementsAttributes.IosElementsAttributes> attributesPair);
}
