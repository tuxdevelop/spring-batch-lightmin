package org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.CommonExecutionModel;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobExecutionModel extends CommonExecutionModel {

    private Long instanceId;
    private Date createTime;

}
