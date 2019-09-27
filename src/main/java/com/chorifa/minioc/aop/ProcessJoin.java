package com.chorifa.minioc.aop;

import net.sf.cglib.proxy.MethodProxy;

@Deprecated
public class ProcessJoin {

    private Object bean;

    private MethodProxy methodProxy;

    private Object[] args;

    public ProcessJoin(Object bean, MethodProxy methodProxy, Object[] args) {
        this.bean = bean;
        this.methodProxy = methodProxy;
        this.args = args;
    }

    public Object proceed() throws Throwable{
        return methodProxy.invoke(bean,args);
    }

}
