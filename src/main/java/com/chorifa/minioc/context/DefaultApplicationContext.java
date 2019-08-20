package com.chorifa.minioc.context;

import com.chorifa.minioc.beans.BeanDefinition;
import com.chorifa.minioc.beans.factory.BeanFactory;
import com.chorifa.minioc.beans.factory.DefaultBeanFactory;
import com.chorifa.minioc.utils.io.AnnotationParser;
import com.chorifa.minioc.utils.io.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultApplicationContext extends AbstractApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(DefaultApplicationContext.class);

    private final String packageName;

    public DefaultApplicationContext(String packageName) {
        super(new DefaultBeanFactory());
        this.packageName = packageName;
        refresh(); // refresh is thread-safe for beanFactory
    }

    @Override
    protected void loadBeanDefinition(BeanFactory beanFactory) {
        List<String> names = ClassScanner.getAllClassName(packageName,true,true);
        Class<?> clazz;
        for(String name : names){
            try {
                clazz = Class.forName(name);
                BeanDefinition beanDefinition = AnnotationParser.parseClassToBean(clazz);
                BeanDefinition[] beanDefinitions = AnnotationParser.parseBeanInClass(clazz);
                if(beanDefinition != null)
                    beanFactory.registerBeanDefinition(beanDefinition.getBeanName(),beanDefinition);
                if(beanDefinitions != null && beanDefinitions.length != 0)
                    for(BeanDefinition bd : beanDefinitions)
                        beanFactory.registerBeanDefinition(bd.getBeanName(),bd);
            } catch (ClassNotFoundException e) {
                logger.warn("DefaultApplicationContext: class {} not found.", name);
            }
        }
    }

}
