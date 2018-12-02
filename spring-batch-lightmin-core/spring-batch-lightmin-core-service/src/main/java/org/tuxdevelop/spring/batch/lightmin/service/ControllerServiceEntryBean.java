package org.tuxdevelop.spring.batch.lightmin.service;

import org.springframework.batch.core.JobInstance;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.client.api.BatchToResourceMapper;
import org.tuxdevelop.spring.batch.lightmin.client.api.DomainToResourceMapper;
import org.tuxdevelop.spring.batch.lightmin.client.api.ResourceToDomainMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the {@link ServiceEntry}
 *
 * @author Marcel Becker
 * @since 0.3
 */
public class ControllerServiceEntryBean implements ServiceEntry {

    private final AdminService adminService;
    private final JobService jobService;
    private final StepService stepService;
    private final JobExecutionQueryService jobExecutionQueryService;
    private final JobLauncherBean jobLauncherBean;

    public ControllerServiceEntryBean(final AdminService adminService,
                                      final JobService jobService,
                                      final StepService stepService,
                                      final JobExecutionQueryService jobExecutionQueryService,
                                      final JobLauncherBean jobLauncherBean) {
        this.adminService = adminService;
        this.jobService = jobService;
        this.stepService = stepService;
        this.jobExecutionQueryService = jobExecutionQueryService;
        this.jobLauncherBean = jobLauncherBean;
    }


    @Override
    public void saveJobConfiguration(final JobConfiguration jobConfiguration) {
        this.adminService.saveJobConfiguration(ResourceToDomainMapper.map(jobConfiguration));
    }

    @Override
    public void updateJobConfiguration(final JobConfiguration jobConfiguration) {
        this.adminService.updateJobConfiguration(ResourceToDomainMapper.map(jobConfiguration));
    }

    @Override
    public void deleteJobConfiguration(final Long jobConfigurationId) {
        this.adminService.deleteJobConfiguration(jobConfigurationId);
    }

    @Override
    public JobConfigurations getJobConfigurationsByJobName(final String jobName) {
        final Collection<org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration> jobConfigurations = this.adminService.getJobConfigurationsByJobName(jobName);
        return DomainToResourceMapper.map(jobConfigurations);
    }

    @Override
    public Map<String, JobConfigurations> getJobConfigurationMap(final Collection<String> jobNames) {
        final Map<String, Collection<org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration>> jobConfigurationMap = this.adminService.getJobConfigurationMap(jobNames);
        final Map<String, JobConfigurations> response = new HashMap<>();
        for (final Map.Entry<String, Collection<org.tuxdevelop.spring.batch.lightmin.domain.JobConfiguration>> entry : jobConfigurationMap.entrySet()) {
            final JobConfigurations jobConfigurations = DomainToResourceMapper.map(entry.getValue());
            response.put(entry.getKey(), jobConfigurations);
        }
        return response;
    }

    @Override
    public JobConfigurations getJobConfigurations(final Collection<String> jobNames) {
        return DomainToResourceMapper.map(this.adminService.getJobConfigurations(jobNames));
    }

    @Override
    public JobConfiguration getJobConfigurationById(final Long jobConfigurationId) {
        return DomainToResourceMapper.map(this.adminService.getJobConfigurationById(jobConfigurationId));
    }

    @Override
    public void stopJobConfiguration(final Long jobConfigurationId) {
        this.adminService.stopJobConfiguration(jobConfigurationId);
    }

    @Override
    public void startJobConfiguration(final Long jobConfigurationId) {
        this.adminService.startJobConfiguration(jobConfigurationId);
    }

    @Override
    public JobExecution getByJobExecutionId(final Long jobExecutionId) {
        final org.springframework.batch.core.JobExecution jobExecution = this.jobService.getJobExecution(jobExecutionId);
        this.jobService.attachJobInstance(jobExecution);
        this.stepService.attachStepExecutions(jobExecution);
        return BatchToResourceMapper.map(jobExecution);

    }

    @Override
    public JobExecutionPage getJobExecutionPage(final Long jobInstanceId,
                                                final Integer startIndex,
                                                final Integer pageSize) {
        final JobInstance jobInstance = this.jobService.getJobInstance(jobInstanceId);
        final Collection<org.springframework.batch.core.JobExecution> jobExecutions = this.jobService.getJobExecutions(jobInstance, startIndex, pageSize);
        final Integer totalJobExecutionCount = this.jobService.getJobExecutionCount(jobInstance);
        final JobExecutionPage jobExecutionPage = new JobExecutionPage();
        jobExecutionPage.setJobName(jobInstance.getJobName());
        jobExecutionPage.setJobInstanceId(jobInstanceId);
        jobExecutionPage.setJobExecutions(BatchToResourceMapper.mapExecutions(jobExecutions));
        jobExecutionPage.setStartIndex(startIndex);
        jobExecutionPage.setPageSize(pageSize);
        jobExecutionPage.setTotalJobExecutionCount(totalJobExecutionCount);
        return jobExecutionPage;
    }

