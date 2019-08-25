package com.chorifa.minioc.entity;

import com.chorifa.minioc.annotation.Aspect;
import com.chorifa.minioc.annotation.PointCut;
import com.chorifa.minioc.aop.DefaultAdvice;
import com.chorifa.minioc.aop.ProcessJoin;

import java.lang.reflect.Method;

@PointCut("*.entity.*:*") @Aspect
public class AopAspect extends DefaultAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("Aop: before");
    }

    @Override
    public void after(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("Aop: after");
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("Aop: after return");
    }

    @Override
    public void afterThrowing(Throwable th, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("Aop: after throw");
    }

    @Override
    public Object around(ProcessJoin join) throws Throwable {
        System.out.println("Aop: around before");
        Object retVal = join.proceed();
        System.out.println("Aop: around after");
        return retVal;
    }
}
