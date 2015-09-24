package org.tuxdevelop.spring.batch.lightmin.api.domain;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;

import java.util.Collection;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Data
public class JobConfigurations {

    private String jobName;
    private Collection<JobConfiguration> jobConfigurations;

}