    @Override
    public JobExecutionPage getJobExecutionPage(final Long jobInstanceId) {
        final JobInstance jobInstance = this.jobService.getJobInstance(jobInstanceId);
        final Collection<org.springframework.batch.core.JobExecution> jobExecutions = this.jobService.getJobExecutions(jobInstance);
        final Integer totalJobExecutionCount = this.jobService.getJobExecutionCount(jobInstance);
        final JobExecutionPage jobExecutionPage = new JobExecutionPage();
        jobExecutionPage.setJobName(jobInstance.getJobName());
        jobExecutionPage.setJobInstanceId(jobInstanceId);
        jobExecutionPage.setJobExecutions(BatchToResourceMapper.mapExecutions(jobExecutions));
        jobExecutionPage.setStartIndex(0);
        jobExecutionPage.setPageSize(jobExecutions.size());
        jobExecutionPage.setTotalJobExecutionCount(totalJobExecutionCount);
        return jobExecutionPage;
    }

    @Override
    public JobInstancePage getJobInstancesByJobName(final String jobName, final int startIndex, final int pageSize) {
        final Collection<JobInstance> jobInstanceCollection = this.jobService.getJobInstances(jobName, startIndex, pageSize);
        final Integer jobInstanceCount = this.jobService.getJobInstanceCount(jobName);
        final JobInstancePage jobInstancePage = new JobInstancePage();
        jobInstancePage.setJobName(jobName);
        jobInstancePage.setJobInstances(BatchToResourceMapper.mapInstances(jobInstanceCollection));
        jobInstancePage.setPageSize(jobInstanceCollection.size());
        jobInstancePage.setStartIndex(startIndex);
        jobInstancePage.setTotalJobInstanceCount(jobInstanceCount);
        return jobInstancePage;
    }

    @Override
    public ApplicationJobInfo getApplicationJobInfo() {
        final ApplicationJobInfo applicationJobInfo = new ApplicationJobInfo();
        final Collection<String> jobNames = this.jobService.getJobNames();
        for (final String jobName : jobNames) {
            final JobInfo jobInfo = new JobInfo();
            jobInfo.setJobName(jobName);
            final int instanceCount = this.jobService.getJobInstanceCount(jobName);
            jobInfo.setJobInstanceCount(instanceCount);
            applicationJobInfo.getJobInfos().add(jobInfo);
        }
        return applicationJobInfo;
    }

    @Override
    public JobInfo getJobInfo(final String jobName) {
        final Integer jobInstanceCount = this.jobService.getJobInstanceCount(jobName);
        final JobInfo jobInfo = new JobInfo();
        jobInfo.setJobName(jobName);
        jobInfo.setJobInstanceCount(jobInstanceCount);
        return jobInfo;
    }

    @Override
    public void restartJobExecution(final Long jobExecutionId) {
        this.jobService.restartJobExecution(jobExecutionId);
    }

    @Override
    public void stopJobExecution(final Long jobExecutionId) {
        this.jobService.stopJobExecution(jobExecutionId);
    }

    @Override
    public StepExecution getStepExecution(final Long jobExecutionId, final Long stepExecutionId) {
        final org.springframework.batch.core.JobExecution jobExecution = this.jobService.getJobExecution(jobExecutionId);
        return BatchToResourceMapper.map(this.stepService.getStepExecution(jobExecution, stepExecutionId));
    }

    @Override
    public void launchJob(final JobLaunch jobLaunch) {
        this.jobLauncherBean.launchJob(jobLaunch);
    }

    @Override
    public JobParameters getLastJobParameters(final String jobName) {
        final org.springframework.batch.core.JobParameters jobParameters = this.jobService.getLastJobParameters(jobName);
        return BatchToResourceMapper.map(jobParameters);
    }

    @Override
    public List<JobExecution> findJobExecutions(final String jobName, final Map<String, Object> queryParameter, final Integer resultSize) {
        final List<org.springframework.batch.core.JobExecution> jobExecutions = this.jobExecutionQueryService.findJobExecutions(jobName, queryParameter, resultSize);
        return BatchToResourceMapper.mapExecutions(jobExecutions);
    }
}
