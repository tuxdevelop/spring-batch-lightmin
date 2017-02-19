package org.tuxdevelop.spring.batch.lightmin.repository.server.configuration;

import lombok.Data;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
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
    private String jdbcTablePrefix = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;
    private String dataSourceName = "dataSource";
    private String databaseSchema;


}
