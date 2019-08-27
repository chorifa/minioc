package com.chorifa.minioc.aop.interceptor;

import com.chorifa.minioc.aop.Advice;
import com.chorifa.minioc.aop.MethodInvocation;

public class BeforeMethodInterceptor extends MethodInterceptor {

    public BeforeMethodInterceptor(Advice advice, String pattern) {
        super(advice, pattern);
        this.priority = 0;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        advice.before(invocation.getMethod(),invocation.getArgs(),invocation.getTarget());
        return invocation.flow();
    }

}
