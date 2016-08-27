package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Data
public class JobInstancePage implements Serializable {

    private static final long serialVersionUID = 1L;

    private int startIndex;
    private int pageSize;
    private Integer totalJobInstanceCount;
    private String jobName;
    private List<JobInstance> jobInstances;
}
