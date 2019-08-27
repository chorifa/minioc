package com.chorifa.minioc.aop;

import com.chorifa.minioc.utils.Assert;

import java.lang.reflect.Method;

public abstract class MethodInvocation {

    protected final Object target;

    protected final Method method;

    protected final Object[] args;

    protected int index = 0;

    public MethodInvocation(Object target, Method method, Object[] args) {
        Assert.notNull(target,"MethodInvocation: target cannot be null...");
        Assert.notNull(method,"MethodInvocation: method cannot be null...");
        this.target = target;
        this.method = method;
        this.args = args;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public abstract Object flow() throws Throwable;

}
