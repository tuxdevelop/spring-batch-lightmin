package org.tuxdevelop.spring.batch.lightmin.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.DiscoveryRemoteJobConfigurationRepositoryLocator;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.RemoteJobConfigurationRepositoryLocator;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.UrlRemoteJobConfigurationRepositoryLocator;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import javax.sql.DataSource;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Slf4j
@Configuration
@EnableBatchProcessing
public class SpringBatchLightminConfiguration {

    private ApplicationContext applicationContext;
    private SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;

    @Autowired
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setSpringBatchLightminConfigurationProperties(final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties) {
        this.springBatchLightminConfigurationProperties = springBatchLightminConfigurationProperties;
    }

    @Bean
    @ConditionalOnMissingBean(BatchConfigurer.class)
    public BatchConfigurer batchConfigurer() {
        final DefaultSpringBatchLightminBatchConfigurer batchConfigurer;
        final BatchRepositoryType batchRepositoryType = this.springBatchLightminConfigurationProperties.getBatchRepositoryType();
        switch (batchRepositoryType) {
            case JDBC:
                final DataSource dataSource = this.applicationContext.getBean(
                        this.springBatchLightminConfigurationProperties.getBatchDataSourceName(), DataSource.class);
                final String tablePrefix = this.springBatchLightminConfigurationProperties.getRepositoryTablePrefix();
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
        final DefaultSpringBatchLightminConfigurator configuration =
                new DefaultSpringBatchLightminConfigurator(this.springBatchLightminConfigurationProperties, this.applicationContext);
        configuration.setBatchConfigurer(batchConfigurer);
        return configuration;
    }

    @Configuration
    @ConditionalOnProperty(prefix = "spring.batch.lightmin", value = "lightmin-repository-type", havingValue = "remote")
    class RemoteJobConfigurationRepositoryConfiguration {

        private final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;

        @Autowired
        RemoteJobConfigurationRepositoryConfiguration(final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties) {
            this.springBatchLightminConfigurationProperties = springBatchLightminConfigurationProperties;
        }

        @Bean
        @ConditionalOnProperty(prefix = "spring.batch.lightmin", value = "discover-remote-repository", havingValue = "false", matchIfMissing = true)
        public RemoteJobConfigurationRepositoryLocator urlRemoteJobConfigurationRepositoryLocator() {
            return new UrlRemoteJobConfigurationRepositoryLocator(this.springBatchLightminConfigurationProperties);
        }

        @Bean
        @ConditionalOnProperty(prefix = "spring.batch.lightmin", value = "discover-remote-repository", havingValue = "true")
        public RemoteJobConfigurationRepositoryLocator discoveryRemoteJobConfigurationRepositoryLocator(final DiscoveryClient discoveryClient) {
            return new DiscoveryRemoteJobConfigurationRepositoryLocator(this.springBatchLightminConfigurationProperties, discoveryClient);
        }
    }

}
