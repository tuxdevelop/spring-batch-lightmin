package org.tuxdevelop.test.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.client.classic.annotation.EnableLightminClientClassic;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminMapConfigurationRepository;

@Configuration
@EnableLightminMapConfigurationRepository
@EnableLightminClientClassic
@Import(value = {ITJobConfiguration.class})
public class ITRemoteConfiguration {


}
