package org.tuxdevelop.spring.batch.lightmin.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;
import java.util.Set;

public class BeanRegistrar {

    @Autowired
    private ConfigurableApplicationContext context;

    public void registerBean(final Class beanClass, final String beanName, final Set<Object> constructorValues,
                             final Set<String> constructorReferences, final Map<String, Object> propertyValues, final
    Map<String, String> propertyReferences, final Set<String> dependsOnBeans) {
        final BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(beanClass);
        addConstructorArgReferences(builder, constructorReferences);
        addConstructorArgValues(builder, constructorValues);
        addPropertyReference(builder, propertyReferences);
        addPropertyValues(builder, propertyValues);
        addDependsOnBean(builder, dependsOnBeans);
        final DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getBeanFactory();
        factory.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    private void addConstructorArgValues(final BeanDefinitionBuilder builder, Set<Object> constructorValues) {
        if (constructorValues != null) {
            for (final Object constructorReference : constructorValues) {
                builder.addConstructorArgValue(constructorReference);
            }
        }
    }

    private void addConstructorArgReferences(final BeanDefinitionBuilder builder, Set<String> constructorReferences) {
        if (constructorReferences != null) {
            for (final String beanName : constructorReferences) {
                builder.addConstructorArgReference(beanName);
            }
        }
    }

    private void addPropertyValues(final BeanDefinitionBuilder builder, Map<String, Object> propertyValues) {
        if (propertyValues != null) {
            for (Map.Entry<String, Object> propertyValue : propertyValues.entrySet()) {
                builder.addPropertyValue(propertyValue.getKey(), propertyValue.getValue());
            }
        }
    }

    private void addPropertyReference(final BeanDefinitionBuilder builder, Map<String, String> propertyReferences) {
        if (propertyReferences != null) {
            for (Map.Entry<String, String> propertyReference : propertyReferences.entrySet()) {
                builder.addPropertyReference(propertyReference.getKey(), propertyReference.getValue());
            }
        }
    }

    private void addDependsOnBean(final BeanDefinitionBuilder builder, final Set<String> beanNames) {
        if (beanNames != null) {
            for (String beanName : beanNames) {
                builder.addDependsOn(beanName);
            }
        }
    }
}
