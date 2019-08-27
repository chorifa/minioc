package com.chorifa.minioc.aop.interceptor;

import com.chorifa.minioc.aop.Advice;
import com.chorifa.minioc.aop.MethodInvocation;

public class AroundMethodInterceptor extends MethodInterceptor {

    public AroundMethodInterceptor(Advice advice, String pattern) {
        super(advice, pattern);
        this.priority = 3;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return advice.around(invocation,invocation.getMethod(),invocation.getArgs(),invocation.getTarget());
    }

}
