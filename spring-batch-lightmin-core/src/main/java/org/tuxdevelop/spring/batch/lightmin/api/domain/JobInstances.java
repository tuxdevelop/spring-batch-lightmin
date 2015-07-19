package org.tuxdevelop.spring.batch.lightmin.api.domain;

import lombok.Data;
import org.springframework.batch.core.JobInstance;

import java.util.Collection;

@Data
public class JobInstances {

    private int startIndex;
    private int pageSize;
    private String jobName;
    private Collection<JobInstance> jobInstances;
}
