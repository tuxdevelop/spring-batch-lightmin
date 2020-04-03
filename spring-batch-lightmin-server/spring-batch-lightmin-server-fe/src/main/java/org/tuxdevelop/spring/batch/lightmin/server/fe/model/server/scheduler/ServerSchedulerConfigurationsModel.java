package org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler;

import lombok.Data;

import java.util.*;

@Data
public class ServerSchedulerConfigurationsModel {

    private Set<String> registeredApplications = new HashSet<>();
    private Map<String, List<ServerSchedulerConfigurationModel>> schedulers = new HashMap<>();
}
