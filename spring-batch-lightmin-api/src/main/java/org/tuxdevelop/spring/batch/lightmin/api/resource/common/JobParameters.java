package org.tuxdevelop.spring.batch.lightmin.api.resource.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class JobParameters implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, JobParameter> parameters;

    public JobParameters() {
        this.parameters = new HashMap<>();
    }

    public JobParameters(final Map<String, JobParameter> parameters) {
        this.parameters = parameters;
    }
}
