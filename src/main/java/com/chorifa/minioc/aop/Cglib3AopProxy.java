package com.chorifa.minioc.aop;

import com.chorifa.minioc.utils.Assert;
import com.chorifa.minioc.utils.exceptions.AopException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

public class Cglib3AopProxy implements AopProxy, MethodInterceptor {

    private final Object target;

    private final List<com.chorifa.minioc.aop.interceptor.MethodInterceptor> interceptors;

    public Cglib3AopProxy(Object target, List<com.chorifa.minioc.aop.interceptor.MethodInterceptor> interceptors) {
        Assert.notNull(target, "Cglib3AopProxy: Object target cannot be null in constructor.");
        Assert.notNull(interceptors, "Cglib3AopProxy: List<MethodInterceptors> interceptors cannot be null in constructor.");
        this.target = target;
        this.interceptors = interceptors;
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        try {
            return enhancer.create();
        }catch (Exception e){
            throw new AopException("CglibAopProxy: currently, aop not support class without default constructor. " +
                    "please check and add one. Note that, it will not influence your purpose.",e);
        }
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        return new DefaultMethodInvocation(target,method,args,interceptors).flow();
    }

}
