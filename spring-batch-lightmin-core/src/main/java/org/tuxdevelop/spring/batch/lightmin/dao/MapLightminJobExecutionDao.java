package org.tuxdevelop.spring.batch.lightmin.dao;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.repository.dao.MapJobExecutionDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MapLightminJobExecutionDao extends MapJobExecutionDao implements LightminJobExecutionDao {

    @Override
    public List<JobExecution> findJobExecutions(final JobInstance job, final int start, final int count) {
        final ArrayList result = new ArrayList(findJobExecutions(job));
        sortDescending(result);
        return subset(result, start, count);
    }

    @Override
    public int getJobExecutionCount(final JobInstance jobInstance) {
        final List<JobExecution> jobExecutions = findJobExecutions(jobInstance);
        return jobExecutions.size();
    }

    void sortDescending(final List<JobExecution> result) {
        Collections.sort(result, new Comparator<JobExecution>() {
            public int compare(final JobExecution o1, final JobExecution o2) {
                return Long.signum(o2.getId().longValue() - o1.getId().longValue());
            }
        });
    }

    List<JobExecution> subset(final List<JobExecution> jobExecutions, final int start, final int count) {
        int startIndex = Math.min(start, jobExecutions.size());
        int endIndex = Math.min(start + count, jobExecutions.size());
        return jobExecutions.subList(startIndex, endIndex);
    }
}
