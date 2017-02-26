package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.dao.JdbcLightminJobExecutionDao;
import org.tuxdevelop.test.configuration.ITPersistenceConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ITPersistenceConfiguration.class)
public class SpringBatchLightminConfigurationJdbcIT {

    @Autowired
    private SpringBatchLightminConfigurator configurator;

    @Test
    public void initJdbcIT() throws Exception {
        assertThat(this.configurator.getLightminJobExecutionDao()).isInstanceOf(JdbcLightminJobExecutionDao.class);
        assertThat(this.configurator.getRepositoryTablePrefix()).isEqualTo(AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX);
    }

}
