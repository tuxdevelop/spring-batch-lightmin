package org.tuxdevelop.test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.tuxdevelop.spring.batch.lightmin.annotation.EnableLightminService;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminMapConfigurationRepository;

@Configuration
@EnableLightminService
@EnableLightminMapConfigurationRepository
@Import(value = {ITSchedulerConfiguration.class, ITJobConfiguration.class})
@PropertySource(value = "classpath:properties/map.properties")
public class ITConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
