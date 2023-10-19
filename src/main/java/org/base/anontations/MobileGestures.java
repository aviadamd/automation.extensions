package org.base.anontations;

import org.apache.commons.lang3.tuple.Pair;
import org.base.mobile.data.ElementsAttributes;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public interface MobileGestures {

    void get(String url);
    void close();
    void quit();
    String getAttribute(WebElement element,
                        Pair<ElementsAttributes.AndroidElementsAttributes,
                                ElementsAttributes.IosElementsAttributes> attributesPair);
}
