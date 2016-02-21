package org.tuxdevelop.spring.batch.lightmin.api.response;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class JobParameters implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, JobParameter> parameters;
}
