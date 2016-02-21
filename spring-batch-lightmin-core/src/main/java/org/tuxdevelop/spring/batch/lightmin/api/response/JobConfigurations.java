package org.tuxdevelop.spring.batch.lightmin.api.response;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Data
public class JobConfigurations implements Serializable {

    private String jobName;
    private Collection<JobConfiguration> jobConfigurations;

}
