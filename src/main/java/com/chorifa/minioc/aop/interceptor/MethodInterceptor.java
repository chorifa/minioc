package com.chorifa.minioc.aop.interceptor;

import com.chorifa.minioc.aop.Advice;
import com.chorifa.minioc.aop.MethodInvocation;
import com.chorifa.minioc.utils.Assert;

public abstract class MethodInterceptor implements Comparable<MethodInterceptor> {

    protected final Advice advice;

    private String pattern;

    protected int priority;

    public MethodInterceptor(Advice advice, String pattern) {
        Assert.notNull(advice,"MethodInterceptor: advice cannot be null...");
        Assert.notNull(pattern, "MethodInterceptor: pattern cannot be null...");
        this.advice = advice;
        this.pattern = pattern;
    }

    public Advice getAdvice() {
        return advice;
    }

    public String getPattern() {
        return pattern;
    }

    public abstract Object invoke (MethodInvocation invocation) throws Throwable;

    @Override
    public int compareTo(MethodInterceptor o) {
        return Integer.compare(this.priority, o.priority);
    }

}
