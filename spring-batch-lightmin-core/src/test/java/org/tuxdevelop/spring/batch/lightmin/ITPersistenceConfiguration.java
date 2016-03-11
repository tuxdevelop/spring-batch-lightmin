package org.tuxdevelop.spring.batch.lightmin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.configuration.EnableSpringBatchLightmin;

import javax.sql.DataSource;

@Configuration
@EnableSpringBatchLightmin
@PropertySource(value = "classpath:properties/jdbc.properties")
public class ITPersistenceConfiguration {
    @Bean
    public DataSource dataSource() {
        final EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        return embeddedDatabaseBuilder.addScript("classpath:create.sql")
                .addScript("classpath:schema_h2.sql").setType(EmbeddedDatabaseType.H2).build();
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
}
