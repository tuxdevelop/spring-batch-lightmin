package org.tuxdevelop.spring.batch.lightmin.server.sample.application.server;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.repository.annotation.EnableLightminJdbcConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.server.configuration.EnableLightminRepositoryServer;
import org.tuxdevelop.spring.batch.lightmin.server.annotation.EnableLightminServer;

import javax.sql.DataSource;

@Configuration
@EnableDiscoveryClient
@SpringBootApplication
@EnableLightminServer
@EnableLightminJdbcConfigurationRepository
@EnableLightminRepositoryServer
@PropertySource(value = "classpath:properties/sample/server/server.properties")
public class LightminServer {


    public static void main(final String[] args) {
        SpringApplication.run(LightminServer.class, args);
    }

    @Bean
    public DataSource dataSource() {
        final EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        return embeddedDatabaseBuilder
                .addScript("classpath:org/tuxdevelop/spring/batch/lightmin/repository/drop_schema_h2.sql")
                .addScript("classpath:org/tuxdevelop/spring/batch/lightmin/repository/schema_h2.sql")
                .addScript("classpath:properties/sample/server/sql/inserts.sql")
                .setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    public PlatformTransactionManager dataSourceTransactionManager(final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

//    @Bean
//    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(@Value("${spring.application.name}") final String name) {
//        return registry ->
//                registry.config()
//                        .commonTags("APPLICATION_NAME", name);
//    }
}
