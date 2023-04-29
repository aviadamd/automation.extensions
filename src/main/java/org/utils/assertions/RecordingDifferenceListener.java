package org.utils.assertions;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.jsonunit.core.listener.Difference;
import net.javacrumbs.jsonunit.core.listener.DifferenceContext;
import net.javacrumbs.jsonunit.core.listener.DifferenceListener;
import org.extensions.report.ExtentTestManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RecordingDifferenceListener implements DifferenceListener {
    private final List<Difference> differenceList = new ArrayList<>();

    @Override
    public void diff(Difference difference, DifferenceContext context) {
        differenceList.add(difference);
    }

    public List<Difference> getDifferenceList() {
        return differenceList;
    }


    /**
     * print extent report print
     * to ExtentTestManager report if instance is not null
     */
    public synchronized void print(Status status, String description) {
        try {
            ExtentTestManager.log(status, "assertion fails " + description);
        } catch (Exception ignore) {
            log.info("assertion fails");
        }
    }
}
