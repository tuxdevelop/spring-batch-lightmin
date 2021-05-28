package org.tuxdevelop.spring.batch.lightmin.server.sample.application.client.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

//    @Bean
//    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(@Value("${spring.application.name}") final String name) {
//        return registry ->
//                registry.config()
//                        .commonTags("APPLICATION_NAME", name);
//    }
}
