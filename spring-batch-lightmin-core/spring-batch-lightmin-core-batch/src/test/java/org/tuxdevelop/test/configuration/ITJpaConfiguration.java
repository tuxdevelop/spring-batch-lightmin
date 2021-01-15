package org.tuxdevelop.test.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.batch.annotation.EnableLightminBatchJpa;

@Configuration
@EnableLightminBatchJpa
@EnableAutoConfiguration
@Import(value = {ITPersistenceConfiguration.class, ITJobConfiguration.class})
public class ITJpaConfiguration {
}
