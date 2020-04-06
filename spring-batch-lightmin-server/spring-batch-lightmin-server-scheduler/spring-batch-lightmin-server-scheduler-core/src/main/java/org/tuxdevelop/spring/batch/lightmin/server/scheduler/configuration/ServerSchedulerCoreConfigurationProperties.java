package org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin.server.scheduler")
public class ServerSchedulerCoreConfigurationProperties {

    private Boolean enabled = Boolean.TRUE;
    private Integer threadPoolSize = 30;
    private Integer threadPoolCoreSize = 10;
    private Integer pollerPeriod = 1000;
    private Integer pollerPeriodRetry = 10000;
    private Boolean failOnInstanceExecutionCount = Boolean.TRUE;
    private Boolean createNewExecutionsOnFailure = Boolean.FALSE;
    private Boolean createNewExecutionsOnLost = Boolean.FALSE;
}
