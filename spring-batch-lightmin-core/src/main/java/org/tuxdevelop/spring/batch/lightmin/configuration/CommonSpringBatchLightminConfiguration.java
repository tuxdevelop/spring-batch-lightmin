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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.service.*;
import org.tuxdevelop.spring.batch.lightmin.util.BeanRegistrar;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Configuration
@Import(value = {SpringBatchLightminConfiguration.class, RestServiceConfiguration.class})
public class CommonSpringBatchLightminConfiguration implements InitializingBean {

    @Autowired
    private JobConfigurationRepository jobConfigurationRepository;

    @Autowired
    private SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public JobService jobService() {
        return defaultSpringBatchLightminConfigurator.getJobService();
    }

    @Bean
    public StepService stepService() {
        return defaultSpringBatchLightminConfigurator.getStepService();
    }

    @Bean
    public JobExecutionDao jobExecutionDao() {
        return defaultSpringBatchLightminConfigurator.getJobExecutionDao();
    }

    @Bean
    public JobInstanceDao jobInstanceDao() {
        return defaultSpringBatchLightminConfigurator.getJobInstanceDao();
    }

    @Bean
    public StepExecutionDao stepExecutionDao() {
        return defaultSpringBatchLightminConfigurator.getStepExecutionDao();
    }

    @Bean
    public JobOperator jobOperator() {
        return defaultSpringBatchLightminConfigurator.getJobOperator();
    }

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        return defaultSpringBatchLightminConfigurator.getJobLauncher();
    }

    @Bean
    public JobRegistry jobRegistry() {
        return defaultSpringBatchLightminConfigurator.getJobRegistry();
    }

    @Bean
    public JobExplorer jobExplorer() throws Exception {
        return defaultSpringBatchLightminConfigurator.getJobExplorer();
    }

    @Bean
    public JobRepository jobRepository() throws Exception {
        return defaultSpringBatchLightminConfigurator.getJobRepository();
    }

    @Bean
    public BeanRegistrar beanRegistrar() {
        return new BeanRegistrar();
    }

    @Bean
    public SchedulerService schedulerService() throws Exception {
        return new DefaultSchedulerService(beanRegistrar(), jobRepository(), jobRegistry());
    }

    @Bean
    public AdminService adminService() throws Exception {
        return new DefaultAdminService(jobConfigurationRepository, schedulerService());
    }

    @Bean
    public StepBuilderFactory stepBuilderFactory() throws Exception {
        return new StepBuilderFactory(jobRepository(),
                defaultSpringBatchLightminConfigurator.getTransactionManager());
    }

    @Bean
    public JobBuilderFactory jobBuilderFactory() throws Exception {
        return new JobBuilderFactory(jobRepository());
    }

    @Bean
    public JobCreationListener jobCreationListener() throws Exception {
        return new JobCreationListener(applicationContext, jobRegistry(), adminService(), schedulerService());
    }

    @Override
    public void afterPropertiesSet() {
        assert jobConfigurationRepository != null;
    }
}
