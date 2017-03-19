package org.tuxdevelop.spring.batch.lightmin.repository.server.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.configuration.LightminRepositoryType;

/**
 * @author Marcel Becker
 * @since 0.4
 */
@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin.remote.repository.server")
public class SpringBatchLightminRemoteRepositoryConfigurationProperties {

    private LightminRepositoryType lightminRepositoryType = LightminRepositoryType.JDBC;
    //Lightmin Tables
    private String jobConfigurationTableName = "BATCH_JOB_CONFIGURATION";
    private String jobConfigurationValueTableName = "BATCH_JOB_CONFIGURATION_VALUE";
    private String jobConfigurationParameterTableName = "BATCH_JOB_CONFIGURATION_PARAMETERS";
    private String dataSourceName = "dataSource";
    private String databaseSchema;


}
