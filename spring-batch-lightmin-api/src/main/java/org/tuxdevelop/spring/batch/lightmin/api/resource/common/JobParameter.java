package org.tuxdevelop.spring.batch.lightmin.api.resource.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class JobParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object parameter;
    private ParameterType parameterType;
}
