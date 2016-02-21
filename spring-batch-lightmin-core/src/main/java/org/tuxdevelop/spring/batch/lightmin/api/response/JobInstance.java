package org.tuxdevelop.spring.batch.lightmin.api.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class JobInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer version;
    private String jobName;
}
