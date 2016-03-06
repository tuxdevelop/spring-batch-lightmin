package org.tuxdevelop.spring.batch.lightmin.configuration;

import lombok.Data;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.batch.lightmin")
public class SpringBatchLightminConfigurationProperties {

    private static final Boolean FORCE_MAP_DEFAULT = Boolean.FALSE;

    private Boolean repositoryForceMap = FORCE_MAP_DEFAULT;
    private Boolean configurationForceMap = FORCE_MAP_DEFAULT;
    private String repositoryTablePrefix = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;
    private String configurationTablePrefix = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;

}
