package com.chorifa.minioc.aop;

import com.chorifa.minioc.aop.interceptor.MethodInterceptor;
import com.chorifa.minioc.aop.matcher.AdviserMatcher;
import com.chorifa.minioc.utils.Assert;
import com.chorifa.minioc.utils.exceptions.AopException;

import java.lang.reflect.Method;
import java.util.List;

public class DefaultMethodInvocation extends MethodInvocation {

    private final List<MethodInterceptor> interceptors;

    public DefaultMethodInvocation(final Object target, final Method method, final Object[] args, final List<MethodInterceptor> interceptors) {
        super(target, method, args);
        Assert.notNull(interceptors,"DefaultMethodInvocation: List<MethodInterceptor> cannot be null...");
        this.interceptors = interceptors;
    }

    @Override
    public Object flow() throws Throwable {
        while (index < interceptors.size()){
            MethodInterceptor interceptor = interceptors.get(index++);
            if(isMatch(interceptor))
                return interceptor.invoke(this);
        }

        return method.invoke(target,args); // TODO change method to MethodProxy
    }

    private boolean isMatch(MethodInterceptor interceptor){
        String pattern = interceptor.getPattern();
        int index = pattern.lastIndexOf(String.valueOf(AdviserMatcher.DELIMITER));
        if(index == -1) throw new AopException("Interceptor: pattern < "+pattern+" > do not have DELIMITER");
        return AdviserMatcher.isMatch(method.getName(), pattern.substring(index+1));
    }

}
