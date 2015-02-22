package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;

import java.util.Collection;

@Data
public class JobConfigurationModel {

    private String jobName;
    private Collection<JobConfiguration> jobConfigurations;

}
