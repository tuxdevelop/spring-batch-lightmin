package org.tuxdevelop.spring.batch.lightmin.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class LightminMetricsConfiguration {


    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(@Value("${spring.batch.lightmin.application-name:null}") final String applicationName, final Environment environment) {
        final String appName;
        if (applicationName == null) {
            appName = environment.getProperty("spring.application.name", "spring-boot-application");
        } else {
            appName = applicationName;
        }

        return registry -> {
            registry.config().commonTags("lightmin-app-name", appName);
        };
    }
}
