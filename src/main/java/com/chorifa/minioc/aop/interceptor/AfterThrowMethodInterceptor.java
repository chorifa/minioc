package com.chorifa.minioc.aop.interceptor;

import com.chorifa.minioc.aop.Advice;
import com.chorifa.minioc.aop.MethodInvocation;

public class AfterThrowMethodInterceptor extends MethodInterceptor {

    public AfterThrowMethodInterceptor(Advice advice, String pattern) {
        super(advice, pattern);
        this.priority = 2;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.flow();
        }catch (Throwable th){
            advice.afterThrowing(th,invocation.getMethod(),invocation.getArgs(),invocation.getTarget());
            throw th;
        }
    }

}
