package org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.CommonExecutionModel;

import java.util.Date;

@Data
public class JobExecutionModel extends CommonExecutionModel {

    private Long instanceId;
    private Date createTime;

}
