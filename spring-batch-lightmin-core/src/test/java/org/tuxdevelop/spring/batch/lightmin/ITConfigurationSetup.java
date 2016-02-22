package org.tuxdevelop.spring.batch.lightmin;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(value = {SpringBatchLightminConfigurationProperties.class})
@PropertySource(value = "classpath:application.properties")
public class ITConfigurationSetup {


    @Bean
    @Profile(value = {"jdbc"})
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

    /*@Bean
    @Profile(value = "prefix")
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        final PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocation(new ClassPathResource("prefix.properties"));
        return propertyPlaceholderConfigurer;
    }

    @Bean
    @Profile(value = "no_prefix")
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurerEmpty() {
        final PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocation(new ClassPathResource("prefix_empty.properties"));
        return propertyPlaceholderConfigurer;
    } */
}
