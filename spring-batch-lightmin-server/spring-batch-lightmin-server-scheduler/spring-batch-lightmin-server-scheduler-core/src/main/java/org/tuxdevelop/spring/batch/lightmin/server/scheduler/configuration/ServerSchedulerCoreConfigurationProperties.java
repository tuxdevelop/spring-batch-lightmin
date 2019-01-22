package org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin.server.scheduler")
public class ServerSchedulerCoreConfigurationProperties {

    private Integer threadPoolSize = 30;
    private Integer threadPoolCoreSize = 10;
    private Integer pollerPeriod = 1000;
    private Integer pollerPeriodRetry = 10000;
}