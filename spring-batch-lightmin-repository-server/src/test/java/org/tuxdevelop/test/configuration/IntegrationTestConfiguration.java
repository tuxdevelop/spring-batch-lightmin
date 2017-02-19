package org.tuxdevelop.test.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.tuxdevelop.spring.batch.lightmin.repository.server.configuration.EnableSpringBatchLightminRemoteRepositoryServer;

@Configuration
@EnableSpringBatchLightminRemoteRepositoryServer
@PropertySource(value = {"classpath:application.properties"})
public class IntegrationTestConfiguration {


}
