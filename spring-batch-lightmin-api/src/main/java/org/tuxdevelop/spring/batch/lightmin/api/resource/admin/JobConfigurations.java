package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;

import lombok.Data;

import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Marcel Becker
 * @version 0.3
 */
@Data
public class JobConfigurations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Valid
    private Collection<JobConfiguration> jobConfigurations;

    public JobConfigurations() {
        this.jobConfigurations = new LinkedList<>();
    }

}
