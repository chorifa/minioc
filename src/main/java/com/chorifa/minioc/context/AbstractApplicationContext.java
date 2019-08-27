package com.chorifa.minioc.context;

import com.chorifa.minioc.aop.Adviser;
import com.chorifa.minioc.aop.interceptor.MethodInterceptor;
import com.chorifa.minioc.beans.BeanDefinition;
import com.chorifa.minioc.beans.factory.BeanFactory;
import com.chorifa.minioc.beans.factory.DefaultBeanFactory;
import com.chorifa.minioc.utils.exceptions.BeanException;

import java.util.List;

public abstract class AbstractApplicationContext implements ApplicationContext{

    private final BeanFactory beanFactory;

    AbstractApplicationContext(DefaultBeanFactory beanFactory){
        this.beanFactory = beanFactory;
    }

    @Override @Deprecated
    public void addAdvisers(Adviser[] advisers){
        this.beanFactory.addAdvisers(advisers);
    }

    @Override
    public void addInterceptors(List<MethodInterceptor> interceptors){
        this.beanFactory.addInterceptors(interceptors);
    }

    @Override
    public void sortInterceptors() {
        beanFactory.sortInterceptors();
    }

    void refresh(){
        synchronized (beanFactory) {
            loadBeanDefinition(beanFactory);
            onRefresh();
        }
    }

    protected abstract void loadBeanDefinition(BeanFactory beanFactory);

    private void onRefresh(){
        beanFactory.preInstantiateSingletons();
    }

    @Override
    public Object getBean(String name){
        return beanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeanException {
        return beanFactory.getBean(requiredType);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeanException {
        return beanFactory.getBean(name, requiredType);
    }

    @Override
    public void preInstantiateSingletons() {
        beanFactory.preInstantiateSingletons();
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanFactory.registerBeanDefinition(name, beanDefinition);
    }
}
