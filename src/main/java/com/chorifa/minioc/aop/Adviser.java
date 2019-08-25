package com.chorifa.minioc.aop;

import com.chorifa.minioc.utils.Assert;

public final class Adviser {

    private String scope;

    private Advice advice;

    private AdviceType adviceType;

    public Adviser(String scope, Advice advice, AdviceType adviceType) {
        Assert.notNull(scope,"Adviser: scope[pattern] cannot be null.");
        Assert.notNull(advice, "Adviser: advice cannot be null.");
        Assert.notNull(adviceType, "Adviser: adviceType cannot be null.");
        this.scope = scope;
        this.advice = advice;
        this.adviceType = adviceType;
    }

    public String getScope() {
        return scope;
    }

    public Advice getAdvice() {
        return advice;
    }

    public AdviceType getAdviceType() {
        return adviceType;
    }

    public enum AdviceType{
        BEFORE,
        AROUND,
        AFTER_RETURN,
        AFTER_THROWING,
        AFTER;
    }

}
