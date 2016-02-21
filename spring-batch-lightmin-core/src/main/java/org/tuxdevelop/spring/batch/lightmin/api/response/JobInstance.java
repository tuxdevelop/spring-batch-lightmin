package org.tuxdevelop.spring.batch.lightmin.api.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class JobInstance implements Serializable {

    private Long id;
    private Integer version;
    private String jobName;
}
