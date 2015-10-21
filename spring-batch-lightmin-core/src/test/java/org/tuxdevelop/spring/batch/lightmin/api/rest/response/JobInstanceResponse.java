package org.tuxdevelop.spring.batch.lightmin.api.rest.response;

import lombok.Data;

@Data
public class JobInstanceResponse {

    private Long id;
    private Integer version;
    private String jobName;
}
