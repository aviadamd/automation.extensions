package org.extensions;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.Arrays.deepToString;

@Aspect
@Component
public class AspectExtension {

    @Before(value = "execution(* *(..)) && @annotation(org.springframework.context.annotation.Description)")
    public void before(JoinPoint joinPoint) {
        String step = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(Description.class).value();
        String methodName = joinPoint.getSignature().getName();
        String classParams = deepToString(((MethodSignature) joinPoint.getSignature()).getParameterNames());
        List<Object> methodParams = new ArrayList<>(Arrays.asList(joinPoint.getArgs()));
//        extentTest.info("1.STEP METHODS : " + methodName + "  |  STEP DESC : " + step + "");
//        extentTest.info("2. STEP DATA : " + classParams + "");
//        extentTest.info("3. STEP PARAMS : " + methodParams + "");
    }
}
