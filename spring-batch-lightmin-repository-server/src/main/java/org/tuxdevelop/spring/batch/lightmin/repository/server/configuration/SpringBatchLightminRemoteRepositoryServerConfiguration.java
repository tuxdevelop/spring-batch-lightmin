package org.tuxdevelop.spring.batch.lightmin.repository.server.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JdbcJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.MapJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.configuration.LightminRepositoryType;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.repository.server.api.controller.RepositoryController;

import javax.sql.DataSource;


/**
 * @author Marcel Becker
 * @since 0.4
 */
@Configuration
@EnableConfigurationProperties(value = {SpringBatchLightminRemoteRepositoryConfigurationProperties.class})
public class SpringBatchLightminRemoteRepositoryServerConfiguration {


    @Bean
    public RepositoryController repositoryController(@Qualifier("localJobConfigurationRepository") final JobConfigurationRepository localJobConfigurationRepository) {
        return new RepositoryController(localJobConfigurationRepository);
    }


    @Bean
    @Qualifier("localJobConfigurationRepository")
    public JobConfigurationRepository localJobConfigurationRepository(final SpringBatchLightminRemoteRepositoryConfigurationProperties springBatchLightminRemoteRepositoryConfigurationProperties,
                                                                      final ApplicationContext applicationContext) {
        final LightminRepositoryType lightminRepositoryType = springBatchLightminRemoteRepositoryConfigurationProperties.getLightminRepositoryType();
        if (lightminRepositoryType == null) {
            throw new SpringBatchLightminConfigurationException("LightminRepositoryType must not be null for Remote Repository");
        }
        final JobConfigurationRepository jobConfigurationRepository;
        switch (lightminRepositoryType) {
            case MAP:
                jobConfigurationRepository = createMapRepository();
                break;
            case JDBC:
                jobConfigurationRepository = createJdbcRepository(springBatchLightminRemoteRepositoryConfigurationProperties, applicationContext);
                break;
            case REMOTE:
            default:
                throw new SpringBatchLightminConfigurationException("Unknown or Unsupported LightminRepositoryType " + lightminRepositoryType + " for Remote Repository");
        }
        return jobConfigurationRepository;
    }

    private JobConfigurationRepository createJdbcRepository(final SpringBatchLightminRemoteRepositoryConfigurationProperties springBatchLightminRemoteRepositoryConfigurationProperties,
                                                            final ApplicationContext applicationContext) {
        final JdbcTemplate jdbcTemplate = createJdbcTemplate(springBatchLightminRemoteRepositoryConfigurationProperties, applicationContext);
        return new JdbcJobConfigurationRepository(jdbcTemplate,
                springBatchLightminRemoteRepositoryConfigurationProperties.getJobConfigurationTableName(),
                springBatchLightminRemoteRepositoryConfigurationProperties.getJobConfigurationValueTableName(),
                springBatchLightminRemoteRepositoryConfigurationProperties.getJobConfigurationParameterTableName(),
                springBatchLightminRemoteRepositoryConfigurationProperties.getDatabaseSchema());
    }

    private JobConfigurationRepository createMapRepository() {
        return new MapJobConfigurationRepository();
    }

    private JdbcTemplate createJdbcTemplate(final SpringBatchLightminRemoteRepositoryConfigurationProperties springBatchLightminRemoteRepositoryConfigurationProperties,
                                            final ApplicationContext applicationContext) {
        final String dataSourceName = springBatchLightminRemoteRepositoryConfigurationProperties.getDataSourceName();
        final DataSource dataSource = applicationContext.getBean(dataSourceName, DataSource.class);
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }


}
