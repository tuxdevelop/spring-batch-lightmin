package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;

import java.util.Date;

@Data
public class JobExecutionEventModel {

    private String jobName;
    private Long jobExecutionId;
    private Date startTime;
    private Date endTime;
    private String applicationName;
    private ExitStatus exitStatus;

}
