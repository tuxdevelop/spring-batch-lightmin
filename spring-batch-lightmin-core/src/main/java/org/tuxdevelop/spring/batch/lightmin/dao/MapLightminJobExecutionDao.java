package org.tuxdevelop.spring.batch.lightmin.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;

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
