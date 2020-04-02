package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.SchedulerConfigurationService;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.SchedulerExecutionService;

public class ServerSchedulerFeService {

    private final SchedulerExecutionService schedulerExecutionService;
    private final SchedulerConfigurationService schedulerConfigurationService;

    public ServerSchedulerFeService(final SchedulerExecutionService schedulerExecutionService,
                                    final SchedulerConfigurationService schedulerConfigurationService) {
        this.schedulerExecutionService = schedulerExecutionService;
        this.schedulerConfigurationService = schedulerConfigurationService;
    }
}
