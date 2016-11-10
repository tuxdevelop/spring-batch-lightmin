package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.dao.JdbcLightminJobExecutionDao;
import org.tuxdevelop.test.configuration.ITPersistenceConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITPersistenceConfiguration.class)
public class SpringBatchLightminConfigurationJdbcIT {

    @Autowired
    private SpringBatchLightminConfigurator configurator;

    @Test
    public void initJdbcIT() throws Exception {
        assertThat(configurator.getLightminJobExecutionDao()).isInstanceOf(JdbcLightminJobExecutionDao.class);
        assertThat(configurator.getRepositoryTablePrefix()).isEqualTo(AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX);
    }

}
