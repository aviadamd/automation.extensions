package org.base;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ErrorsCollector {

    private static ErrorsCollector errorsCollectorInstance;

    public static ErrorsCollector getInstance() {
        if (errorsCollectorInstance == null) {
            errorsCollectorInstance = new ErrorsCollector();
        }
        return errorsCollectorInstance;
    }

    private ErrorsCollector() {}

    private static List<Object> observableCollector = Collections.synchronizedList(new ArrayList<>());

    public List<Object> getCollector() { return observableCollector; }

    public void setObservableCollector(List<Object> observableCollector) {
        ErrorsCollector.observableCollector = observableCollector;
    }

    public <T> void subscribeWithObserver(Observable<T> observable) {
        observable.blockingSubscribe(new Observer<>() {
            private Disposable disposable;

            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                this.disposable = disposable;
            }

            @Override
            public void onNext(@NonNull T any) {
                log.info("On Next Received in Observer details : " + any);
                observableCollector.add(any);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                log.info("On Error Received in Observer error : " + throwable);
            }

            @Override
            public void onComplete() {
                disposable.dispose();
            }
        });

    }
}
