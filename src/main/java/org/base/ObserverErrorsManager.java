package org.base;

import com.aventstack.extentreports.Status;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.extensions.report.ExtentReportExtension;
import org.extensions.report.ExtentTestManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ObserverErrorsManager {

    private static ObserverErrorsManager observerErrorsManagerInstance;

    public static ObserverErrorsManager getInstance() {
        if (observerErrorsManagerInstance == null) {
            observerErrorsManagerInstance = new ObserverErrorsManager();
        }
        return observerErrorsManagerInstance;
    }

    private ObserverErrorsManager() {}

    private static List<Object> observableCollector = new ArrayList<>();

    public List<Object> getCollector() { return observableCollector; }

    public void reset() {
        ObserverErrorsManager.observableCollector = new ArrayList<>();
    }

    public <T, ERROR extends Exception> void subscribeWithObserver(Observable<T> observable, Class<ERROR> errorClass) {
        observable.observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .blockingSubscribe(new Observer<>() {
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        log.info("onSubscribe step finished");
                        this.disposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull T any) {
                        log.info("onNext step finished with: " + any);
                        observableCollector.add(any);
                        if (errorClass != null) {
                            throw new RuntimeException(any.toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        log.info("onError step finished with: " + throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        log.info("onComplete step finished");
                        disposable.dispose();
                    }
        });

    }
}
