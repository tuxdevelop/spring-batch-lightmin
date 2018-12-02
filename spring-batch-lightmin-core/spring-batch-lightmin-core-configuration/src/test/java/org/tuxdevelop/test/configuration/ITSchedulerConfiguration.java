package org.tuxdevelop.test.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.tuxdevelop.spring.batch.lightmin.annotation.EnableLightminCoreConfiguration;
import org.tuxdevelop.spring.batch.lightmin.domain.*;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminMapConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.scheduler.CronScheduler;
import org.tuxdevelop.spring.batch.lightmin.scheduler.PeriodScheduler;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;

@Configuration
@EnableLightminCoreConfiguration
@EnableLightminMapConfigurationRepository
public class ITSchedulerConfiguration {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job simpleJob;

    @Bean
    public PeriodScheduler periodScheduler(final ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(null,
                10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final SchedulerConstructorWrapper schedulerConstructorWrapper = new SchedulerConstructorWrapper();
        schedulerConstructorWrapper.setJob(this.simpleJob);
        schedulerConstructorWrapper.setJobConfiguration(jobConfiguration);
        schedulerConstructorWrapper.setJobIncrementer(JobIncrementer.DATE);
        schedulerConstructorWrapper.setJobLauncher(this.jobLauncher);
        schedulerConstructorWrapper.setJobParameters(new JobParametersBuilder().toJobParameters());
        schedulerConstructorWrapper.setThreadPoolTaskScheduler(threadPoolTaskScheduler);
        final PeriodScheduler periodScheduler = new PeriodScheduler(schedulerConstructorWrapper);
        return periodScheduler;
    }

    @Bean
    public CronScheduler cronScheduler(final ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        final JobSchedulerConfiguration jobSchedulerConfiguration = DomainTestHelper.createJobSchedulerConfiguration(
                "0 0/5 * * * ?", null, null, JobSchedulerType.CRON);
        final JobConfiguration jobConfiguration = DomainTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        final SchedulerConstructorWrapper schedulerConstructorWrapper = new SchedulerConstructorWrapper();
        schedulerConstructorWrapper.setJob(this.simpleJob);
        schedulerConstructorWrapper.setJobConfiguration(jobConfiguration);
        schedulerConstructorWrapper.setJobIncrementer(JobIncrementer.DATE);
        schedulerConstructorWrapper.setJobLauncher(this.jobLauncher);
        schedulerConstructorWrapper.setJobParameters(new JobParametersBuilder().toJobParameters());
        schedulerConstructorWrapper.setThreadPoolTaskScheduler(threadPoolTaskScheduler);
        final CronScheduler cronScheduler = new CronScheduler(schedulerConstructorWrapper);
        return cronScheduler;
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        return scheduler;
    }
}
