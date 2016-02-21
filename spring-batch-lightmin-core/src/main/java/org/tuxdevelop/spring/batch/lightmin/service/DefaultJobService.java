package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.tuxdevelop.spring.batch.lightmin.dao.LightminJobExecutionDao;
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
            jobInstanceCount = jobExplorer.getJobInstanceCount(jobName);
        } catch (final NoSuchJobException e) {
            log.info(e.getMessage(), e);
        }
        return jobInstanceCount;
    }

    @Override
    public int getJobExecutionCount(final JobInstance jobInstance) {
        final int jobExecutionCount;
        jobExecutionCount = lightminJobExecutionDao.getJobExecutionCount(jobInstance);

        return jobExecutionCount;
    }

    @Override
    public Set<String> getJobNames() {
        return new HashSet<String>(jobRegistry.getJobNames());
    }

    @Override
    public Job getJobByName(final String jobName) {
        Job job;
        try {
            job = jobRegistry.getJob(jobName);
        } catch (final NoSuchJobException e) {
            log.info("Could not find job with jobName: " + jobName);
            job = null;
        }
        return job;
    }

    @Override
    public Collection<JobInstance> getJobInstances(final String jobName, final int startIndex, final int pageSize) {
        return jobExplorer.getJobInstances(jobName, startIndex, pageSize);
    }

    @Override
    public Collection<JobExecution> getJobExecutions(final JobInstance jobInstance) {
        final Collection<JobExecution> jobExecutions = new LinkedList<JobExecution>();
        final List<JobExecution> jobExecutionList = jobExplorer.getJobExecutions(jobInstance);
        jobExecutions.addAll(jobExecutionList);
        return jobExecutions;
    }

    @Override
    public Collection<JobExecution> getJobExecutions(final JobInstance jobInstance, final int start, final int count) {
        final Collection<JobExecution> jobExecutions = new LinkedList<JobExecution>();
        final List<JobExecution> jobExecutionList = lightminJobExecutionDao.findJobExecutions(jobInstance, start, count);
        jobExecutions.addAll(jobExecutionList);
        return jobExecutions;
    }

    @Override
    public JobExecution getJobExecution(final Long jobExecutionId) {
        return jobExplorer.getJobExecution(jobExecutionId);
    }

    @Override
    public JobInstance getJobInstance(final Long jobInstanceId) {
        return jobExplorer.getJobInstance(jobInstanceId);
    }

    @Override
    public void attachJobInstance(final JobExecution jobExecution) {
        final JobInstance jobInstance = jobExplorer.getJobInstance(jobExecution.getJobInstance().getId());
        jobExecution.setJobInstance(jobInstance);
    }

    @Override
    public void restartJobExecution(final Long jobExecutionId) {
        try {
            jobOperator.restart(jobExecutionId);
        } catch (final Exception e) {
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    public void stopJobExecution(final Long jobExecutionId) {
        try {
            jobOperator.stop(jobExecutionId);
        } catch (final Exception e) {
            throw new SpringBatchLightminApplicationException(e, e.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() {
        assert jobOperator != null;
        assert jobRegistry != null;
        assert jobExplorer != null;
    }
}
