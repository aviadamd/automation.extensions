package org.utils.rest.okHttp;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import java.util.Arrays;

public class MethodInterceptorImpl implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation i) throws Throwable {
        System.out.println("method " + i.getMethod() + " is called on " + i.getThis() + " with args "+ Arrays.toString(i.getArguments()));
        Object ret= i.proceed();
        System.out.println("method " + i.getMethod()+ " returns " + ret);
        return ret;
    }
}
