package org.tuxdevelop.test.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;

@Configuration
@EnableConfigurationProperties(value = {SpringBatchLightminConfigurationProperties.class})
@Import(value = {ITSchedulerConfiguration.class, ITJobConfiguration.class})
@PropertySource(value = "classpath:properties/map.properties")
public class ITConfiguration {

}
