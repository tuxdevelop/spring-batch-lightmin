package org.tuxdevelop.spring.batch.lightmin.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.beans.factory.InitializingBean;

import javax.batch.operations.JobOperator;
import java.util.*;

@Slf4j
public class JobServiceBean implements JobService, InitializingBean {

    private JobOperator jobOperator;
    private JobRegistry jobRegistry;
    private JobInstanceDao jobInstanceDao;
    private JobExecutionDao jobExecutionDao;

    public JobServiceBean(final JobOperator jobOperator, final JobRegistry jobRegistry, final JobInstanceDao
            jobInstanceDao, final JobExecutionDao jobExecutionDao) {
        this.jobOperator = jobOperator;
        this.jobRegistry = jobRegistry;
        this.jobInstanceDao = jobInstanceDao;
        this.jobInstanceDao = jobInstanceDao;
    }

    @Override
    public int getJobInstanceCount(final String jobName) {
        return jobOperator.getJobInstanceCount(jobName);
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
        } catch (NoSuchJobException e) {
            log.info("Could not find job with jobName: " + jobName);
            job = null;
        }
        return job;
    }

    @Override
    public Collection<JobInstance> getJobInstances(final String jobName, final int startIndex, final int pageSize) {
        return jobInstanceDao.getJobInstances(jobName, startIndex, pageSize);
    }

    public Collection<JobExecution> getJobExecutions(final Collection<JobInstance> jobInstances) {
        final Collection<JobExecution> jobExecutions = new LinkedList<JobExecution>();
        for(final JobInstance jobInstance : jobInstances) {
            final List<JobExecution> jobExecutionList = jobExecutionDao.findJobExecutions(jobInstance);
            jobExecutions.addAll(jobExecutionList);
        }
        return jobExecutions;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
