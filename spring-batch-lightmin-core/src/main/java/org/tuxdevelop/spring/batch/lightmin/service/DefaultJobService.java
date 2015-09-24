package org.tuxdevelop.spring.batch.lightmin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;

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
    private final JobInstanceDao jobInstanceDao;
    private final JobExecutionDao jobExecutionDao;

    public DefaultJobService(final JobOperator jobOperator, final JobRegistry jobRegistry,
                             final JobInstanceDao jobInstanceDao, final JobExecutionDao jobExecutionDao) {
        this.jobOperator = jobOperator;
        this.jobRegistry = jobRegistry;
        this.jobInstanceDao = jobInstanceDao;
        this.jobExecutionDao = jobExecutionDao;
    }

    @Override
    public int getJobInstanceCount(final String jobName) {
        int jobInstanceCount = 0;
        try {
            jobInstanceCount = jobInstanceDao.getJobInstanceCount(jobName);
        } catch (final NoSuchJobException e) {
            log.info(e.getMessage(), e);
        }
        return jobInstanceCount;
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
        return jobInstanceDao.getJobInstances(jobName, startIndex, pageSize);
    }

    @Override
    public Collection<JobExecution> getJobExecutions(final JobInstance jobInstance) {
        final Collection<JobExecution> jobExecutions = new LinkedList<JobExecution>();
        final List<JobExecution> jobExecutionList = jobExecutionDao.findJobExecutions(jobInstance);
        jobExecutions.addAll(jobExecutionList);
        return jobExecutions;
    }

    @Override
    public JobExecution getJobExecution(final Long jobExecutionId) {
        return jobExecutionDao.getJobExecution(jobExecutionId);
    }

    @Override
    public JobInstance getJobInstance(final Long jobInstanceId) {
        return jobInstanceDao.getJobInstance(jobInstanceId);
    }

    @Override
    public void attachJobInstance(final JobExecution jobExecution) {
        final JobInstance jobInstance = jobInstanceDao.getJobInstance(jobExecution);
        jobExecution.setJobInstance(jobInstance);
    }

    @Override
    public void afterPropertiesSet() {
        assert jobOperator != null;
        assert jobRegistry != null;
        assert jobInstanceDao != null;
        assert jobExecutionDao != null;
    }
}
