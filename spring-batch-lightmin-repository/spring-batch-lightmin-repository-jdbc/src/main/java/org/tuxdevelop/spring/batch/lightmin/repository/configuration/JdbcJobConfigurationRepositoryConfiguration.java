package org.tuxdevelop.spring.batch.lightmin.repository.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.tuxdevelop.spring.batch.lightmin.repository.JdbcJobConfigurationRepository;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableConfigurationProperties(value = {JdbcJobConfigurationRepositoryConfigurationProperties.class})
public class JdbcJobConfigurationRepositoryConfiguration extends LightminJobConfigurationRepositoryConfigurer {

    private final JdbcJobConfigurationRepositoryConfigurationProperties properties;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcJobConfigurationRepositoryConfiguration(
            final JdbcJobConfigurationRepositoryConfigurationProperties properties) {
        this.properties = properties;
    }

    protected void configureJdbcTemplate() {
        if (this.jdbcTemplate != null) {
            log.info("JdbcTemplate already configured for JdbcJobConfigurationRepository");
        } else {
            final DataSource dataSource =
                    this.getApplicationContext().getBean(this.properties.getDataSourceName(), DataSource.class);
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }
    }

    protected JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    protected void setJdbcTemplate(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected void configureJobConfigurationRepository() {
        this.configureJdbcTemplate();
        this.setJobConfigurationRepository(new JdbcJobConfigurationRepository(this.jdbcTemplate, this.properties));
    }
}
