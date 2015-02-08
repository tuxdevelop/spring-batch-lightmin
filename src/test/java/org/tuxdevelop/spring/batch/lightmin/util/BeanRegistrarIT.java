package org.tuxdevelop.spring.batch.lightmin.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.ITConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BeanRegistrarIT {

    @Autowired
    private BeanRegistrar beanRegistrar;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void registerBeanStringIT() {
        beanRegistrar.registerBean(String.class, "sampleString", null, null, null, null, null);
        final String registeredBean = (String) applicationContext.getBean("sampleString");
        assertThat(registeredBean).isNotNull();
    }

    @Test
    public void registerBeanStringValueIT() {
        final Set<Object> constructorValues = new HashSet<Object>();
        constructorValues.add("Test");
        beanRegistrar.registerBean(String.class, "sampleString", constructorValues, null, null, null, null);
        final String registeredBean = (String) applicationContext.getBean("sampleString");
        assertThat(registeredBean).isNotNull();
        assertThat(registeredBean).isEqualTo("Test");
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void unregisterBeanStringIT() {
        beanRegistrar.registerBean(String.class, "sampleString", null, null, null, null, null);
        final String registeredBean = (String) applicationContext.getBean("sampleString");
        assertThat(registeredBean).isNotNull();
        beanRegistrar.unregisterBean("sampleString");
        applicationContext.getBean("sampleString");
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void unregisterBeanNotFoundIT(){
        beanRegistrar.unregisterBean("notExistingBean");
    }
}
