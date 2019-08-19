package com.chorifa.minioc.beans;

public class PropertyValue {

    private String name; // FieldName

    private BeanReference value;

    public PropertyValue(String name, BeanReference value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public BeanReference getValue() {
        return value;
    }

    public void setValue(BeanReference value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PropertyValue{" +
                "name='" + name + '\'' +
                ", value=" + value.toString() +
                '}';
    }
}
