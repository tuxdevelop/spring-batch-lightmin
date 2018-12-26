package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.util.CollectionUtils;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.LightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.util.*;

/**
 * Default implementation of {@link org.tuxdevelop.spring.batch.lightmin.service.JobService}
 *
 * @author Marcel Becker
 * @since 0.1
 */
@Slf4j
public class DefaultJobService implements JobService {

    private final JobOperator jobOperator;
    private final JobRegistry jobRegistry;
    private final JobExplorer jobExplorer;
    private final LightminJobExecutionDao lightminJobExecutionDao;

    public DefaultJobService(final JobOperator jobOperator,
                             final JobRegistry jobRegistry,
                             final JobExplorer jobExplorer,
                             final LightminJobExecutionDao lightminJobExecutionDao) {
        this.jobOperator = jobOperator;
        this.jobRegistry = jobRegistry;
        this.jobExplorer = jobExplorer;
        this.lightminJobExecutionDao = lightminJobExecutionDao;
    }

    @Override
    public int getJobInstanceCount(final String jobName) {
        int jobInstanceCount = 0;
        try {
            jobInstanceCount = this.jobExplorer.getJobInstanceCount(jobName);
        } catch (final NoSuchJobException e) {
            log.info(e.getMessage(), e);
        }
        return jobInstanceCount;
    }

    @Override
    public int getJobExecutionCount(final JobInstance jobInstance) {
        return this.lightminJobExecutionDao.getJobExecutionCount(jobInstance);
    }

    @Override
    public Set<String> getJobNames() {
        return new HashSet<>(this.jobRegistry.getJobNames());
    }

    @Override
    public Job getJobByName(final String jobName) {
        Job job;
        try {
            job = this.jobRegistry.getJob(jobName);
        } catch (final NoSuchJobException e) {
            log.info("Could not find job with jobName: " + jobName);
            job = null;
        }
        return job;
    }

    @Override
    public Collection<JobInstance> getJobInstances(final String jobName, final int startIndex, final int pageSize) {
        return this.jobExplorer.getJobInstances(jobName, startIndex, pageSize);
    }

    @Override
    public Collection<JobExecution> getJobExecutions(final JobInstance jobInstance) {
        final Collection<JobExecution> jobExecutions = new LinkedList<>();
        final List<JobExecution> jobExecutionList = this.jobExplorer.getJobExecutions(jobInstance);
        jobExecutions.addAll(jobExecutionList);
        return jobExecutions;
    }

    @Override
    public Collection<JobExecution> getJobExecutions(final JobInstance jobInstance, final int start, final int count) {
        final Collection<JobExecution> jobExecutions = new LinkedList<>();
        final List<JobExecution> jobExecutionList = this.lightminJobExecutionDao.findJobExecutions(jobInstance, start, count);
        jobExecutions.addAll(jobExecutionList);
        return jobExecutions;
    }

    @Override
    public JobExecution getJobExecution(final Long jobExecutionId) {
        return this.jobExplorer.getJobExecution(jobExecutionId);
    }

    @Override
    public JobInstance getJobInstance(final Long jobInstanceId) {
        return this.jobExplorer.getJobInstance(jobInstanceId);
    }

    @Override
    public void attachJobInstance(final JobExecution jobExecution) {
        if (jobExecution != null) {
            if (jobExecution.getJobInstance() != null) {
                final JobInstance jobInstance = this.jobExplorer.getJobInstance(jobExecution.getJobInstance().getId());
                jobExecution.setJobInstance(jobInstance);
            } else {
                throw new SpringBatchLightminApplicationException("JobInstance of JobExecution with id:" + jobExecution.getJobId() + "is null, cannot provide information");
            }
        } else {
            throw new SpringBatchLightminApplicationException("jobExecution is null, cannot provide information");
        }
    }

    @Override
    public void restartJobExecution(final Long jobExecutionId) {
        try {
            this.jobOperator.restart(jobExecutionId);
        } catch (final Exception e) {
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    public void stopJobExecution(final Long jobExecutionId) {
        try {
            this.jobOperator.stop(jobExecutionId);
        } catch (final Exception e) {
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    public JobParameters getLastJobParameters(final String jobName) {
        final List<JobExecution> executions = this.lightminJobExecutionDao.getJobExecutions(jobName, 0, 1);
        JobExecution lastExecution = null;
        if (!CollectionUtils.isEmpty(executions)) {
            lastExecution = executions.iterator().next();
        }
        JobParameters oldParameters = new JobParameters();
        if (lastExecution != null) {
            oldParameters = lastExecution.getJobParameters();
        }
        return oldParameters;
    }

    @Override
    public void afterPropertiesSet() {
        assert this.jobOperator != null;
        assert this.jobRegistry != null;
        assert this.jobExplorer != null;
    }
}
