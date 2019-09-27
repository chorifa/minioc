package com.chorifa.minioc.aop;

import java.lang.reflect.Method;

public interface Advice {

    void before(Method method, Object[] args, Object target) throws Throwable;

    void after(Method method, Object[] args, Object target) throws Throwable;

    void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable;

    void afterThrowing(Throwable th, Method method, Object[] args, Object target) throws Throwable;

    @Deprecated
    Object around(ProcessJoin join) throws Throwable;

    Object around(MethodInvocation invocation, Method method, Object[] args, Object target) throws Throwable;

}
