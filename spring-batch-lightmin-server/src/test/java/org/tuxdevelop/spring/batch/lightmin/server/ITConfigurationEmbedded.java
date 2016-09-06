package org.tuxdevelop.spring.batch.lightmin.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.EnableEmbeddedSpringBatchLightminServer;

@Configuration
@EnableEmbeddedSpringBatchLightminServer
@Import(value = {ITJobConfiguration.class})
@PropertySource(value = "classpath:application.properties")
public class ITConfigurationEmbedded {
}
