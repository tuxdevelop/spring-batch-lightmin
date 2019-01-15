package org.tuxdevelop.spring.batch.lightmin.test.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.CleanUpRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.configuration.ServerSchedulerJdbcConfiguration;

import javax.sql.DataSource;

@Configuration
@Import(value = {ServerSchedulerJdbcConfiguration.class})
public class JdbcTestConfiguration {

    @Bean
    public DataSource dataSource() {
        final EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        return embeddedDatabaseBuilder
                .addScript("classpath:org/tuxdevelop/spring/batch/lightmin/server/scheduler/repository/drop_scheduler_schema_h2.sql")
                .addScript("classpath:org/tuxdevelop/spring/batch/lightmin/server/scheduler/repository/scheduler_schema_h2.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    public CleanUpRepository cleanUpRepository(
            @Qualifier("serverSchedulerJdbcTemplate") final JdbcTemplate serverSchedulerJdbcTemplate) {
        return new JdbcCleanUpRepository(serverSchedulerJdbcTemplate);
    }
}
