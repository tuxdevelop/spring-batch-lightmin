package org.tuxdevelop.test.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.tuxdevelop.spring.batch.lightmin.annotation.EnableLightminCoreConfiguration;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminMapConfigurationRepository;

@Configuration
@EnableLightminCoreConfiguration
@EnableLightminMapConfigurationRepository
@Import(value = {ITSchedulerConfiguration.class, ITJobConfiguration.class})
@PropertySource(value = "classpath:properties/map.properties")
public class ITConfiguration {

}
