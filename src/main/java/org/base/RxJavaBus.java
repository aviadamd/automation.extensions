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
     * publishFromObserver
     * use this method to send data
     * @param object any
     */
    public static void publishFromObserver(@NonNull Object object) {
        log.info("publish " + object + " data");
        behaviorSubject.onNext(object);

    }

    /**
     * subscribeToObservable
     * Subscribes to the current Observable and provides a callback to handle the items it emits.
     * @param action an observer is ready to consume items
     * @return Disposable
     */
    public static Disposable subscribeToObservable(@NonNull Consumer<? super Object> action) {
        return behaviorSubject.subscribe(action);
    }
}
