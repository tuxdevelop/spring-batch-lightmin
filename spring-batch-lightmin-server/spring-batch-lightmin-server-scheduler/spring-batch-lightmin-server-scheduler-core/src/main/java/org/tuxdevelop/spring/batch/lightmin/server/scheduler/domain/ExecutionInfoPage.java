package org.tuxdevelop.spring.batch.lightmin.server.scheduler.domain;

import lombok.Data;

import java.util.List;

@Data
public class ExecutionInfoPage {

    private int startIndex;
    private int pageSize;
    private Integer status;
    private Integer totalCount;
    private List<ExecutionInfo> executionInfos;
}
