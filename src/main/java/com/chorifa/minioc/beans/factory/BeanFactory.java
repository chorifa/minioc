package com.chorifa.minioc.beans.factory;

import com.chorifa.minioc.aop.Adviser;
import com.chorifa.minioc.beans.BeanDefinition;
import com.chorifa.minioc.utils.exceptions.BeanException;

public interface BeanFactory {

    Object getBean(String name) throws BeanException;

    <T> T getBean(String name, Class<T> requiredType) throws BeanException;

    <T> T getBean(Class<T> requiredType) throws BeanException;

    void preInstantiateSingletons();

    void registerBeanDefinition(String name, BeanDefinition beanDefinition);

    void addAdvisers(Adviser[] advisers);

}
