package org.tuxdevelop.spring.batch.lightmin.support;

import org.springframework.batch.core.JobInstance;
import org.tuxdevelop.spring.batch.lightmin.api.resource.AdminToResourceMapper;
import org.tuxdevelop.spring.batch.lightmin.api.resource.BatchToResourceMapper;
import org.tuxdevelop.spring.batch.lightmin.api.resource.ResourceToAdminMapper;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.service.AdminService;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.service.StepService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class ControllerServiceEntryBean implements ServiceEntry {

    private final AdminService adminService;
    private final JobService jobService;
    private final StepService stepService;
    private final JobLauncherBean jobLauncherBean;

    public ControllerServiceEntryBean(final AdminService adminService,
                                      final JobService jobService,
                                      final StepService stepService,
                                      final JobLauncherBean jobLauncherBean) {
        this.adminService = adminService;
        this.jobService = jobService;
        this.stepService = stepService;
        this.jobLauncherBean = jobLauncherBean;
    }


    @Override
    public void saveJobConfiguration(final JobConfiguration jobConfiguration) {
        adminService.saveJobConfiguration(ResourceToAdminMapper.map(jobConfiguration));
    }

    @Override
    public void updateJobConfiguration(final JobConfiguration jobConfiguration) {
        adminService.updateJobConfiguration(ResourceToAdminMapper.map(jobConfiguration));
    }

    @Override
    public void deleteJobConfiguration(final Long jobConfigurationId) {
        adminService.deleteJobConfiguration(jobConfigurationId);
    }

    @Override
    public JobConfigurations getJobConfigurationsByJobName(final String jobName) {
        final Collection<org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration> jobConfigurations = adminService.getJobConfigurationsByJobName(jobName);
        return AdminToResourceMapper.map(jobConfigurations);
    }

    @Override
    public Map<String, JobConfigurations> getJobConfigurationMap(final Collection<String> jobNames) {
        final Map<String, Collection<org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration>> jobConfigurationMap = adminService.getJobConfigurationMap(jobNames);
        final Map<String, JobConfigurations> response = new HashMap<>();
        for (final Map.Entry<String, Collection<org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration>> entry : jobConfigurationMap.entrySet()) {
            final JobConfigurations jobConfigurations = AdminToResourceMapper.map(entry.getValue());
            response.put(entry.getKey(), jobConfigurations);
        }
        return response;
    }

    @Override
    public JobConfigurations getJobConfigurations(final Collection<String> jobNames) {
        return AdminToResourceMapper.map(adminService.getJobConfigurations(jobNames));
    }

    @Override
    public JobConfiguration getJobConfigurationById(final Long jobConfigurationId) {
        return AdminToResourceMapper.map(adminService.getJobConfigurationById(jobConfigurationId));
    }

    @Override
    public void stopJobConfiguration(final Long jobConfigurationId) {
        adminService.stopJobConfiguration(jobConfigurationId);
    }

    @Override
    public void startJobConfiguration(final Long jobConfigurationId) {
        adminService.startJobConfiguration(jobConfigurationId);
    }

    @Override
    public JobExecution getByJobExecutionId(final Long jobExecutionId) {
        final org.springframework.batch.core.JobExecution jobExecution = jobService.getJobExecution(jobExecutionId);
        jobService.attachJobInstance(jobExecution);
        stepService.attachStepExecutions(jobExecution);
        return BatchToResourceMapper.map(jobExecution);

    }

    @Override
    public JobExecutionPage getJobExecutionPage(final Long jobInstanceId,
                                                final Integer startIndex,
                                                final Integer pageSize) {
        final JobInstance jobInstance = jobService.getJobInstance(jobInstanceId);
        final Collection<org.springframework.batch.core.JobExecution> jobExecutions = jobService.getJobExecutions(jobInstance, startIndex, pageSize);
        final Integer totalJobExecutionCount = jobService.getJobExecutionCount(jobInstance);
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
        final JobInstance jobInstance = jobService.getJobInstance(jobInstanceId);
        final Collection<org.springframework.batch.core.JobExecution> jobExecutions = jobService.getJobExecutions(jobInstance);
        final Integer totalJobExecutionCount = jobService.getJobExecutionCount(jobInstance);
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
        final Collection<JobInstance> jobInstanceCollection = jobService.getJobInstances(jobName, startIndex, pageSize);
        final Integer jobInstanceCount = jobService.getJobInstanceCount(jobName);
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
        final Collection<String> jobNames = jobService.getJobNames();
        for (final String jobName : jobNames) {
            final JobInfo jobInfo = new JobInfo();
            jobInfo.setJobName(jobName);
            final int instanceCount = jobService.getJobInstanceCount(jobName);
            jobInfo.setJobInstanceCount(instanceCount);
            applicationJobInfo.getJobInfos().add(jobInfo);
        }
        return applicationJobInfo;
    }

    @Override
    public JobInfo getJobInfo(final String jobName) {
        final Integer jobInstanceCount = jobService.getJobInstanceCount(jobName);
        final JobInfo jobInfo = new JobInfo();
        jobInfo.setJobName(jobName);
        jobInfo.setJobInstanceCount(jobInstanceCount);
        return jobInfo;
    }

    @Override
    public void restartJobExecution(final Long jobExecutionId) {
        jobService.restartJobExecution(jobExecutionId);
    }

    @Override
    public void stopJobExecution(final Long jobExecutionId) {
        jobService.stopJobExecution(jobExecutionId);
    }

    @Override
    public StepExecution getStepExecution(final Long jobExecutionId, final Long stepExecutionId) {
        final org.springframework.batch.core.JobExecution jobExecution = jobService.getJobExecution(jobExecutionId);
        return BatchToResourceMapper.map(stepService.getStepExecution(jobExecution, stepExecutionId));
    }

    @Override
    public void launchJob(final JobLaunch jobLaunch) {
        jobLauncherBean.launchJob(jobLaunch);
    }

    @Override
    public JobParameters getLastJobParameters(final String jobName) {
        final org.springframework.batch.core.JobParameters jobParameters = jobService.getLastJobParameters(jobName);
        return BatchToResourceMapper.map(jobParameters);
    }
}
