package org.utils.assertions;

import org.assertj.core.api.Condition;
import java.util.function.Predicate;

public class AssertCondition<E> {
    private final Condition<E> condition;

    public AssertCondition(Predicate<E> condition, String description) {
        this.condition = new Condition<>(condition, description);
    }

    public Condition<E> getCondition() {
        return condition;
    }
}
