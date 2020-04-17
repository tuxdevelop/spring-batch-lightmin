package org.tuxdevelop.spring.batch.lightmin.server.repository;


import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class KVJobExecutionEventRepository<T extends Map<Long, JobExecutionEventInfo>> implements JobExecutionEventRepository {

    private final T store;

    public KVJobExecutionEventRepository(final T store) {
        this.store = store;
    }

    @Override
    public synchronized JobExecutionEventInfo save(final JobExecutionEventInfo jobExecutionEventInfo) {
        this.store.put(jobExecutionEventInfo.getJobExecutionId(), jobExecutionEventInfo);
        return jobExecutionEventInfo;
    }

    @Override
    public List<JobExecutionEventInfo> findAll(final int start, final int count) {
        final List<JobExecutionEventInfo> result = new ArrayList<>(this.store.values());
        this.sortByStartDate(result);
        return this.subset(result, start, count);
    }

    @Override
    public List<JobExecutionEventInfo> finalByExitStatus(final ExitStatus exitStatus, final int start, final int count) {
        final List<JobExecutionEventInfo> result = this.filterByExitStatus(exitStatus);
        this.sortByStartDate(result);
        return this.subset(result, start, count);
    }

    @Override
    public int getTotalCount() {
        final int count;
        if (!this.store.values().isEmpty()) {
            count = this.store.size();
        } else {
            log.debug("Empty JobExecutionEventInfo store, nothing todo");
            count = 0;
        }
        return count;
    }

    @Override
    public void clear() {
        this.store.clear();
    }

    private void sortByStartDate(final List<JobExecutionEventInfo> jobExecutionEventInfos) {
        if (jobExecutionEventInfos != null && !jobExecutionEventInfos.isEmpty()) {
            jobExecutionEventInfos.sort((jobExecutionEventInfo, jobExecutionEventInfoCompare) -> {
                final int result;
                if (jobExecutionEventInfoCompare.getStartDate() != null) {
                    if (jobExecutionEventInfo.getStartDate() != null) {
                        result = jobExecutionEventInfoCompare.getStartDate()
                                .compareTo(jobExecutionEventInfo.getStartDate());
                    } else {
                        result = 1;
                    }
                } else {
                    result = -1;
                }
                return result;
            });
        } else {
            log.debug("Skip sorting for a null or empty list");
        }
    }

    private List<JobExecutionEventInfo> subset(
            final List<JobExecutionEventInfo> jobExecutionEventInfos,
            final int start,
            final int count) {
        final int end = count > 0 ? start + count : jobExecutionEventInfos.size();
        final int startIndex = Math.min(start, jobExecutionEventInfos.size());
        final int endIndex = Math.min(end, jobExecutionEventInfos.size());
        return jobExecutionEventInfos.subList(startIndex, endIndex);
    }

    private List<JobExecutionEventInfo> filterByExitStatus(final ExitStatus exitStatus) {
        final List<JobExecutionEventInfo> jobExecutionEventInfos = new ArrayList<>();
        for (final JobExecutionEventInfo jobExecutionEventInfo : this.store.values()) {
            if (jobExecutionEventInfo.getExitStatus().getExitCode().equals(exitStatus.getExitCode())) {
                jobExecutionEventInfos.add(jobExecutionEventInfo);
            } else {
                log.debug("skipping to add, ExitStatus does not match");
            }
        }
        return jobExecutionEventInfos;
    }

}
