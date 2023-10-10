package org.utils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class StepReader implements Runnable {

    @Override
    public void run() {
        try {
            Step step = getClass().getAnnotation(Step.class);
            if (step != null) {
                log.info(step.desc());
            }
        } catch (Exception ignore) {}
    }
}
