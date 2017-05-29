package org.tuxdevelop.spring.batch.lightmin.server.repository;


import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;

import java.util.*;

public class MapJobExecutionEventRepository implements JobExecutionEventRepository {

    private final Set<JobExecutionEventInfo> store;

    public MapJobExecutionEventRepository(final Integer limit) {
        this.store = Collections.newSetFromMap(new LinkedHashMap<JobExecutionEventInfo, Boolean>() {
            @Override
            protected boolean removeEldestEntry(final Map.Entry<JobExecutionEventInfo, Boolean> eldest) {
                return size() > limit;
            }
        });
    }

    @Override
    public void save(final JobExecutionEventInfo jobExecutionEventInfo) {
        this.store.add(jobExecutionEventInfo);
    }

    @Override
    public List<JobExecutionEventInfo> findAll() {
        final List<JobExecutionEventInfo> result = new ArrayList<>(this.store);
        sortByStartDate(result);
        return result;
    }

    private void sortByStartDate(final List<JobExecutionEventInfo> jobExecutionEventInfos) {
        jobExecutionEventInfos.sort((jobExecutionEventInfo, jobExecutionEventInfoCompare)
                -> (jobExecutionEventInfo.getStartDate()
                .compareTo(jobExecutionEventInfoCompare.getStartDate())));
    }
}
