package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Data
public class JobConfigurations implements Serializable {

    private static final long serialVersionUID = 1L;

    private String jobName;
    private Collection<JobConfiguration> jobConfigurations;

    public JobConfigurations() {
        this.jobConfigurations = new LinkedList<>();
    }

}
