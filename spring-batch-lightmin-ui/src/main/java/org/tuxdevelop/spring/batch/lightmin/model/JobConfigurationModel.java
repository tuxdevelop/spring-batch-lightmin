package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;

import java.util.Collection;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Data
public class JobConfigurationModel {

    private String jobName;
    private Collection<JobConfiguration> jobConfigurations;

}
