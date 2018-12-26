package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.LightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.event.listener.JobExecutionFinishedJobExecutionListener;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.service.*;
import org.tuxdevelop.spring.batch.lightmin.util.BeanRegistrar;

@Configuration
public class SpringBatchLightminServiceConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = {SchedulerService.class})
    public SchedulerService schedulerService(final BeanRegistrar beanRegistrar,
                                             final JobRepository jobRepository,
                                             final JobRegistry jobRegistry) {
        return new DefaultSchedulerService(beanRegistrar, jobRepository, jobRegistry);
    }

    @Bean
    @ConditionalOnMissingBean(value = {ListenerService.class})
    public ListenerService listenerService(final BeanRegistrar beanRegistrar,
                                           final JobRegistry jobRegistry,
                                           final JobRepository jobRepository) {
        return new DefaultListenerService(beanRegistrar, jobRegistry, jobRepository);
    }

    @Bean
    @ConditionalOnMissingBean(value = {AdminService.class})
    public AdminService adminService(final JobConfigurationRepository jobConfigurationRepository,
                                     final SchedulerService schedulerService,
                                     final ListenerService listenerService,
                                     final SpringBatchLightminCoreConfigurationProperties properties) {
        return new DefaultAdminService(jobConfigurationRepository, schedulerService, listenerService, properties);
    }

    @Bean
    @ConditionalOnMissingBean(value = {JobService.class})
    public JobService jobService(final JobOperator jobOperator,
                                 final JobRegistry jobRegistry,
                                 final JobExplorer jobExplorer,
                                 final LightminJobExecutionDao lightminJobExecutionDao) throws Exception {
        final JobService jobService =
                new DefaultJobService(jobOperator, jobRegistry, jobExplorer, lightminJobExecutionDao);
        jobService.afterPropertiesSet();
        return jobService;
    }

    @Bean
    @ConditionalOnMissingBean(value = {StepService.class})
    public StepService stepService(final JobExplorer jobExplorer) throws Exception {
        final StepService stepService = new DefaultStepService(jobExplorer);
        stepService.afterPropertiesSet();
        return stepService;
    }

    @Bean
    @ConditionalOnMissingBean(value = {JobExecutionQueryService.class})
    public JobExecutionQueryService jobExecutionQueryService(final LightminJobExecutionDao lightminJobExecutionDao) {
        return new DefaultJobExecutionQueryService(lightminJobExecutionDao);
    }

    @Bean
    @ConditionalOnMissingBean(value = {JobLauncherBean.class})
    public JobLauncherBean jobLauncherBean(
            @Qualifier("defaultAsyncJobLauncher") final JobLauncher defaultAsyncJobLauncher,
            final JobRegistry jobRegistry,
            final SpringBatchLightminCoreConfigurationProperties properties) {

        return new JobLauncherBean(defaultAsyncJobLauncher, jobRegistry, properties);
    }

    @Bean
    public JobCreationListener jobCreationListener(
            final ApplicationContext applicationContext,
            final JobRegistry jobRegistry,
            final AdminService adminService,
            final SchedulerService schedulerService,
            final ListenerService listenerService,
            final JobExecutionListenerRegisterBean jobExecutionListenerRegisterBean) {
        return new JobCreationListener(
                applicationContext,
                jobRegistry,
                adminService,
                schedulerService,
                listenerService,
                jobExecutionListenerRegisterBean);
    }

    @Bean
    @Qualifier("jobExecutionFinishedJobExecutionListener")
    public JobExecutionListener jobExecutionFinishedJobExecutionListener(
            final SpringBatchLightminCoreConfigurationProperties properties) {
        return new JobExecutionFinishedJobExecutionListener(properties);
    }

    @Bean
    public JobExecutionListenerRegisterBean jobExecutionListenerRegisterBean(
            @Qualifier("jobExecutionFinishedJobExecutionListener") final JobExecutionListener jobExecutionFinishedJobExecutionListener) {
        return new JobExecutionListenerRegisterBean(jobExecutionFinishedJobExecutionListener);
    }

}
