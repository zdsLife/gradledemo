package com.zds.treedemo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Iterator;
import java.util.Map;

/**
 * @author houyaqian
 * @date 2020/10/22
 */
@Slf4j
public class SpringFactoryUtil {

    private static ApplicationContext applicationContext = null;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        if (SpringFactoryUtil.applicationContext == null) {
            SpringFactoryUtil.applicationContext = applicationContext;
        }

    }

    /**
     * @param className         注册class
     * @param serviceName 注册别名
     * @param propertyMap 注入属性
     */
    public static void addBean(String className, String serviceName, Map propertyMap) {
        try {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
            if (propertyMap != null) {
                Iterator<?> entries = propertyMap.entrySet().iterator();
                Map.Entry<?, ?> entry;
                while (entries.hasNext()) {
                    entry = (Map.Entry<?, ?>) entries.next();
                    String key = (String) entry.getKey();
                    Object val = entry.getValue();
                    beanDefinitionBuilder.addPropertyValue(key, val);
                }
            }
            registerBean(serviceName, beanDefinitionBuilder.getRawBeanDefinition(), applicationContext);
        } catch (ClassNotFoundException e) {
            log.error("{}主动注册失败.", className);
        }
    }

    public static void addBean(Class clz, String serviceName, Map propertyMap) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clz);
        if (propertyMap != null) {
            Iterator<?> entries = propertyMap.entrySet().iterator();
            Map.Entry<?, ?> entry;
            while (entries.hasNext()) {
                entry = (Map.Entry<?, ?>) entries.next();
                String key = (String) entry.getKey();
                Object val = entry.getValue();
                beanDefinitionBuilder.addPropertyValue(key, val);
            }
        }
        registerBean(serviceName, beanDefinitionBuilder.getRawBeanDefinition(), applicationContext);
    }

    private static void registerBean(String beanName, BeanDefinition beanDefinition, ApplicationContext context) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) configurableApplicationContext
                .getBeanFactory();
        beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);

    }

    /**
     * 通过class获取Bean.
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
