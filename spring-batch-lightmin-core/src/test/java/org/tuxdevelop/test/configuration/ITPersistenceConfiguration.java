package org.tuxdevelop.test.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.configuration.EnableSpringBatchLightmin;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITJdbcJobConfigurationRepository;

import javax.sql.DataSource;

@Configuration
@EnableSpringBatchLightmin
@EnableConfigurationProperties(SpringBatchLightminConfigurationProperties.class)
@PropertySource(value = "classpath:properties/jdbc.properties")
@Import(value = {ITSchedulerConfiguration.class, ITJobConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
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

    @Bean
    public ITJdbcJobConfigurationRepository itJdbcJobConfigurationRepository(final JdbcTemplate jdbcTemplate,
                                                                             final PlatformTransactionManager dataSourceTransactionManager,
                                                                             final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties) {
        return new ITJdbcJobConfigurationRepository(jdbcTemplate, "BATCH_", dataSourceTransactionManager, springBatchLightminConfigurationProperties);
    }
}
