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

    private DataSource dataSource;
    private SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;

    @Autowired(required = false)
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setSpringBatchLightminConfigurationProperties(final SpringBatchLightminConfigurationProperties
                                                                      springBatchLightminConfigurationProperties) {
        this.springBatchLightminConfigurationProperties = springBatchLightminConfigurationProperties;
    }

    @Bean
    @ConditionalOnMissingBean(BatchConfigurer.class)
    public BatchConfigurer batchConfigurer() {
        final DefaultSpringBatchLightminBatchConfigurer batchConfigurer;
        if (dataSource != null && !springBatchLightminConfigurationProperties.getRepositoryForceMap()) {
            if (springBatchLightminConfigurationProperties.getRepositoryTablePrefix() != null &&
                    !springBatchLightminConfigurationProperties.getRepositoryTablePrefix().isEmpty()) {
                batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer(dataSource,
                        springBatchLightminConfigurationProperties.getRepositoryTablePrefix());
            } else {
                batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer(dataSource);
            }
        } else if (springBatchLightminConfigurationProperties.getRepositoryTablePrefix() != null &&
                !springBatchLightminConfigurationProperties.getRepositoryTablePrefix().isEmpty()) {
            batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer(
                    springBatchLightminConfigurationProperties.getRepositoryTablePrefix());
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
        if (dataSource != null) {
            configuration = new DefaultSpringBatchLightminConfigurator(dataSource, springBatchLightminConfigurationProperties);
            stringBuilder.append("with dataSource and repositoryTablePrefix: ");
            stringBuilder.append(springBatchLightminConfigurationProperties.getRepositoryTablePrefix());
        } else {
            configuration = new DefaultSpringBatchLightminConfigurator(springBatchLightminConfigurationProperties);
        }
        configuration.setBatchConfigurer(batchConfigurer);
        log.info(stringBuilder.toString());
        return configuration;
    }
}
