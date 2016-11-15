package org.tuxdevelop.spring.batch.lightmin.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.admin.scheduler.PeriodScheduler;
import org.tuxdevelop.test.configuration.ITConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class BeanRegistrarIT {

    private BeanRegistrar beanRegistrar;
    private ConfigurableApplicationContext applicationContext;
    private Job simpleJob;
    private JobLauncher jobLauncher;

    @Test
    public void registerBeanStringIT() {
        beanRegistrar.registerBean(String.class, "sampleString", null, null, null, null, null);
        final String registeredBean = (String) applicationContext.getBean("sampleString");
        assertThat(registeredBean).isNotNull();
        applicationContext.close();
    }

    @Test
    public void registerBeanStringValueIT() {
        final Set<Object> constructorValues = new HashSet<>();
        constructorValues.add("Test");
        beanRegistrar.registerBean(String.class, "sampleString", constructorValues, null, null, null, null);
        final String registeredBean = (String) applicationContext.getBean("sampleString");
        assertThat(registeredBean).isNotNull();
        assertThat(registeredBean).isEqualTo("Test");
        applicationContext.close();
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void unregisterBeanStringIT() {
        final Set<Object> constructorValues = new HashSet<>();
        constructorValues.add("sampleStringSecond");
        beanRegistrar.registerBean(String.class, "sampleStringSecond", constructorValues, null, null, null, null);
        final String registeredBean = (String) applicationContext.getBean("sampleStringSecond");
        assertThat(registeredBean).isNotNull();
        assertThat(registeredBean).isEqualTo("sampleStringSecond");
        beanRegistrar.unregisterBean("sampleStringSecond");
        final String gotBean = applicationContext.getBean("sampleStringSecond", String.class);
        log.info("got: " + gotBean);
        applicationContext.close();
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void unregisterBeanNotFoundIT() {
        beanRegistrar.unregisterBean("notExistingBean");
    }

    @Test
    public void registerPeriodSchedulerIT() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final SchedulerConstructorWrapper schedulerConstructorWrapper = new SchedulerConstructorWrapper();
        schedulerConstructorWrapper.setJob(simpleJob);
        schedulerConstructorWrapper.setJobConfiguration(jobConfiguration);
        schedulerConstructorWrapper.setJobIncrementer(JobIncrementer.DATE);
        schedulerConstructorWrapper.setJobLauncher(jobLauncher);
        schedulerConstructorWrapper.setJobParameters(new JobParametersBuilder().toJobParameters());
        final Set<Object> constructorValues = new HashSet<>();
        constructorValues.add(schedulerConstructorWrapper);
        beanRegistrar
                .registerBean(PeriodScheduler.class, "sampleBeanRegistrar", constructorValues, null, null, null, null);
        final PeriodScheduler periodScheduler = applicationContext.getBean("sampleBeanRegistrar", PeriodScheduler
                .class);
        assertThat(periodScheduler).isNotNull();
        periodScheduler.schedule();
        applicationContext.close();
    }

    @Before
    public void init() {
        applicationContext = new AnnotationConfigApplicationContext(ITConfiguration.class);
        beanRegistrar = applicationContext.getBean(BeanRegistrar.class);
        simpleJob = applicationContext.getBean("simpleJob", Job.class);
        jobLauncher = applicationContext.getBean("jobLauncher", JobLauncher.class);
    }
}
