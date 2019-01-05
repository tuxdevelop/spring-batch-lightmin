package org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.CommonExecutionModel;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StepExecutionModel extends CommonExecutionModel {

    private String stepName;
    private int readCount;
    private int writeCount;
    private int commitCount;
    private int rollbackCount;
    private int readSkipCount;
    private int processSkipCount;
    private int writeSkipCount;
}
