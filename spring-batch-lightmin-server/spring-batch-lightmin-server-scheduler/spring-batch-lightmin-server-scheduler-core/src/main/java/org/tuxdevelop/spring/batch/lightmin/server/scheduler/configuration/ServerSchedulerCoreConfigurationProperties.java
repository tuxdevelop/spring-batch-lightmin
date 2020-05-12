package org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin.server.scheduler")
public class ServerSchedulerCoreConfigurationProperties {

    private Boolean enabled = Boolean.TRUE;
    private Integer threadPoolSize = 30;
    private Integer threadPoolCoreSize = 10;
    private Integer pollerPeriod = 1000;
    private Integer pollerPeriodRetry = 1000;
    private Boolean failOnInstanceExecutionCount = Boolean.TRUE;
    private Boolean createNewExecutionsOnFailure = Boolean.FALSE;
    private Boolean createNewExecutionsOnLost = Boolean.FALSE;
    @NestedConfigurationProperty
    private RepositoryProperties repository = new RepositoryProperties();

    @Data
    public static class RepositoryProperties {

        private Duration deletePollerPeriod = Duration.ofMinutes(10);

        private Boolean deleteFinished = Boolean.TRUE;
        private Boolean deleteFailed = Boolean.TRUE;
        private Boolean deleteLost = Boolean.TRUE;

        private Duration keepFinished = Duration.ofHours(12);
        private Duration keepFailed = Duration.ofHours(24);
        private Duration keepLost = Duration.ofHours(24);
    }
}
