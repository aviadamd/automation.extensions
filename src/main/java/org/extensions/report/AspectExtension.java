package org.extensions.report;

import com.aventstack.extentreports.Status;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.Arrays.deepToString;

@Aspect
@Component
public class AspectExtension {

    @After(value = "execution(* *(..)) && @annotation(org.springframework.context.annotation.Description)")
    public synchronized void before(JoinPoint joinPoint) {
        if(ExtentTestManager.getReportsInstance != null) {
            String methodName = joinPoint.getSignature().getName();
            ExtentTestManager.log(Status.INFO,"1.STEP METHOD : " + methodName + " IS EXECUTED");
        }
    }

    @After(value = "execution(* *(..)) && @annotation(org.springframework.context.annotation.Description)")
    public synchronized void after(JoinPoint joinPoint) {
        if(ExtentTestManager.getReportsInstance != null) {
            String methodName = joinPoint.getSignature().getName();
            String classParams = deepToString(((MethodSignature) joinPoint.getSignature()).getParameterNames());
            List<Object> methodParams = new ArrayList<>(Arrays.asList(joinPoint.getArgs()));
            ExtentTestManager.log(Status.INFO,"1.STEP NAME : " + methodName);
            ExtentTestManager.log(Status.INFO,"2.STEP PARAMS NAMES : " + classParams);
            ExtentTestManager.log(Status.INFO,"3.STEP PARAMS VALUES : " + methodParams);
        }
    }

    @AfterThrowing(value = "execution(* *(..)) && @annotation(org.springframework.context.annotation.Description)")
    public synchronized void fail(JoinPoint joinPoint) {
        if(ExtentTestManager.getReportsInstance != null) {
            String methodName = joinPoint.getSignature().getName();
            ExtentTestManager.log(Status.INFO,"1.STEP NAME : " + methodName + " FAILS");
        }
    }

}
