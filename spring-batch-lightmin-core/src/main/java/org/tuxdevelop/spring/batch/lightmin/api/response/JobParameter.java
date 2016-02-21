package org.tuxdevelop.spring.batch.lightmin.api.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class JobParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object parameter;
    private org.springframework.batch.core.JobParameter.ParameterType parameterType;
    private boolean identifying;
}
