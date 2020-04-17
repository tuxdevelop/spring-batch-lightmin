package org.tuxdevelop.spring.batch.lightmin.test.configuration;

import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.configuration.InfinispanServerClusterConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;

@Configuration
@Import(value = {InfinispanServerClusterConfiguration.class})
public class InfinispanITConfiguration {

    @MockBean
    private JobServerService jobServerService;

    @Bean
    public GlobalConfiguration globalConfiguration() {
        return new GlobalConfigurationBuilder()
                .clusteredDefault()
                .build();
    }
}
