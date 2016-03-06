package org.tuxdevelop.spring.batch.lightmin;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.tuxdevelop.spring.batch.lightmin.configuration.EnableSpringBatchLightmin;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;

@Profile("mapOnly")
@Configuration
@EnableSpringBatchLightmin
@EnableConfigurationProperties(value = {SpringBatchLightminConfigurationProperties.class})
@PropertySource("classpath:properties/map.properties")
@Import(value = {ITJobConfiguration.class})
public class ITMapConfiguration {

}
