package com.chorifa.minioc.entity;

import com.chorifa.minioc.annotation.Aspect;
import com.chorifa.minioc.annotation.PointCut;
import com.chorifa.minioc.aop.DefaultAdvice;
import com.chorifa.minioc.aop.MethodInvocation;
import com.chorifa.minioc.aop.ProcessJoin;

import java.lang.reflect.Method;

@PointCut("*.entity.*:*") @Aspect
public class AopAspect1 extends DefaultAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("AopAspect1: before");
    }

    @Override
    public void after(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("AopAspect1: after");
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("AopAspect1: after return");
    }

    @Override
    public void afterThrowing(Throwable th, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("AopAspect1: after throw");
    }

    @Override
    public Object around(MethodInvocation invocation, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("AopAspect1: around before");
        Object retVal = invocation.flow();
        System.out.println("AopAspect1: around after");
        return retVal;
    }

    @Override
    public Object around(ProcessJoin join) throws Throwable {
        System.out.println("AopAspect1: around before");
        Object retVal = join.proceed();
        System.out.println("AopAspect1: around after");
        return retVal;
    }
}
