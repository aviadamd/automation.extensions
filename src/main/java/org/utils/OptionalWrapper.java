package org.utils;

import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class OptionalWrapper<T> {
    private Optional<T> optional;

    public OptionalWrapper(Optional<T> optional) {
        this.optional = optional;
    }

    public OptionalWrapper<T> ifPresent(Consumer<T> consumer) {
        if (consumer != null) {
            this.optional.ifPresent(consumer);
        } else this.optional.empty();
        return this;
    }

    public OptionalWrapper<T> ifNotPresent(Runnable runnable) {
        if (runnable != null && this.optional.isEmpty()) {
            runnable.run();
        } else this.optional.empty();
        return this;
    }

    public Optional<T> empty() {
        this.optional = Optional.empty();
        return this.optional.empty();
    }
}
