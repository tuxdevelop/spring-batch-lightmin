package org.tuxdevelop.spring.batch.lightmin.util;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;
import java.util.Set;

/**
 * @author Marcel Becker
 * @since 0.1
 * <p>
 * Utility class the register and unregister classes within the current application context on the fly
 * </p>
 */
public class BeanRegistrar {

    private final ConfigurableApplicationContext context;

    public BeanRegistrar(final ConfigurableApplicationContext context) {
        this.context = context;
    }

    /**
     * registers beans within the current application context of the given class with the given parameters
     *
     * @param beanClass             Class of the bean to be generated
     * @param beanName              unique name of the bean to be generated
     * @param constructorValues     Set of Objects, which will be passed as contructor values
     * @param constructorReferences Set of Object which will be passed as constructor references
     * @param propertyValues        Map of String,Object which will be passed to the key (property) name as value
     * @param propertyReferences    Map of String,Object which will be passed to the key (property) name as reference
     * @param dependsOnBeans        Set of Strings, which contains depending bean names
     */
    public void registerBean(final Class<?> beanClass, final String beanName,
                             final Set<Object> constructorValues,
                             final Set<String> constructorReferences,
                             final Map<String, Object> propertyValues,
                             final Map<String, String> propertyReferences,
                             final Set<String> dependsOnBeans) {
        final BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(beanClass);
        builder.setAutowireMode(AbstractBeanDefinition.DEPENDENCY_CHECK_ALL);
        addConstructorArgReferences(builder, constructorReferences);
        addConstructorArgValues(builder, constructorValues);
        addPropertyReference(builder, propertyReferences);
        addPropertyValues(builder, propertyValues);
        addDependsOnBean(builder, dependsOnBeans);
        final DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getBeanFactory();
        factory.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    /**
     * unregisters a bean of the current application context
     *
     * @param beanName name of the bean, which should be destroyed on current application context
     * @throws NoSuchBeanDefinitionException if the context does not contain a bean with the given name
     */
    public void unregisterBean(final String beanName) {
        final BeanDefinitionRegistry factory = (BeanDefinitionRegistry) context.getAutowireCapableBeanFactory();
        if (factory.containsBeanDefinition(beanName)) {
            ((DefaultListableBeanFactory) factory).destroySingleton(beanName);
            factory.removeBeanDefinition(beanName);
        } else {
            throw new NoSuchBeanDefinitionException(
                    "No Bean definition exists for bean name: " + beanName);
        }
    }

    private void addConstructorArgValues(final BeanDefinitionBuilder builder,
                                         final Set<Object> constructorValues) {
        if (constructorValues != null) {
            for (final Object constructorReference : constructorValues) {
                builder.addConstructorArgValue(constructorReference);
            }
        }
    }

    private void addConstructorArgReferences(
            final BeanDefinitionBuilder builder,
            final Set<String> constructorReferences) {
        if (constructorReferences != null) {
            for (final String beanName : constructorReferences) {
                builder.addConstructorArgReference(beanName);
            }
        }
    }

    private void addPropertyValues(final BeanDefinitionBuilder builder,
                                   final Map<String, Object> propertyValues) {
        if (propertyValues != null) {
            for (final Map.Entry<String, Object> propertyValue : propertyValues
                    .entrySet()) {
                builder.addPropertyValue(propertyValue.getKey(),
                        propertyValue.getValue());
            }
        }
    }

    private void addPropertyReference(final BeanDefinitionBuilder builder,
                                      final Map<String, String> propertyReferences) {
        if (propertyReferences != null) {
            for (final Map.Entry<String, String> propertyReference : propertyReferences
                    .entrySet()) {
                builder.addPropertyReference(propertyReference.getKey(),
                        propertyReference.getValue());
            }
        }
    }

    private void addDependsOnBean(final BeanDefinitionBuilder builder,
                                  final Set<String> beanNames) {
        if (beanNames != null) {
            for (final String beanName : beanNames) {
                builder.addDependsOn(beanName);
            }
        }
    }
}
