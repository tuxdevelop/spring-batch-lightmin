package org.tuxdevelop.spring.batch.lightmin.util;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.ITConfiguration;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.admin.scheduler.PeriodScheduler;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BeanRegistrarIT {

    @Autowired
    private BeanRegistrar beanRegistrar;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Job simpleJob;

    @Autowired
    private JobLauncher jobLauncher;

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

    //FIXME: Bean found, but is unregistered
    @Ignore
    //@Test(expected = NoSuchBeanDefinitionException.class)
    public void unregisterBeanStringIT() {
        beanRegistrar.registerBean(String.class, "sampleString", null, null, null, null, null);
        final String registeredBean = (String) applicationContext.getBean("sampleString");
        assertThat(registeredBean).isNotNull();
        beanRegistrar.unregisterBean("sampleString");
        final String gotBean = (String) applicationContext.getBean("sampleString");
        log.info("got: " + gotBean);
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
        final Set<Object> constructorValues = new HashSet<Object>();
        constructorValues.add(schedulerConstructorWrapper);
        beanRegistrar
                .registerBean(PeriodScheduler.class, "sampleBeanRegistrar", constructorValues, null, null, null, null);
        final PeriodScheduler periodScheduler = applicationContext.getBean("sampleBeanRegistrar", PeriodScheduler
                .class);
        assertThat(periodScheduler).isNotNull();
        periodScheduler.schedule();
    }
}
