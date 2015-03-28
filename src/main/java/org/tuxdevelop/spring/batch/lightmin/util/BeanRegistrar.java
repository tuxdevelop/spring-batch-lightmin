package org.tuxdevelop.spring.batch.lightmin.util;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class BeanRegistrar {

	@Autowired
	private ConfigurableApplicationContext context;

	/**
	 *
	 * @param beanClass
	 * @param beanName
	 * @param constructorValues
	 * @param constructorReferences
	 * @param propertyValues
	 * @param propertyReferences
	 * @param dependsOnBeans
	 */
	public void registerBean(final Class<?> beanClass, final String beanName,
			final Set<Object> constructorValues,
			final Set<String> constructorReferences,
			final Map<String, Object> propertyValues,
			final Map<String, String> propertyReferences,
			final Set<String> dependsOnBeans) {
		final BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.rootBeanDefinition(beanClass);
		addConstructorArgReferences(builder, constructorReferences);
		addConstructorArgValues(builder, constructorValues);
		addPropertyReference(builder, propertyReferences);
		addPropertyValues(builder, propertyValues);
		addDependsOnBean(builder, dependsOnBeans);
		final DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context
				.getBeanFactory();
		factory.registerBeanDefinition(beanName, builder.getBeanDefinition());
	}

	/**
	 *
	 * @param beanName
	 */
	public void unregisterBean(final String beanName) {
		final BeanDefinitionRegistry factory = (BeanDefinitionRegistry) context
				.getAutowireCapableBeanFactory();
		if (factory.containsBeanDefinition(beanName)) {
			((DefaultListableBeanFactory) factory).destroySingleton(beanName);
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
