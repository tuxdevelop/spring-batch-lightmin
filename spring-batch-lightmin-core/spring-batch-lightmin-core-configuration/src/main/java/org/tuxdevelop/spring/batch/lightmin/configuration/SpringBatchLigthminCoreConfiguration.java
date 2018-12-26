package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.util.BeanRegistrar;

@Configuration
@EnableConfigurationProperties(value = {SpringBatchLightminCoreConfigurationProperties.class})
public class SpringBatchLigthminCoreConfiguration {


    @Bean
    public BeanRegistrar beanRegistrar(final ConfigurableApplicationContext context) {
        return new BeanRegistrar(context);
    }

}
