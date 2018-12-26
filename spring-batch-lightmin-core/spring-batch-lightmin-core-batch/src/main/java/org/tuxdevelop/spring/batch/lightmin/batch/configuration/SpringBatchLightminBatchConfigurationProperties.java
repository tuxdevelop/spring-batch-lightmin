package org.tuxdevelop.spring.batch.lightmin.batch.configuration;

import lombok.Data;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin.batch")
public class SpringBatchLightminBatchConfigurationProperties {


    private static final String DEFAULT_DATA_SOURCE_NAME = "dataSource";

    private final Environment environment;

    @Autowired
    public SpringBatchLightminBatchConfigurationProperties(final Environment environment) {
        this.environment = environment;
    }

    //Table Prefix
    private String tablePrefix = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;
    //Repository
    private BatchRepositoryType repositoryType = BatchRepositoryType.JDBC;
    //Spring Batch Datasource
    private String dataSourceName = DEFAULT_DATA_SOURCE_NAME;
    private String databaseSchema;

    public void setDatabaseSchema(final String configurationDatabaseSchema) {
        if (configurationDatabaseSchema != null) {
            if (StringUtils.isEmpty(configurationDatabaseSchema)) {
                throw new SpringBatchLightminConfigurationException("databaseSchema must not be empty!");
            }
        }
        this.databaseSchema = configurationDatabaseSchema;
    }
}
