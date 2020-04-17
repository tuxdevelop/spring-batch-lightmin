package org.tuxdevelop.spring.batch.lightmin.server.repository;


import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;

import java.util.Collections;
import java.util.Map;

@Slf4j
public class MapJobExecutionEventRepository extends KVJobExecutionEventRepository<Map<Long, JobExecutionEventInfo>> {

    public MapJobExecutionEventRepository(final Integer limit) {
        super(Collections.synchronizedMap(new LimitedLinkedHashMap<>(limit)));
    }


}
