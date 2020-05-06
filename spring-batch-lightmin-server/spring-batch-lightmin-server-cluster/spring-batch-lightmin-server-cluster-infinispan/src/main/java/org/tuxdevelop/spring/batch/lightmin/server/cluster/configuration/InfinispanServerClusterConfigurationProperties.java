package org.tuxdevelop.spring.batch.lightmin.server.cluster.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin.server.cluster.infinispan")
public class InfinispanServerClusterConfigurationProperties {

    @NestedConfigurationProperty
    private RepositoryConfigurationProperties repository = new RepositoryConfigurationProperties();

    @Data
    static class RepositoryConfigurationProperties {
        private Boolean initSchedulerExecutionRepository = Boolean.FALSE;
        private Boolean initSchedulerConfigurationRepository = Boolean.FALSE;
    }

}
