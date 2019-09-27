package com.chorifa.minioc.aop;

import java.lang.reflect.Method;

public class DefaultAdviceAdapter implements Advice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {

    }

    @Override
    public void after(Method method, Object[] args, Object target) throws Throwable {

    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {

    }

    @Override
    public void afterThrowing(Throwable th, Method method, Object[] args, Object target) throws Throwable {

    }

    @Override @Deprecated
    public Object around(ProcessJoin join) throws Throwable{
        return join.proceed();
    }

    @Override
    public Object around(MethodInvocation invocation, Method method, Object[] args, Object target) throws Throwable {
        return invocation.flow();
    }
}
