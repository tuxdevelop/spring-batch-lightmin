package org.tuxdevelop.test.configuration;

import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.repository.server.configuration.EnableSpringBatchLightminRemoteRepositoryServer;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITJdbcJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITJobConfigurationRepository;

import javax.sql.DataSource;

@Configuration
@EnableSpringBatchLightminRemoteRepositoryServer
@PropertySource(value = {"classpath:it_properties/jdbc_configuration.properties"})
@EnableConfigurationProperties(value = {SpringBatchLightminConfigurationProperties.class})
public class JdbcIntegrationTestConfiguration {

    @Bean
    public ITJobConfigurationRepository itJobConfigurationRepository(final JdbcTemplate jdbcTemplate,
                                                                     final PlatformTransactionManager platformTransactionManager,
                                                                     final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties) {
        return new ITJdbcJobConfigurationRepository(jdbcTemplate, AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX,
                platformTransactionManager, springBatchLightminConfigurationProperties);
    }

    @Bean
    public DataSource dataSource() {
        final EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        embeddedDatabaseBuilder.setType(EmbeddedDatabaseType.H2);
        embeddedDatabaseBuilder.addScript("classpath:org/tuxdevelop/spring/batch/lightmin/schema_h2.sql");
        return embeddedDatabaseBuilder.build();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(final DataSource dataSource) {
        final DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        dataSourceTransactionManager.afterPropertiesSet();
        return dataSourceTransactionManager;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }


}
