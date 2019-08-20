package com.chorifa.minioc.utils.io;

import com.chorifa.minioc.annotation.Bean;
import com.chorifa.minioc.beans.BeanDefinition;
import com.chorifa.minioc.beans.BeanReference;
import com.chorifa.minioc.beans.FactoryBeanDefinition;
import com.chorifa.minioc.beans.PropertyValue;
import com.chorifa.minioc.utils.exceptions.AnnotationException;
import com.chorifa.minioc.utils.exceptions.BeanException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class AnnotationParser {

    /**
     * Annotation: @Scope @Qualifier must on Annotation_Type
     * Annotation on class: @Named @Singleton @(Prototype) Or Empty
     * Annotation on Field: @Inject @Named
     * Annotation on Construction: @Inject @Named (args)
     * Annotation in future: @Bean, @Configure
     * @param clazz class to be parsed
     * @return BeanDefinition
     */
    public static BeanDefinition parseClassToBean(Class<?> clazz){
        if(clazz == null)
            throw new AnnotationException("AnnotationParser: clazz in parseClassToBean is Null.");
        //check @Named and @Singleton on class
        BeanDefinition beanDefinition;
        boolean isValid = false;
        Named named = clazz.getAnnotation(Named.class);
        if(named != null && !named.value().isEmpty()){ // Class with @Named("declared name")
            beanDefinition = new BeanDefinition(named.value(),clazz);
            beanDefinition.setScope(clazz.isAnnotationPresent(Singleton.class)? BeanDefinition.Scope.SINGLETON : BeanDefinition.Scope.PROTOTYPE);
            isValid = true;
        }else if(clazz.getAnnotation(Singleton.class) != null){ // Class with no or empty @Name
            beanDefinition = new BeanDefinition(clazz.getName(),clazz);
            beanDefinition.setScope(BeanDefinition.Scope.SINGLETON);
            isValid = true;
        }else{
            beanDefinition = new BeanDefinition(clazz.getName(),clazz);
            beanDefinition.setScope(BeanDefinition.Scope.PROTOTYPE);
        }

        // check @Inject on field
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            if(field.isAnnotationPresent(Inject.class)){ // @inject
                named = field.getAnnotation(Named.class);
                BeanReference beanReference;
                if(named != null && !named.value().isEmpty()){ // @Named("declare name")
                    beanReference = new BeanReference(named.value(),field.getType());
                }else{ // @Named
                    beanReference = new BeanReference(field.getType());
                }
                PropertyValue propertyValue = new PropertyValue(field.getName(),beanReference);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
        }

        Constructor<?>[] constructors = clazz.getConstructors();
        boolean hasConstructor = false;
        for(Constructor<?> constructor : constructors){
            if(constructor.isAnnotationPresent(Inject.class)) {
                if (hasConstructor)
                    throw new AnnotationException("AnnotationParser: not support Multi-Constructors with @Inject in Class " + clazz.getName());
                BeanReference[] constructorArgs = parseParameters(constructor.getParameters());
                hasConstructor = true;
                beanDefinition.setConstructorArgs(constructorArgs);
                beanDefinition.setConstructor(constructor);
                isValid = true;
            }
        }
        if(!hasConstructor){
            try{
                beanDefinition.setConstructor(clazz.getDeclaredConstructor());
            }catch (NoSuchMethodException e){
                if(isValid)
                    throw new BeanException("BeanDefinition: Bean name >"+beanDefinition.getBeanName()+
                         "< with class >"+clazz+"< do not have resolvable constructors.");
            }
        }
        //TODO setter methods

        if(isValid) return beanDefinition;
        beanDefinition.destroy();
        return null;
    }

    /**
     * parse Annotation @Bean
     * also may be annotated by @Named @Singleton on Method
     * and be annotated by @Named on Parameter
     * @param clazz class to be parsed
     * @return all BeanDefinitions annotate by @Bean in class clazz
     */
    public static BeanDefinition[] parseBeanInClass(Class<?> clazz){
        if(clazz == null)
            throw new AnnotationException("AnnotationParser: clazz in parseClassToBean is Null.");
        if(clazz.isAnnotationPresent(Named.class) || clazz.isAnnotationPresent(Singleton.class)) {
            Named clazzNamed = clazz.getAnnotation(Named.class);
            final BeanReference clazzReference;
            if(clazzNamed != null && !clazzNamed.value().isEmpty())
                clazzReference = new BeanReference(clazzNamed.value(),clazz);
            else clazzReference = new BeanReference(clazz);

            List<BeanDefinition> list = new ArrayList<>();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Bean.class)) {
                    String beanName;
                    Named named = method.getAnnotation(Named.class);
                    if (named != null && !named.value().isEmpty())
                        beanName = named.value();
                    else beanName = method.getName();
                    FactoryBeanDefinition beanDefinition = new FactoryBeanDefinition(beanName, method.getReturnType());
                    beanDefinition.setScope(method.isAnnotationPresent(Singleton.class) ? BeanDefinition.Scope.SINGLETON : BeanDefinition.Scope.PROTOTYPE);
                    BeanReference[] methodArgs = parseParameters(method.getParameters());
                    beanDefinition.setMethod(method);
                    beanDefinition.setMethodArgs(methodArgs);
                    beanDefinition.setInvoker(clazzReference);
                    list.add(beanDefinition);
                }
            }
            if (list.size() == 0) return null;
            else return list.toArray(new BeanDefinition[0]);
        }
        return null;
    }

    private static BeanReference[] parseParameters(Parameter[] parameters){
        if(parameters == null || parameters.length == 0) return null;
        Named named;
        BeanReference[] args = new BeanReference[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            named = parameters[i].getAnnotation(Named.class);
            if (named != null && !named.value().isEmpty()) { // Parameter has @Named("declared name")
                args[i] = new BeanReference(named.value(), parameters[i].getType());
            } else { // Parameter has no or empty @Name
                args[i] = new BeanReference(parameters[i].getType());
            }
        }
        return args;
    }


}
