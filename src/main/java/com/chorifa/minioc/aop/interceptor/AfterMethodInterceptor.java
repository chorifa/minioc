package com.chorifa.minioc.aop.interceptor;

import com.chorifa.minioc.aop.Advice;
import com.chorifa.minioc.aop.MethodInvocation;

public class AfterMethodInterceptor extends MethodInterceptor {

    public AfterMethodInterceptor(Advice advice, String pattern) {
        super(advice, pattern);
        this.priority = 1;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.flow();
        }finally {
            advice.after(invocation.getMethod(),invocation.getArgs(),invocation.getTarget());
        }
    }

}
