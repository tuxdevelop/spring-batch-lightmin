package org.tuxdevelop.test.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminMapConfigurationRepository;

@Configuration
@EnableLightminMapConfigurationRepository
@Import(value = {ITJobConfiguration.class})
public class ITEmbeddedConfiguration {


}
