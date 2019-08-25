package com.chorifa.minioc.beans;

import java.lang.reflect.Method;

/**
 * FactoryBean is used for @Bean
 */
public class FactoryBeanDefinition extends BeanDefinition {

    private BeanReference invoker;

    private Method method;

    private BeanReference[] methodArgs;

    public FactoryBeanDefinition(String beanName, Class<?> beanClass) {
        super(beanName, beanClass);
    }

    public Method getMethod() {
        return method;
    }

    public BeanReference[] getMethodArgs() {
        return methodArgs;
    }

    public BeanReference getInvoker() {
        return invoker;
    }

    public void setInvoker(BeanReference invoker) {
        this.invoker = invoker;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setMethodArgs(BeanReference[] methodArgs) {
        this.methodArgs = methodArgs;
    }
}
