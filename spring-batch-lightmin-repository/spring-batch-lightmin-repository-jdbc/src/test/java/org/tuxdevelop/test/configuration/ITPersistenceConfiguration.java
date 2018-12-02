package org.tuxdevelop.test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminJdbcConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.configuration.JdbcJobConfigurationRepositoryConfigurationProperties;

import javax.sql.DataSource;

@Configuration
@EnableLightminJdbcConfigurationRepository
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ITPersistenceConfiguration {
    @Bean
    public DataSource dataSource() {
        final EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        return embeddedDatabaseBuilder
                .addScript("classpath:org/tuxdevelop/spring/batch/lightmin/repository/drop_schema_h2.sql")
                .addScript("classpath:org/tuxdevelop/spring/batch/lightmin/repository/schema_h2.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    public ITJdbcJobConfigurationRepository itJdbcJobConfigurationRepository(final JdbcTemplate jdbcTemplate,
                                                                             final PlatformTransactionManager dataSourceTransactionManager,
                                                                             final JdbcJobConfigurationRepositoryConfigurationProperties properties) {
        return new ITJdbcJobConfigurationRepository(jdbcTemplate, "BATCH_", dataSourceTransactionManager, properties);
    }

    @Bean
    public PlatformTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(this.dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(this.dataSource());
        return jdbcTemplate;
    }

}
