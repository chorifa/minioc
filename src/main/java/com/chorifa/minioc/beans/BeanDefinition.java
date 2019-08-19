package com.chorifa.minioc.beans;

import com.chorifa.minioc.utils.exceptions.BeanException;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class BeanDefinition {

    // private Object bean;

    private final String beanName;

    private final Class<?> beanClass;

    private final PropertyValues propertyValues;

    // construction-args

    private Constructor<?> constructor;

    private BeanReference[] constructorArgs;

    private volatile Object status;

    private Scope scope;

    public BeanDefinition(String beanName, Class<?> beanClass) {
        this.beanName = beanName;
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    @SuppressWarnings("unchecked")
    public Status getStatus() {
        if(scope == Scope.SINGLETON){ // no need sync
            return (Status) status;
        }else if(scope == Scope.PROTOTYPE){
            return ((ThreadLocal<Status>)status).get();
        }else
            throw new BeanException("BeanDefinition: illegal value of scope >>> "+scope.name());
    }

    public void setStatus(Status newStatus){
        if(scope == Scope.SINGLETON){
            this.status = newStatus;
        }else if(scope == Scope.PROTOTYPE){
            @SuppressWarnings("unchecked")
            ThreadLocal<Status> tl = (ThreadLocal<Status>)status;
            tl.set(newStatus);
        }else
            throw new BeanException("BeanDefinition: illegal value of scope >>> "+scope.name());
    }

    /*
    public boolean setStatus(Status expectStatus, Status newStatus){
        if(scope == Scope.SINGLETON) {
            synchronized (this){
                if (status == expectStatus) {
                    status = newStatus;
                    if(newStatus == Status.AVAILABLE)
                        this.notifyAll(); // notify all
                    return true;
                } else return false;
            }
        }else if(scope == Scope.PROTOTYPE){
            @SuppressWarnings("unchecked")
            ThreadLocal<Status> tl = (ThreadLocal<Status>)status;
            if(tl.get() == expectStatus){
                tl.set(newStatus);
                return true;
            } else return false;
        }else
            throw new BeanException("BeanDefinition: illegal value of scope >>> "+scope.name());
    }
     */

    public void waitForAvailable(long timeout, TimeUnit unit) throws InterruptedException {
        if(scope == Scope.SINGLETON){
            if(status == Status.AVAILABLE) return;
            synchronized (this){
                long timeoutMillis = unit==TimeUnit.MILLISECONDS?timeout:TimeUnit.MILLISECONDS.convert(timeout, unit);
                if(status != Status.AVAILABLE)
                    this.wait(timeoutMillis);
            }
            if(status != Status.AVAILABLE)
                throw new BeanException("BeanDefinition: Singleton Bean >>> "+this.beanClass+" wait for available timeout");
        }else if(scope != Scope.PROTOTYPE)
            throw new BeanException("BeanDefinition: illegal value of scope >>> "+scope.name());
    }

    public String getBeanName() {
        return beanName;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public BeanReference[] getConstructorArgs() {
        return constructorArgs;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public void setConstructorArgs(BeanReference[] constructorArgs) {
        this.constructorArgs = constructorArgs;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
        if(scope == Scope.SINGLETON)
            status = Status.NOOP;
        else if(scope == Scope.PROTOTYPE)
            status = ThreadLocal.withInitial(() -> Status.NOOP);
    }

    public void destroy(){
        if(status instanceof ThreadLocal)
            ((ThreadLocal) status).remove();
        propertyValues.getPropertyValueList().clear();
        if(constructorArgs != null)
            Arrays.fill(constructorArgs, null);
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "beanName='" + beanName + '\'' +
                ", beanClass=" + beanClass +
                ", propertyValues=" + propertyValues +
                ", constructionArgs=" + Arrays.toString(constructorArgs) +
                ", status=" + status +
                ", scope=" + scope +
                '}';
    }

    public enum Status{
        NOOP,
        IN_CREATE,
        IN_INITIALIZE,
        UNREACHABLE,
        AVAILABLE;
    }

    public enum Scope{
        SINGLETON,
        PROTOTYPE;
    }

}
