package com.chorifa.minioc.beans;

public class BeanReference {
    private String beanName;

    private final Class<?> type;

    public BeanReference(Class<?> type) {
        this.type = type;
        this.beanName = null;
    }

    public BeanReference(String beanName, Class<?> type) {
        this.beanName = beanName;
        this.type = type;
    }

    public boolean byName(){
        return beanName != null;
    }

    public String getBeanName() {
        return beanName;
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "BeanReference{" +
                "beanName='" + beanName + '\'' +
                ", type=" + type +
                '}';
    }
}
