package org.utils;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.extern.slf4j.Slf4j;
import org.extensions.report.TestData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TestDataObserverBus
 * this class does a similar job as the actual computer bus does.
 */
@Slf4j
public final class TestDataObserverBus {
    private static List<TestData<?>> testDataList = Collections.synchronizedList(new ArrayList<>());
    private static BehaviorSubject<List<TestData<?>>> testDataBehaviorSubject = BehaviorSubject.create();

    /**
     * publishFromObserver
     * use this method to send data
     * @param testData any
     */
    public static void onNext(@NonNull TestData<?> testData) {
        log.info("publish " + testData.toString() + " data");
        testDataList.add(testData);
        testDataBehaviorSubject.onNext(testDataList);
    }

    /**
     * subscribeToObservable
     * Subscribes to the current Observable and provides a callback to handle the items it emits.
     * @return Disposable
     */
    public static @NonNull Disposable subscribe(@NonNull Consumer<? super List<TestData<?>>> onNext) {
        return testDataBehaviorSubject.subscribe(onNext);
    }

    public static void reset() {
        testDataList = new ArrayList<>();
        testDataBehaviorSubject = BehaviorSubject.create();
    }

}
