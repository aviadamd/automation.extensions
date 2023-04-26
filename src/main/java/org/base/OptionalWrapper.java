package org.base;

import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class OptionalWrapper<T> {
    private final Optional<T> optional;

    public OptionalWrapper(Optional<T> optional) {
        this.optional = optional;
    }

    public OptionalWrapper<T> ifPresent(Consumer<T> consumer) {
        if (consumer != null) this.optional.ifPresent(consumer);
        return this;
    }

    public OptionalWrapper<T> ifNotPresent(Runnable runnable) {
        if (runnable != null && this.optional.isEmpty()) runnable.run();
        return this;
    }
}
