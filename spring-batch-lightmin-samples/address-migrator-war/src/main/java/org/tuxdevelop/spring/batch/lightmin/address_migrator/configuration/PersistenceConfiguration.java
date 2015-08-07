package org.tuxdevelop.spring.batch.lightmin.address_migrator.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JdbcJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JobConfigurationRepository;

import javax.sql.DataSource;

@Configuration
public class PersistenceConfiguration {

    @Value("${table.prefix:BATCH_}")
    private String tablePrefix;

    @Bean
    public DataSource dataSource() {
        final EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        return embeddedDatabaseBuilder.addScript("classpath:create.sql").addScript("classpath:schema_h2.sql")
                .addScript("classpath:inserts.sql").setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    public PlatformTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    @Bean
    public JobConfigurationRepository jobConfigurationRepository() {
        return new JdbcJobConfigurationRepository(jdbcTemplate(), tablePrefix);
    }
}
