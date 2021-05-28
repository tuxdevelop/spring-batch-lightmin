package org.tuxdevelop.spring.batch.lightmin.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.scheduler.PeriodScheduler;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;
import org.tuxdevelop.test.configuration.ITConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
public class BeanRegistrarIT {

    private BeanRegistrar beanRegistrar;
    private ConfigurableApplicationContext applicationContext;
    private Job simpleJob;
    private JobLauncher jobLauncher;

    @Test
    public void registerUnregisteredBean() {
        this.beanRegistrar.registerBean(String.class, "firstBean", null, null, null, null, null);
        String registeredBean = (String) this.applicationContext.getBean("firstBean");
        assertThat(registeredBean).isNotNull();

        this.beanRegistrar.unregisterBean("firstBean");
        assertFalse(this.applicationContext.containsBeanDefinition("firstBean"));

        this.beanRegistrar.registerBean(String.class, "firstBean", null, null, null, null, null);
        registeredBean = (String) this.applicationContext.getBean("firstBean");
        assertThat(registeredBean).isNotNull();
    }

    @Test
    public void registerBeanStringIT() {
        this.beanRegistrar.registerBean(String.class, "sampleString", null, null, null, null, null);
        final String registeredBean = (String) this.applicationContext.getBean("sampleString");
        assertThat(registeredBean).isNotNull();
    }

    @Test
    public void registerBeanStringValueIT() {
        final Set<Object> constructorValues = new HashSet<>();
        constructorValues.add("Test");
        this.beanRegistrar.registerBean(String.class, "sampleString", constructorValues, null, null, null, null);
        final String registeredBean = (String) this.applicationContext.getBean("sampleString");
        assertThat(registeredBean).isNotNull();
        assertThat(registeredBean).isEqualTo("Test");
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void unregisterBeanStringIT() {
        final Set<Object> constructorValues = new HashSet<>();
        constructorValues.add("sampleStringSecond");
        this.beanRegistrar.registerBean(String.class, "sampleStringSecond", constructorValues, null, null, null, null);
        final String registeredBean = (String) this.applicationContext.getBean("sampleStringSecond");
        assertThat(registeredBean).isNotNull();
        assertThat(registeredBean).isEqualTo("sampleStringSecond");
        this.beanRegistrar.unregisterBean("sampleStringSecond");
        final String gotBean = this.applicationContext.getBean("sampleStringSecond", String.class);
        log.info("got: " + gotBean);
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void unregisterBeanNotFoundIT() {
        this.beanRegistrar.unregisterBean("notExistingBean");
    }

    @Test
    public void registerPeriodSchedulerIT() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        final SchedulerConstructorWrapper schedulerConstructorWrapper = new SchedulerConstructorWrapper();
        schedulerConstructorWrapper.setJob(this.simpleJob);
        schedulerConstructorWrapper.setJobConfiguration(jobConfiguration);
        schedulerConstructorWrapper.setJobIncrementer(JobIncrementer.DATE);
        schedulerConstructorWrapper.setJobLauncher(this.jobLauncher);
        schedulerConstructorWrapper.setJobParameters(new JobParametersBuilder().toJobParameters());
        schedulerConstructorWrapper.setThreadPoolTaskScheduler(scheduler);
        final Set<Object> constructorValues = new HashSet<>();
        constructorValues.add(schedulerConstructorWrapper);
        this.beanRegistrar
                .registerBean(PeriodScheduler.class, "sampleBeanRegistrar", constructorValues, null, null, null, null);
        final PeriodScheduler periodScheduler = this.applicationContext.getBean("sampleBeanRegistrar", PeriodScheduler
                .class);
        assertThat(periodScheduler).isNotNull();
        periodScheduler.schedule();
    }

    @Before
    public void init() {
        this.applicationContext = new AnnotationConfigApplicationContext(ITConfiguration.class);
        this.beanRegistrar = this.applicationContext.getBean(BeanRegistrar.class);
        this.simpleJob = this.applicationContext.getBean("simpleJob", Job.class);
        this.jobLauncher = this.applicationContext.getBean("jobLauncher", JobLauncher.class);
    }

    @After
    public void tearDown() {
        this.applicationContext.close();
    }
}
