package org.tuxdevelop.spring.batch.lightmin.api.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class JobParameter implements Serializable {

    private Object parameter;
    private org.springframework.batch.core.JobParameter.ParameterType parameterType;
    private boolean identifying;
}
