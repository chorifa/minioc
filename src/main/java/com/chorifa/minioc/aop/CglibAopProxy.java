package com.chorifa.minioc.aop;

import com.chorifa.minioc.aop.matcher.AdviserMatcher;
import com.chorifa.minioc.utils.Assert;
import com.chorifa.minioc.utils.exceptions.AopException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

@Deprecated
public class CglibAopProxy  implements MethodInterceptor, AopProxy{

    private final Object target;

    private final AdviserMatcher matcher;

    private List<Adviser> advisers;

    public CglibAopProxy(List<Adviser> advisers, Object target, AdviserMatcher matcher) {
        Assert.notNull(target, "CglibAopProxy: Object target cannot be null in constructor.");
        Assert.notNull(matcher, "CglibAopProxy: AdviserMatcher matcher cannot be null in constructor.");
        this.advisers = advisers;
        this.target = target;
        this.matcher = matcher;
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
        // if has advise for this method , do interceptor
        // else invoke origin
        Adviser[] methodAdviser = matcher.getPriorityAdviserMatchForMethod(method.getName(),advisers);
        if(methodAdviser == null){
            return methodProxy.invoke(target,args);
        }else{
            try{
                if(methodAdviser[0] != null)
                    methodAdviser[0].getAdvice().before(method,args,target);
                Object retVal = null;
                if(methodAdviser[1] != null)
                    retVal = methodAdviser[1].getAdvice().around(new ProcessJoin(target,methodProxy,args));
                else retVal = methodProxy.invoke(target,args);
                if(methodAdviser[2] != null)
                    methodAdviser[2].getAdvice().afterReturning(retVal,method,args,target);
                return retVal;
            }catch (Throwable t){
                if(methodAdviser[3] != null)
                    methodAdviser[3].getAdvice().afterThrowing(t,method,args,target);
                else throw t;
            }finally {
                if(methodAdviser[4] != null)
                    methodAdviser[4].getAdvice().after(method,args,target);
            }
            return null;
        }
    }

}
