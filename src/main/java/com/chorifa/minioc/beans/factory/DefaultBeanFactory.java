package com.chorifa.minioc.beans.factory;

import com.chorifa.minioc.beans.BeanDefinition;
import com.chorifa.minioc.beans.BeanReference;
import com.chorifa.minioc.beans.PropertyValue;
import com.chorifa.minioc.utils.exceptions.BeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory implements BeanFactory{

    private static final Logger logger = LoggerFactory.getLogger(DefaultBeanFactory.class);

    private final Map<String /*bean name*/, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final List<String> beanNames = new ArrayList<>();

    private final Map<String /*bean name*/, Object /*beans*/> readyBeans = new HashMap<>();

    private final Map<String /*bean name*/, Object /*early bean*/> earlyBeans = new HashMap<>();

    private final Map<Class<?> /*class*/, String /*bean name*/> typeToNameMap = new HashMap<>();

    @Override
    public Object getBean(String name) throws BeanException {
        return doGetBean(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) throws BeanException {
        String name = convertFromTypeToName(requiredType);
        return (T) doGetBean(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name, Class<T> requiredType) throws BeanException {
        Object bean = doGetBean(name);
        return (T) bean;
    }

    private Object doGetBean(String name){
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if(beanDefinition == null)
            throw new BeanException("DefaultBeanFactory: Bean >'"+name+"'< not found.");
        String beanName = beanDefinition.getBeanName();
        String className = beanDefinition.getBeanClass().getName();
        switch (beanDefinition.getScope()){
            case PROTOTYPE:
                switch (beanDefinition.getStatus()){
                    case NOOP:
                        beanDefinition.setStatus(BeanDefinition.Status.IN_CREATE);
                        Object bean;
                        try {
                            bean = createBean(beanDefinition);
                            bean = populateBean(bean,beanDefinition);
                        }catch (BeanException e){
                            beanDefinition.setStatus(BeanDefinition.Status.UNREACHABLE);
                            throw e;
                        }
                        beanDefinition.setStatus(BeanDefinition.Status.NOOP); // success create
                        return bean;
                    case IN_CREATE:
                    case IN_INITIALIZE:
                        throw new BeanException("DefaultBeanFactory: Bean >'"+beanName+" -- "
                                +className+"'< encounter Circular Dependency (Prototype Mood).");
                    case UNREACHABLE:
                        throw new BeanException("DefaultBeanFactory: Bean >'"+beanName+" -- "
                            +className+"'< IN Status >UN_REACHABLE< .");
                    case AVAILABLE:
                        throw new BeanException("DefaultBeanFactory: Bean >'"+beanName+" -- "
                                +className+"'< IN Status >AVAILABLE< (Prototype Mood), which"+
                                "should not happen");
                    default:
                        throw new BeanException("DefaultBeanFactory: Bean >'"+beanName+" -- "
                                +className+"'< IN unknown Status >"+beanDefinition.getStatus()+
                                "<, which should not happen.");
                }
            case SINGLETON:
                synchronized (this){
                    switch (beanDefinition.getStatus()){
                        case NOOP: // create
                            beanDefinition.setStatus(BeanDefinition.Status.IN_CREATE); //
                            Object earlyBean;
                            try {
                                earlyBean = createBean(beanDefinition);
                            }catch (BeanException e){
                                beanDefinition.setStatus(BeanDefinition.Status.UNREACHABLE);
                                throw e;
                            }
                            earlyBeans.put(beanName,earlyBean); // register into earlyBeans
                            beanDefinition.setStatus(BeanDefinition.Status.IN_INITIALIZE);
                            Object readyBean;
                            try {
                                readyBean = populateBean(earlyBean, beanDefinition);
                            }catch (BeanException e){
                                beanDefinition.setStatus(BeanDefinition.Status.UNREACHABLE);
                                throw e;
                            }finally {
                                if(!earlyBeans.remove(beanName,earlyBean))
                                    logger.warn("DefaultBeanFactory: earlyBeans do not contain {} when removing," +
                                            " which should not happen.",beanName);
                            }
                            readyBeans.put(beanName,readyBean); // register into readyBeans
                            beanDefinition.setStatus(BeanDefinition.Status.AVAILABLE);
                            return readyBean;
                        case IN_CREATE:
                            beanDefinition.setStatus(BeanDefinition.Status.UNREACHABLE); // unreachable
                            throw new BeanException("DefaultBeanFactory: Bean >'"+beanName+" -- "
                                    +className+"'< encounter Circular Dependency (Singleton Mood).");
                        case IN_INITIALIZE:
                            logger.info("DefaultBeanFactory: Bean >'{} -- {}'< meet early access.",beanName,className);
                            earlyBean = earlyBeans.get(beanName);
                            if(earlyBean == null)
                                throw new BeanException("DefaultBeanFactory: Bean >'"+beanName+" -- "
                                        +className+"'< IN Status >INITIALIZE< and" +
                                        "need early access, but earlyMap do not contain.");
                            return earlyBean;
                        case AVAILABLE:
                            readyBean = readyBeans.get(beanName);
                            if(readyBean == null)
                                throw new BeanException("DefaultBeanFactory: Bean >'"+beanName+" -- "
                                        +className+"'< IN Status >AVAILABLE< , but"+
                                        "readyMap do not contain");
                            return readyBean;
                        case UNREACHABLE:
                            throw new BeanException("DefaultBeanFactory: Bean >'"+beanName+" -- "
                                    +className+"'< IN Status >UN_REACHABLE< .");
                        default:
                            throw new BeanException("DefaultBeanFactory: Bean >'"+beanName+" -- "
                                    +className+"'< IN unknown Status >"+beanDefinition.getStatus()+
                                    "<, which should not happen.");
                    }
                }
            default:
                throw new BeanException("BeanDefinition: Bean >'"+beanName+" -- "
                        +className+"'< has unsupported scope"+beanDefinition.getScope());
        }
    }

    private Object createBean(BeanDefinition beanDefinition){
        BeanReference[] constructorArgs = beanDefinition.getConstructorArgs();
        if(constructorArgs != null && constructorArgs.length != 0) {
            Object[] args = new Object[constructorArgs.length]; // cannot cache args, caz singleton do not need, while prototype can not
            String argBeanName;
            for (int i = 0; i < args.length; i++) {
                if (!constructorArgs[i].byName())
                    argBeanName = convertFromTypeToName(constructorArgs[i].getType());
                else argBeanName = constructorArgs[i].getBeanName();
                args[i] = doGetBean(argBeanName);
                typeToNameMap.put(constructorArgs[i].getType(), argBeanName); // put if can get bean
            }
            try {
                return beanDefinition.getConstructor().newInstance(args);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                logger.error("DefaultBeanFactory: error occur when constructor.newInstance(Object...)", e);
                throw new BeanException(e);
            }
        }else{
            try {
                return beanDefinition.getConstructor().newInstance();
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                logger.error("DefaultBeanFactory: error occur when constructor.newInstance(Object...)", e);
                throw new BeanException(e);
            }
        }
    }

    private Object populateBean(Object earlyBean, BeanDefinition beanDefinition){
        if(!beanDefinition.getBeanClass().isInstance(earlyBean))
            throw new BeanException("DefaultBeanFactory: earlyBean is not instance of class >"+ beanDefinition.getBeanClass().getName()
                    +"< declared in BeanDefinition, when populateBean");
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues().getPropertyValueList();
        String fieldName;
        String fieldBeanName;
        BeanReference ref;
        for(PropertyValue propertyValue : propertyValues){
            fieldName = propertyValue.getName();
            ref = propertyValue.getValue();
            if(ref.byName())
                fieldBeanName = ref.getBeanName();
            else fieldBeanName = convertFromTypeToName(ref.getType());
            Object o = doGetBean(fieldBeanName);
            typeToNameMap.put(ref.getType(),fieldBeanName); // put if can get bean

            Field field;
            try{
                field = beanDefinition.getBeanClass().getDeclaredField(fieldName);
                field.setAccessible(true); // close security check
                field.set(earlyBean,o);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error("DefaultBeanFactory: error occur when field = getDeclaredField() or field.set()",e);
                throw new BeanException(e);
            }
        }
        return earlyBean;
    }

    private String convertFromTypeToName(Class<?> clazz){
        String beanName = typeToNameMap.get(clazz);
        if(beanName != null) return beanName;
        for(BeanDefinition beanDefinition : beanDefinitionMap.values()){
            if(beanDefinition.getBeanClass().isAssignableFrom(clazz)){
                if(beanName != null)
                    throw new BeanException("BeanDefinition: Class >"+clazz.toString()+
                        "< has multi-suitable beans: "+beanName+" and "+ beanDefinition.getBeanName());
                beanName = beanDefinition.getBeanName();
            }
        }
        if(beanName == null)
            throw new BeanException("BeanDefinition: Class >"+clazz.toString()+
                    "< do not have suitable beans.");
        return beanName;
    }

    // need sync(this) -> sync(beanDefinitionMap)
    public synchronized void registerBeanDefinition(String name, BeanDefinition beanDefinition){
        this.beanDefinitionMap.put(name, beanDefinition);
        this.beanNames.add(name);
    }

    public synchronized void preInstantiateSingletons(){
        for(String beanName : beanNames){
            try {
                getBean(beanName);
            }catch (BeanException e){
                logger.error("DefaultBeanFactory: encounter exception when get Bean {}",beanName,e);
            }
        }
    }

}
