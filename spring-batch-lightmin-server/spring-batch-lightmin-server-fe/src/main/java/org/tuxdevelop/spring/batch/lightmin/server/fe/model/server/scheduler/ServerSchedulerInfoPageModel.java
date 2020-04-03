package org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler;

import lombok.Data;

import java.util.List;

@Data
public class ServerSchedulerInfoPageModel {

    private Integer filterState;
    private String displayFilterState;
    private List<ServerSchedulerInfoModel> schedulerInfos;

}
