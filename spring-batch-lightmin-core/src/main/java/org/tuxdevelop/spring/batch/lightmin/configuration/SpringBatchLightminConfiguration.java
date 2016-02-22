package org.tuxdevelop.spring.batch.lightmin.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Slf4j
@Configuration
public class SpringBatchLightminConfiguration {

    /*
     *TODO: refactor to apply constructor injection (0.2)
     */
    @Autowired(required = false)
    private DataSource dataSource;

    @Autowired
    private SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;

    @Bean
    @ConditionalOnMissingBean(BatchConfigurer.class)
    public BatchConfigurer batchConfigurer() {
        final DefaultSpringBatchLightminBatchConfigurer batchConfigurer;
        if (dataSource != null && !springBatchLightminConfigurationProperties.getRepositoryForceMap()) {
            if (springBatchLightminConfigurationProperties.getTablePrefix() != null &&
                    !springBatchLightminConfigurationProperties.getTablePrefix().isEmpty()) {
                batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer(dataSource,
                        springBatchLightminConfigurationProperties.getTablePrefix());
            } else {
                batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer(dataSource);
            }
        } else if (springBatchLightminConfigurationProperties.getTablePrefix() != null &&
                !springBatchLightminConfigurationProperties.getTablePrefix().isEmpty()) {
            batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer(
                    springBatchLightminConfigurationProperties.getTablePrefix());
        } else {
            batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer();
        }

        return batchConfigurer;
    }


    @Bean
    @ConditionalOnMissingBean(SpringBatchLightminConfigurator.class)
    public SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator(final BatchConfigurer batchConfigurer) {
        final DefaultSpringBatchLightminConfigurator configuration;
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Create DefaultSpringBatchLightminConfigurator ");
        if (dataSource != null && !springBatchLightminConfigurationProperties.getConfigurationForceMap()) {
            if (springBatchLightminConfigurationProperties.getTablePrefix() != null &&
                    !springBatchLightminConfigurationProperties.getTablePrefix().isEmpty()) {
                configuration = new DefaultSpringBatchLightminConfigurator(dataSource,
                        springBatchLightminConfigurationProperties.getTablePrefix());
                stringBuilder.append("with dataSource and tablePrefix: ");
                stringBuilder.append(springBatchLightminConfigurationProperties.getTablePrefix());
            } else {
                configuration = new DefaultSpringBatchLightminConfigurator(dataSource);
                stringBuilder.append("with dataSource");
            }
        } else if (springBatchLightminConfigurationProperties.getTablePrefix() != null &&
                !springBatchLightminConfigurationProperties.getTablePrefix().isEmpty()) {
            configuration = new DefaultSpringBatchLightminConfigurator(
                    springBatchLightminConfigurationProperties.getTablePrefix());
            stringBuilder.append("with tablePrefix: ");
            stringBuilder.append(springBatchLightminConfigurationProperties.getTablePrefix());
        } else {
            configuration = new DefaultSpringBatchLightminConfigurator();
        }
        configuration.setBatchConfigurer(batchConfigurer);
        log.info(stringBuilder.toString());
        return configuration;
    }
}
