package org.tuxdevelop.spring.batch.lightmin.server.sample.application.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.tuxdevelop.spring.batch.lightmin.repository.server.configuration.EnableSpringBatchLightminRemoteRepositoryServer;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.EnableSpringBatchLightminServer;

import javax.sql.DataSource;

@Configuration
@EnableDiscoveryClient
@SpringBootApplication
@EnableSpringBatchLightminServer
@EnableSpringBatchLightminRemoteRepositoryServer
@PropertySource(value = "classpath:properties/sample/server/server.properties")
@ComponentScan(basePackages = "org.tuxdevelop.spring.batch.lightmin.server.sample.application.server")
public class LightminServer {


    public static void main(final String[] args) {
        SpringApplication.run(LightminServer.class, args);
    }

    @Bean
    public DataSource dataSource() {
        final EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        return embeddedDatabaseBuilder
                .addScript("classpath:org/tuxdevelop/spring/batch/lightmin/drop_schema_h2.sql")
                .addScript("classpath:org/tuxdevelop/spring/batch/lightmin/schema_h2.sql")
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
}
