package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;


import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;

import jakarta.validation.Valid;
import java.io.Serializable;

/**
 * @author Marcel
 * @since 0.3
 */
@Data
public class JobConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long jobConfigurationId;
    private String jobName;
    @Valid
    private JobSchedulerConfiguration jobSchedulerConfiguration;
    @Valid
    private JobListenerConfiguration jobListenerConfiguration;
    private JobParameters jobParameters;
    private JobIncrementer jobIncrementer;

}
