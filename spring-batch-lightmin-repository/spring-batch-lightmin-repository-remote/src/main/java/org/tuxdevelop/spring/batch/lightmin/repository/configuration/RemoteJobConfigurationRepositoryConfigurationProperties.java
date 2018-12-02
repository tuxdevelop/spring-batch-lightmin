package org.tuxdevelop.spring.batch.lightmin.repository.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin.repository.remote")
public class RemoteJobConfigurationRepositoryConfigurationProperties {

    //Remote Repository
    private String serverUrl;
    private String username;
    private String password;
    private Boolean discoverRemoteRepository = Boolean.FALSE;
    private String serverDiscoveryName = "spring-batch-lightmin-repository-server";
    private String contextPath;
    private Integer serverStartupDiscoveryRetry = 30;
    private Long serverStartupDiscoveryRetryWaitTime = 500L;

}
