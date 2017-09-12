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
        if (store.containsKey(exitStatus)) {
            log.debug("JobExecutionEventInfo set for exit status {} is already present", exitStatus);
        } else {
            final Set<JobExecutionEventInfo> jobExecutionEventInfos = createJobExecutionEventInfoSet();
            store.put(exitStatus, jobExecutionEventInfos);
        }
        store.get(exitStatus).add(jobExecutionEventInfo);
    }

    @Override
    public List<JobExecutionEventInfo> findAll() {
        final List<JobExecutionEventInfo> result = new ArrayList<>();
        for (final Set<JobExecutionEventInfo> jobExecutionEventInfos : store.values()) {
            result.addAll(new ArrayList<>(jobExecutionEventInfos));
        }
        sortByStartDate(result);
        return result;
    }

    @Override
    public List<JobExecutionEventInfo> finalByExitStatus(final ExitStatus exitStatus) {
        final Set<JobExecutionEventInfo> jobExecutionEventInfos = store.getOrDefault(exitStatus, new HashSet<>());
        return new ArrayList<>(jobExecutionEventInfos);
    }

    private void sortByStartDate(final List<JobExecutionEventInfo> jobExecutionEventInfos) {
        jobExecutionEventInfos.sort((jobExecutionEventInfo, jobExecutionEventInfoCompare)
                -> (jobExecutionEventInfo.getStartDate()
                .compareTo(jobExecutionEventInfoCompare.getStartDate())));
    }

    private Set<JobExecutionEventInfo> createJobExecutionEventInfoSet() {
        return Collections.newSetFromMap(new LinkedHashMap<JobExecutionEventInfo, Boolean>() {
            @Override
            protected boolean removeEldestEntry(final Map.Entry<JobExecutionEventInfo, Boolean> eldest) {
                return size() > limit;
            }
        });
    }
}
