package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.dao.LightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.service.*;
import org.tuxdevelop.spring.batch.lightmin.util.BeanRegistrar;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Configuration
@Import(value = {SpringBatchLightminConfiguration.class, RestServiceConfiguration.class})
public class CommonSpringBatchLightminConfiguration {

    @Bean
    public JobService jobService(final SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator) {
        return defaultSpringBatchLightminConfigurator.getJobService();
    }

    @Bean
    public StepService stepService(final SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator) {
        return defaultSpringBatchLightminConfigurator.getStepService();
    }

    @Bean
    public JobExecutionDao jobExecutionDao(final SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator) {
        return defaultSpringBatchLightminConfigurator.getJobExecutionDao();
    }

    @Bean
    public LightminJobExecutionDao lightminJobExecutionDao(final SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator) {
        return defaultSpringBatchLightminConfigurator.getLightminJobExecutionDao();
    }

    @Bean
    public JobInstanceDao jobInstanceDao(final SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator) {
        return defaultSpringBatchLightminConfigurator.getJobInstanceDao();
    }

    @Bean
    public StepExecutionDao stepExecutionDao(final SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator) {
        return defaultSpringBatchLightminConfigurator.getStepExecutionDao();
    }

    @Bean
    public JobOperator jobOperator(final SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator) {
        return defaultSpringBatchLightminConfigurator.getJobOperator();
    }

    @Bean
    public JobLauncher jobLauncher(final SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator) throws Exception {
        return defaultSpringBatchLightminConfigurator.getJobLauncher();
    }

    @Bean
    public JobRegistry jobRegistry(final SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator) {
        return defaultSpringBatchLightminConfigurator.getJobRegistry();
    }

    @Bean
    public JobExplorer jobExplorer(final SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator) throws Exception {
        return defaultSpringBatchLightminConfigurator.getJobExplorer();
    }

    @Bean
    public JobRepository jobRepository(final SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator) throws Exception {
        return defaultSpringBatchLightminConfigurator.getJobRepository();
    }

    @Bean
    public BeanRegistrar beanRegistrar(final ConfigurableApplicationContext context) {
        return new BeanRegistrar(context);
    }

    @Bean
    public SchedulerService schedulerService(final BeanRegistrar beanRegistrar,
                                             final JobRepository jobRepository,
                                             final JobRegistry jobRegistry) throws Exception {
        return new DefaultSchedulerService(beanRegistrar, jobRepository, jobRegistry);
    }

    @Bean
    public AdminService adminService(final JobConfigurationRepository jobConfigurationRepository,
                                     final SchedulerService schedulerService) throws Exception {
        return new DefaultAdminService(jobConfigurationRepository, schedulerService);
    }

    @Bean
    public StepBuilderFactory stepBuilderFactory(final JobRepository jobRepository,
                                                 final SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator) throws Exception {
        return new StepBuilderFactory(jobRepository,
                defaultSpringBatchLightminConfigurator.getTransactionManager());
    }

    @Bean
    public JobBuilderFactory jobBuilderFactory(final JobRepository jobRepository) throws Exception {
        return new JobBuilderFactory(jobRepository);
    }

    @Bean
    public JobCreationListener jobCreationListener(final ApplicationContext applicationContext,
                                                   final JobRegistry jobRegistry,
                                                   final AdminService adminService,
                                                   final SchedulerService schedulerService) throws Exception {
        return new JobCreationListener(applicationContext, jobRegistry, adminService, schedulerService);
    }
}
