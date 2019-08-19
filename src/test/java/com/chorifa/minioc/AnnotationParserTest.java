package com.chorifa.minioc;

import com.chorifa.minioc.beans.BeanDefinition;
import com.chorifa.minioc.utils.io.AnnotationParser;
import com.chorifa.minioc.utils.io.ClassScanner;
import org.junit.Test;

import java.util.List;

public class AnnotationParserTest {

    @Test
    public void testAnnotationParser(){
        List<String> classes = ClassScanner.getAllClassName(this.getClass().getPackageName(),true,false);
        for(String s : classes){
            try {
                Class<?> clazz = Class.forName(s);
                BeanDefinition beanDefinition = AnnotationParser.parseClassToBean(clazz);
                if(beanDefinition != null)
                    System.out.println(beanDefinition);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
