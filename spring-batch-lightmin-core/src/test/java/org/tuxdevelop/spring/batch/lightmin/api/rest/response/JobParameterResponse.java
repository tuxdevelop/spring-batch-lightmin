package org.tuxdevelop.spring.batch.lightmin.api.rest.response;

import lombok.Data;
import org.springframework.batch.core.JobParameter;

@Data
public class JobParameterResponse {

    private Object parameter;
    private JobParameter.ParameterType parameterType;
    private boolean identifying;
}
