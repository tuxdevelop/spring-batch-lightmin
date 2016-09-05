package org.tuxdevelop.spring.batch.lightmin.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import javax.sql.DataSource;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Slf4j
@Configuration
public class SpringBatchLightminConfiguration {

    private ApplicationContext applicationContext;
    private SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;

    @Autowired
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
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
        final BatchRepositoryType batchRepositoryType = springBatchLightminConfigurationProperties.getBatchRepositoryType();
        switch (batchRepositoryType) {
            case JDBC:
                final DataSource dataSource = applicationContext.getBean(
                        springBatchLightminConfigurationProperties.getBatchDataSourceName(), DataSource.class);
                final String tablePrefix = springBatchLightminConfigurationProperties.getRepositoryTablePrefix();
                batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer(dataSource, tablePrefix);
                break;
            case MAP:
                batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer();
                break;
            default:
                throw new SpringBatchLightminConfigurationException("Unknown BatchRepositoryType: " + batchRepositoryType);

        }
        return batchConfigurer;
    }


    @Bean
    @ConditionalOnMissingBean(SpringBatchLightminConfigurator.class)
    public SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator(final BatchConfigurer batchConfigurer) {
        final DefaultSpringBatchLightminConfigurator configuration = new DefaultSpringBatchLightminConfigurator(springBatchLightminConfigurationProperties, applicationContext);
        configuration.setBatchConfigurer(batchConfigurer);
        return configuration;
    }
}
