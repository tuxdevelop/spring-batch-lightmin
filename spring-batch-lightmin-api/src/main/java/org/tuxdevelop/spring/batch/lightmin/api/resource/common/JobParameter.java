package org.tuxdevelop.spring.batch.lightmin.api.resource.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Marcel Becker
 * @Since 0.3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object parameter;
    private ParameterType parameterType;
}
