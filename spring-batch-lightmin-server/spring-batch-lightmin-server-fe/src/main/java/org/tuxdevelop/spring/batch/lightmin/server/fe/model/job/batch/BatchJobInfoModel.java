package org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch;

import lombok.Data;

import java.io.Serializable;


@Data
public class BatchJobInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private String jobName;
    private Integer instanceCount;

}
