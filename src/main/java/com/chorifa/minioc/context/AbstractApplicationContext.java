package com.chorifa.minioc.context;

import com.chorifa.minioc.beans.BeanDefinition;
import com.chorifa.minioc.beans.factory.BeanFactory;
import com.chorifa.minioc.beans.factory.DefaultBeanFactory;
import com.chorifa.minioc.utils.exceptions.BeanException;

public abstract class AbstractApplicationContext implements ApplicationContext{

    private final BeanFactory beanFactory;

    AbstractApplicationContext(DefaultBeanFactory beanFactory){
        this.beanFactory = beanFactory;
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
