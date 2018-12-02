package org.tuxdevelop.spring.batch.lightmin.batch.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;

import java.util.*;

@Slf4j
public class MapLightminJobExecutionDao implements LightminJobExecutionDao {

    private final JobExplorer jobExplorer;

    public MapLightminJobExecutionDao(final JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }

    @Override
    public List<JobExecution> findJobExecutions(final JobInstance job, final int start, final int count) {
        final ArrayList<JobExecution> result = new ArrayList<>(this.jobExplorer.getJobExecutions(job));
        this.sortDescending(result);
        return this.subset(result, start, count);
    }

    @Override
    public int getJobExecutionCount(final JobInstance jobInstance) {
        final List<JobExecution> jobExecutions = this.jobExplorer.getJobExecutions(jobInstance);
        return jobExecutions.size();
    }

    @Override
    public List<JobExecution> getJobExecutions(final String jobName, final int start, final int count) {
        final List<JobExecution> jobExecutions = new LinkedList<>();

        int jobInstanceCount;
        try {
            jobInstanceCount = this.jobExplorer.getJobInstanceCount(jobName);
        } catch (final NoSuchJobException e) {
            jobInstanceCount = 0;
        }
        final List<JobInstance> jobInstances = this.jobExplorer.getJobInstances(jobName, 0, jobInstanceCount);
        for (final JobInstance jobInstance : jobInstances) {
            final List<JobExecution> jobExecutionsByInstance = this.jobExplorer.getJobExecutions(jobInstance);
            jobExecutions.addAll(jobExecutionsByInstance);
        }
        this.sortDescending(jobExecutions);
        return this.subset(jobExecutions, start, count);
    }

    @Override
    public List<JobExecution> findJobExecutions(final String jobName, final Map<String, Object> queryParameter, final Integer size) {
        final List<JobExecution> allJobExecutions;
        if (jobName == null) {
            final List<String> jobNames = this.jobExplorer.getJobNames();
            allJobExecutions = new ArrayList<>();
            for (final String jobNameInner : jobNames) {
                final List<JobExecution> jobExecutionsInner = this.getJobExecutions(jobNameInner, 0, -1);
                allJobExecutions.addAll(jobExecutionsInner);
            }
        } else {
            allJobExecutions = this.getJobExecutions(jobName, 0, -1);
        }
        final List<JobExecution> filteredJobExecutions = this.applyQueryParameter(allJobExecutions, queryParameter);
        this.sortDescending(filteredJobExecutions);
        return this.subset(filteredJobExecutions, 0, size);
    }

    private void sortDescending(final List<JobExecution> result) {
        result.sort((jobExecution, jobExecutionToCompare) -> Long.signum(jobExecutionToCompare.getId() - jobExecution.getId()));
    }

    private List<JobExecution> subset(final List<JobExecution> jobExecutions, final int start, final int count) {
        final int end = count > 0 ? start + count : jobExecutions.size();
        final int startIndex = Math.min(start, jobExecutions.size());
        final int endIndex = Math.min(end, jobExecutions.size());
        return jobExecutions.subList(startIndex, endIndex);
    }

    private List<JobExecution> applyQueryParameter(final List<JobExecution> allJobExecution, final Map<String, Object> queryParameter) {
        final List<JobExecution> filteredByExitStatus = this.applyExitStatusQueryParameter(allJobExecution, queryParameter);
        final List<JobExecution> filteredByStartDate = this.applyStartDateQueryParameter(filteredByExitStatus, queryParameter);
        return this.applyEndDateQueryParameter(filteredByStartDate, queryParameter);
    }

    private List<JobExecution> applyExitStatusQueryParameter(final List<JobExecution> allJobExecutions, final Map<String, Object> queryParamater) {
        final List<JobExecution> result;
        if (queryParamater.containsKey(QueryParameterKey.EXIT_STATUS)) {
            result = new ArrayList<>();
            final String exitStatus = queryParamater.get(QueryParameterKey.EXIT_STATUS).toString();
            for (final JobExecution allJobExecution : allJobExecutions) {
                if (allJobExecution.getExitStatus().getExitCode().equals(exitStatus)) {
                    result.add(allJobExecution);
                }
            }
        } else {
            log.debug("Could not apply exit status query, no value defined!");
            result = allJobExecutions;
        }
        return result;
    }

    private List<JobExecution> applyStartDateQueryParameter(final List<JobExecution> allJobExecutions, final Map<String, Object> queryParamater) {
        final List<JobExecution> result;
        if (queryParamater.containsKey(QueryParameterKey.START_DATE)) {
            result = new ArrayList<>();
            final Date startDate = DaoUtil.castDate(queryParamater.get(QueryParameterKey.START_DATE));
            for (final JobExecution jobExecution : allJobExecutions) {
                if (startDate.before(jobExecution.getStartTime())) {
                    result.add(jobExecution);
                }
            }
        } else {
            log.debug("Could not apply start date query, no value defined!");
            result = allJobExecutions;
        }
        return result;
    }

    private List<JobExecution> applyEndDateQueryParameter(final List<JobExecution> allJobExecutions, final Map<String, Object> queryParamater) {
        final List<JobExecution> result;
        if (queryParamater.containsKey(QueryParameterKey.END_DATE)) {
            result = new ArrayList<>();
            final Date endDate = DaoUtil.castDate(queryParamater.get(QueryParameterKey.END_DATE));
            for (final JobExecution jobExecution : allJobExecutions) {
                if (endDate.after(jobExecution.getEndTime())) {
                    result.add(jobExecution);
                }
            }
        } else {
            log.debug("Could not apply start date query, no value defined!");
            result = allJobExecutions;
        }
        return result;
    }
}
