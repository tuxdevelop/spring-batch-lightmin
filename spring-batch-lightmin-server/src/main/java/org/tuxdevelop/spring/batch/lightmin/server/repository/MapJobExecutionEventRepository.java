package org.tuxdevelop.spring.batch.lightmin.server.repository;


import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;

import java.util.*;

@Slf4j
public class MapJobExecutionEventRepository implements JobExecutionEventRepository {

    private final Map<ExitStatus, Set<JobExecutionEventInfo>> store;
    private final Integer limit;

    public MapJobExecutionEventRepository(final Integer limit) {
        this.store = new HashMap<>();
        this.limit = limit;
    }

    @Override
    public void save(final JobExecutionEventInfo jobExecutionEventInfo) {
        final ExitStatus exitStatus = jobExecutionEventInfo.getExitStatus();
        if (this.store.containsKey(exitStatus)) {
            log.debug("JobExecutionEventInfo set for exit status {} is already present", exitStatus);
        } else {
            final Set<JobExecutionEventInfo> jobExecutionEventInfos = this.createJobExecutionEventInfoSet();
            this.store.put(exitStatus, jobExecutionEventInfos);
        }
        this.store.get(exitStatus).add(jobExecutionEventInfo);
    }

    @Override
    public List<JobExecutionEventInfo> findAll(final int start, final int count) {
        final List<JobExecutionEventInfo> result = new ArrayList<>();
        for (final Set<JobExecutionEventInfo> jobExecutionEventInfos : this.store.values()) {
            result.addAll(new ArrayList<>(jobExecutionEventInfos));
        }
        this.sortByStartDate(result);
        return this.subset(result, start, count);
    }

    @Override
    public List<JobExecutionEventInfo> finalByExitStatus(final ExitStatus exitStatus, final int start, final int count) {
        final Set<JobExecutionEventInfo> jobExecutionEventInfos = this.store.getOrDefault(exitStatus, new HashSet<>());
        final ArrayList<JobExecutionEventInfo> result = new ArrayList<>(jobExecutionEventInfos);
        this.sortByStartDate(result);
        return this.subset(result, start, count);
    }

    @Override
    public int getTotalCount() {
        int count = 0;
        if (!this.store.values().isEmpty()) {
            for (final Set<JobExecutionEventInfo> jobExecutionEventInfos : this.store.values()) {
                count += jobExecutionEventInfos.size();
            }
        } else {
            log.debug("Empty JobExecutionEventInfo store, nothing todo");
        }
        return count;
    }

    private void sortByStartDate(final List<JobExecutionEventInfo> jobExecutionEventInfos) {
        jobExecutionEventInfos.sort((jobExecutionEventInfo, jobExecutionEventInfoCompare)
                -> (jobExecutionEventInfoCompare.getStartDate()
                .compareTo(jobExecutionEventInfo.getStartDate())));
    }

    private Set<JobExecutionEventInfo> createJobExecutionEventInfoSet() {
        return Collections.newSetFromMap(new LinkedHashMap<JobExecutionEventInfo, Boolean>() {
            @Override
            protected boolean removeEldestEntry(final Map.Entry<JobExecutionEventInfo, Boolean> eldest) {
                return this.size() > MapJobExecutionEventRepository.this.limit;
            }
        });
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
}
