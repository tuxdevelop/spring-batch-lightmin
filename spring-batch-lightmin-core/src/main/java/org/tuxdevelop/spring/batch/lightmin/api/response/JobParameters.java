package org.tuxdevelop.spring.batch.lightmin.api.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class JobParameters implements Serializable {

    private Map<String, JobParameter> parameters;
}
