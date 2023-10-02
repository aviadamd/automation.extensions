package org.base;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.extern.slf4j.Slf4j;

/**
 * RxJavaBus
 * this class does a similar job as the actual computer bus does.
 */
@Slf4j
public final class RxJavaBus {

    /**
     * this how to create our bus instance
     */
    private static final BehaviorSubject<Object> behaviorSubject = BehaviorSubject.create();


    public static BehaviorSubject<Object> getSubject() {
        return behaviorSubject;
    }

    /**
     * publish
     * use this method to send data
     * @param object any
     */
    public static void publish(@NonNull Object object) {
        log.info("publish " + object + " data");
        behaviorSubject.onNext(object);
    }

    /**
     * subscribe
     * set the logic on the action
     * @param action
     * @return Disposable
     */
    public static Disposable subscribe(@NonNull Consumer<? super Object> action) {
        return behaviorSubject.subscribe(action);
    }
}
