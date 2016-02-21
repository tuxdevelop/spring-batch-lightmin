package org.tuxdevelop.spring.batch.lightmin.api.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Data
public class JobInstances implements Serializable {

    private int startIndex;
    private int pageSize;
    private String jobName;
    private List<JobInstance> jobInstances;
}
