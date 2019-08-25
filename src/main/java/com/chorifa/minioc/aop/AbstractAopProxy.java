package com.chorifa.minioc.aop;

import java.util.List;

public abstract class AbstractAopProxy {

    protected List<Adviser> advisers;

    AbstractAopProxy(List<Adviser> advisers){
        this.advisers = advisers;
    }

    public abstract Object getProxy();

}
