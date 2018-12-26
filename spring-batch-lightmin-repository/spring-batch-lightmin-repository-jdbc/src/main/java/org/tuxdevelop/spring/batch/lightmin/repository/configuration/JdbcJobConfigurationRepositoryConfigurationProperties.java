package org.tuxdevelop.spring.batch.lightmin.repository.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

@Data
@Slf4j
@ConfigurationProperties(prefix = "spring.batch.lightmin.repository.jdbc")
public class JdbcJobConfigurationRepositoryConfigurationProperties {

    private static final String DEFAULT_DATA_SOURCE_NAME = "dataSource";

    //Lightmin Tables
    private String jobConfigurationTableName = "BATCH_JOB_CONFIGURATION";
    private String jobConfigurationValueTableName = "BATCH_JOB_CONFIGURATION_VALUE";
    private String jobConfigurationParameterTableName = "BATCH_JOB_CONFIGURATION_PARAMETERS";
    //Optional Database schema name
    private String configurationDatabaseSchema;
    //dataSource name
    private String dataSourceName = DEFAULT_DATA_SOURCE_NAME;

    public void setConfigurationDatabaseSchema(final String configurationDatabaseSchema) {
        if (configurationDatabaseSchema != null) {
            if (StringUtils.isEmpty(configurationDatabaseSchema)) {
                throw new SpringBatchLightminConfigurationException("configurationDatabaseSchema must not be empty!");
            } else {
                log.info(
                        "Setting database schema {} for Spring Batch Lightmin Jdbc Repository ", configurationDatabaseSchema);
            }
        } else {
            log.info("Database schema for Spring Batch Lightmin Jdbc Repository is null, skipping");
        }
        this.configurationDatabaseSchema = configurationDatabaseSchema;
    }
}
