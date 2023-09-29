package org.utils.assertions;

import org.assertj.core.api.Condition;

public interface AssertJCondition<A> {
    Condition<A> condition();
    String description();
}
