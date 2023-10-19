package org.utils.assertions.impl;

import org.assertj.core.api.Condition;
import org.assertj.core.api.HamcrestCondition;
import org.hamcrest.Matcher;
import org.utils.assertions.AssertJCondition;

public class HamcrestJConditionImpl<A> implements AssertJCondition<A> {

    private final String descriptionText;
    private final Matcher<? extends A> matcher;

    public HamcrestJConditionImpl(Matcher<? extends A> matcher, String descriptionText) {
        this.matcher = matcher;
        this.descriptionText = descriptionText;
    }

    @Override
    public Condition<A> condition() {
        return new HamcrestCondition<>(this.matcher);
    }

    @Override
    public String description() {
        return this.descriptionText;
    }

    @Override
    public String toString() {
        return "HamcrestConditionsOptionsImpl{" +
                "descriptionText='" + descriptionText + '\'' +
                ", matcher=" + matcher +
                '}';
    }
}
