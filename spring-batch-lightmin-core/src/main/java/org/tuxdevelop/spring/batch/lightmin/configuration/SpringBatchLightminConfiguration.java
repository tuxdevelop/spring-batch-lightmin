package org.tuxdevelop.spring.batch.lightmin.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Slf4j
@Configuration
@ConditionalOnMissingBean(SpringBatchLightminConfigurator.class)
public class SpringBatchLightminConfiguration {

    @Autowired(required = false)
    private DataSource dataSource;

    @Value("${table.prefix:BATCH_}")
    private String tablePrefix;


    @Bean
    public SpringBatchLightminConfigurator defaultSpringBatchLightminConfigurator() {
        final DefaultSpringBatchLightminConfigurator configuration;
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Create DefaultSpringBatchLightminConfiguration ");
        if (dataSource != null) {
            if (tablePrefix != null) {
                configuration = new DefaultSpringBatchLightminConfigurator(dataSource, tablePrefix);
                stringBuilder.append("with dataSource and tablePrefix: ");
                stringBuilder.append(tablePrefix);
            } else {
                configuration = new DefaultSpringBatchLightminConfigurator(dataSource);
                stringBuilder.append("with dataSource");
            }
        } else if (tablePrefix != null) {
            configuration = new DefaultSpringBatchLightminConfigurator(tablePrefix);
            stringBuilder.append("with tablePrefix: ");
            stringBuilder.append(tablePrefix);
        } else {
            configuration = new DefaultSpringBatchLightminConfigurator();
        }
        log.info(stringBuilder.toString());
        return configuration;
    }
}
