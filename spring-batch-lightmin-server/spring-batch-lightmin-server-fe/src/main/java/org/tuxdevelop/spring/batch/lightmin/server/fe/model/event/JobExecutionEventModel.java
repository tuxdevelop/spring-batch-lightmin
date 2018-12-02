package org.tuxdevelop.spring.batch.lightmin.server.fe.model.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.CommonExecutionModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class JobExecutionEventModel extends CommonExecutionModel {

    private String jobName;
    private String applicationName;
    private String applicationInstanceId;

}
