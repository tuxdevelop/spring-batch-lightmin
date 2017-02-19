package org.tuxdevelop.spring.batch.lightmin.configuration;

import lombok.Data;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import javax.annotation.PostConstruct;

@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin")
public class SpringBatchLightminConfigurationProperties {

    @Autowired
    private Environment environment;

    private static final Boolean FORCE_MAP_DEFAULT = Boolean.FALSE;
    private static final String DEFAULT_DATA_SOURCE_NAME = "dataSource";

    @Deprecated
    private Boolean repositoryForceMap = FORCE_MAP_DEFAULT;
    @Deprecated
    private Boolean configurationForceMap = FORCE_MAP_DEFAULT;

    private String repositoryTablePrefix = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;
    private String configurationTablePrefix = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;

    private LightminRepositoryType lightminRepositoryType = LightminRepositoryType.JDBC;
    private BatchRepositoryType batchRepositoryType = BatchRepositoryType.JDBC;
    private String remoteRepositoryServerUrl;
    private String remoteRepositoryUsername;
    private String remoteRepositoryPassword;

    private String batchDataSourceName = DEFAULT_DATA_SOURCE_NAME;
    private String dataSourceName = DEFAULT_DATA_SOURCE_NAME;
    private String configurationDatabaseSchema;
    private String applicationName;


    public void setConfigurationDatabaseSchema(final String configurationDatabaseSchema) {
        if (configurationDatabaseSchema != null) {
            if (StringUtils.isEmpty(configurationDatabaseSchema)) {
                throw new SpringBatchLightminConfigurationException("configurationDatabaseSchema must not be empty!");
            }
        }
        this.configurationDatabaseSchema = configurationDatabaseSchema;
    }

    public void setRepositoryForceMap(final Boolean forceMap) {
        this.repositoryForceMap = forceMap;
        if (forceMap) {
            batchRepositoryType = BatchRepositoryType.MAP;
        } else {
            batchRepositoryType = BatchRepositoryType.JDBC;
        }
    }

    public void setConfigurationForceMap(final Boolean forceMap) {
        this.configurationForceMap = forceMap;
        if (forceMap) {
            lightminRepositoryType = LightminRepositoryType.MAP;
        } else {
            lightminRepositoryType = LightminRepositoryType.JDBC;
        }
    }

    @PostConstruct
    public void init() {
        if (!StringUtils.hasText(applicationName)) {
            applicationName = environment.getProperty("spring.application.name");
        }
        if(this.applicationName == null || applicationName.isEmpty()){
            throw new SpringBatchLightminConfigurationException("The property spring.batch.lightmin.application-name " +
                    "must not be null or empty. The value has to be set or spring.application.name has to be present!");
        }
    }
}
