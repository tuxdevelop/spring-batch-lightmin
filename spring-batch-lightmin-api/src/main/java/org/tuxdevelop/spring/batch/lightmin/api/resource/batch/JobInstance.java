package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Marcel Becker
 * @Since 0.3
 */
@Data
public class JobInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer version;
    private String jobName;
}
