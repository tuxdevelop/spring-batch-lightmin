package org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Data
public class JobExecutionEventInfo implements Serializable {

    private static final long serialVersionUID = -1L;
    private String applicationName;
    private String jobName;
    private Long jobExecutionId;
    private ExitStatus exitStatus;
    private Date startDate;
    private Date endDate;

}
