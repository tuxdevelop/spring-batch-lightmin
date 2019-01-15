package org.tuxdevelop.spring.batch.lightmin.server.scheduler.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(value = {ServerSchedulerCoreConfigurationProperties.class})
public class ServerSchedulerCoreConfiguration {

}
