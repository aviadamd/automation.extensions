package org.utils;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.extern.slf4j.Slf4j;
import org.extensions.report.TestData;

/**
 * RxJavaBus
 * this class does a similar job as the actual computer bus does.
 */
@Slf4j
public final class TestDataObserverBus {

    private static final BehaviorSubject<TestData<?>> testDataBehaviorSubject = BehaviorSubject.create();

    /**
     * subscribeToObservable
     * Subscribes to the current Observable and provides a callback to handle the items it emits.
     * @param observer an observer is ready to consume items
     */
    public static void subscribe(@NonNull Observer<TestData<?>> observer) {
        testDataBehaviorSubject.subscribe(observer);
    }

    /**
     * subscribeToObservable
     * Subscribes to the current Observable and provides a callback to handle the items it emits.
     * @return Disposable
     */
    public static @NonNull Disposable subscribe(
            @NonNull Consumer<? super TestData<?>> onNext,
            @NonNull Consumer<? super Throwable> onError) {
        return testDataBehaviorSubject.subscribe(onNext, onError);
    }

    /**
     * publishFromObserver
     * use this method to send data
     * @param testData any
     */
    public static void onNext(@NonNull TestData<?> testData) {
        log.debug("publish " + testData.toString() + " data");
        testDataBehaviorSubject.onNext(testData);
    }
}
