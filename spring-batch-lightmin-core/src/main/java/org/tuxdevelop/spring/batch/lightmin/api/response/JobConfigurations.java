package org.tuxdevelop.spring.batch.lightmin.api.response;

import java.io.Serializable;
import java.util.Collection;

import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;

import lombok.Data;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Data
public class JobConfigurations implements Serializable {

    private static final long serialVersionUID = 1L;

    private String jobName;
    private Collection<JobConfiguration> jobConfigurations;

}
