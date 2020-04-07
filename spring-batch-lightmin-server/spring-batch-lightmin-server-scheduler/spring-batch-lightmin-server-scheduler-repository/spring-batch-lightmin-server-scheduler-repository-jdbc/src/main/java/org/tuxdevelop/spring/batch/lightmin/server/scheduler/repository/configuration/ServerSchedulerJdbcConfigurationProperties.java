package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin.server.scheduler.repository.jdbc")
public class ServerSchedulerJdbcConfigurationProperties {

    private String configurationTable = "SCHEDULER_CONFIGURATION";
    private String configurationValueTable = "SCHEDULER_CONFIGURATION_VALUE";
    private String executionTable = "SCHEDULER_EXECUTION";
    private String databaseSchema;
    private String datasourceName = "dataSource";

}
