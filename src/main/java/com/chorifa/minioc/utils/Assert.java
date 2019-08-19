package com.chorifa.minioc.utils;

public abstract class Assert {

    public static void notNull(Object o, String message){
        if(o == null)
            throw new IllegalArgumentException(message);
    }

    public static void isNull(Object o, String message){
        if(o != null)
            throw new IllegalArgumentException(message);
    }

}
