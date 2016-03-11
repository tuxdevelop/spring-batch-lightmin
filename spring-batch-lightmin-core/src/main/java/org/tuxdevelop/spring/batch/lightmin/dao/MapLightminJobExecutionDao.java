package org.tuxdevelop.spring.batch.lightmin.dao;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;

import java.util.*;

public class MapLightminJobExecutionDao implements LightminJobExecutionDao {

    private final JobExplorer jobExplorer;

    public MapLightminJobExecutionDao(final JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }

    @Override
    public List<JobExecution> findJobExecutions(final JobInstance job, final int start, final int count) {
        final ArrayList<JobExecution> result = new ArrayList<JobExecution>(jobExplorer.getJobExecutions(job));
        sortDescending(result);
        return subset(result, start, count);
    }

    @Override
    public int getJobExecutionCount(final JobInstance jobInstance) {
        final List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
        return jobExecutions.size();
    }

    @Override
    public List<JobExecution> getJobExecutions(final String jobName, final int start, final int count) {
        final List<JobExecution> jobExecutions = new LinkedList<JobExecution>();

        int jobInstanceCount;
        try {
            jobInstanceCount = jobExplorer.getJobInstanceCount(jobName);
        } catch (final NoSuchJobException e) {
            jobInstanceCount = 0;
        }
        final List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0, jobInstanceCount);
        for (final JobInstance jobInstance : jobInstances) {
            final List<JobExecution> jobExecutionsByInstance = jobExplorer.getJobExecutions(jobInstance);
            jobExecutions.addAll(jobExecutionsByInstance);
        }
        sortDescending(jobExecutions);
        return subset(jobExecutions, start, count);
    }

    void sortDescending(final List<JobExecution> result) {
        Collections.sort(result, new Comparator<JobExecution>() {
            @Override
            public int compare(final JobExecution jobExecution, final JobExecution jobExecutionToCompare) {
                return Long.signum(jobExecutionToCompare.getId().longValue() - jobExecution.getId().longValue());
            }
        });
    }

    List<JobExecution> subset(final List<JobExecution> jobExecutions, final int start, final int count) {
        final int startIndex = Math.min(start, jobExecutions.size());
        final int endIndex = Math.min(start + count, jobExecutions.size());
        return jobExecutions.subList(startIndex, endIndex);
    }
}
