package com.chorifa.minioc.aop.interceptor;

import com.chorifa.minioc.aop.Advice;
import com.chorifa.minioc.aop.MethodInvocation;

public class AfterReturnMethodInterceptor extends MethodInterceptor {

    public AfterReturnMethodInterceptor(Advice advice, String pattern) {
        super(advice, pattern);
        this.priority = 2;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object o = invocation.flow();
        advice.afterReturning(o,invocation.getMethod(),invocation.getArgs(),invocation.getTarget());
        return o;
    }

}
