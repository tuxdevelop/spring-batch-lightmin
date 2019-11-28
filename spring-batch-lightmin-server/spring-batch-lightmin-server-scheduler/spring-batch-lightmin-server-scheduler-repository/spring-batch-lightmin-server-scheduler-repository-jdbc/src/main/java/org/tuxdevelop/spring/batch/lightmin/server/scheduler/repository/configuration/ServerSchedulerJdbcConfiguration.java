package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.JdbcSchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.JdbcSchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(value = {ServerSchedulerJdbcConfigurationProperties.class})
public class ServerSchedulerJdbcConfiguration {

    private final ApplicationContext applicationContext;

    @Autowired
    public ServerSchedulerJdbcConfiguration(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean(name = "serverSchedulerJdbcTemplate")
    public JdbcTemplate serverSchedulerJdbcTemplate(final ServerSchedulerJdbcConfigurationProperties properties) {
        final DataSource dataSource = getDataSource(properties);
        return new JdbcTemplate(dataSource);
    }

    protected DataSource getDataSource(final ServerSchedulerJdbcConfigurationProperties properties) {
        return this.applicationContext.getBean(properties.getDatasourceName(), DataSource.class);
    }

    @Bean
    public SchedulerConfigurationRepository schedulerConfigurationRepository(
            @Qualifier("serverSchedulerJdbcTemplate") final JdbcTemplate serverSchedulerJdbcTemplate,
            final ServerSchedulerJdbcConfigurationProperties properties) {
        return new JdbcSchedulerConfigurationRepository(serverSchedulerJdbcTemplate, properties);
    }

    @Bean
    public SchedulerExecutionRepository schedulerExecutionRepository(
            @Qualifier("serverSchedulerJdbcTemplate") final JdbcTemplate serverSchedulerJdbcTemplate,
            final ServerSchedulerJdbcConfigurationProperties properties) throws Exception {
        return new JdbcSchedulerExecutionRepository(serverSchedulerJdbcTemplate, properties);
    }

    @Bean(name = "lightminServerSchedulerTransactionManager")
    public PlatformTransactionManager lightminServerSchedulerTransactionManager(
            final ServerSchedulerJdbcConfigurationProperties properties) {
        final DataSource dataSource = getDataSource(properties);
        return new DataSourceTransactionManager(dataSource);
    }
}
