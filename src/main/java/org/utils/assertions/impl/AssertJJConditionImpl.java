package org.utils.assertions.impl;

import org.assertj.core.api.Condition;
import org.utils.assertions.AssertJCondition;

import java.util.function.Predicate;

public class AssertJJConditionImpl<A> implements AssertJCondition<A> {
    private final String descriptionText;
    private final Predicate<A> predicateCondition;

    public AssertJJConditionImpl(Predicate<A> predicateCondition, String descriptionText) {
        this.predicateCondition = predicateCondition;
        this.descriptionText = descriptionText;
    }

    @Override
    public Condition<A> condition() {
        return new Condition<>(this.predicateCondition, this.descriptionText);
    }

    @Override
    public String description() {
        return this.descriptionText;
    }

    @Override
    public String toString() {
        return "AssertJConditionsOptionsImpl{" +
                "descriptionText='" + descriptionText + '\'' +
                ", predicateCondition=" + predicateCondition +
                '}';
    }
}
