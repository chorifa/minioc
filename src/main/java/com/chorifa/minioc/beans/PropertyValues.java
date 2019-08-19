package com.chorifa.minioc.beans;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {

    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public void addPropertyValue(PropertyValue value){
        propertyValueList.add(value);
    }

    public List<PropertyValue> getPropertyValueList() {
        return propertyValueList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        propertyValueList.forEach((x)-> sb.append(x.toString()).append("\n"));
        return "PropertyValues{" +
                "propertyValueList=" + sb.toString() +
                '}';
    }
}
