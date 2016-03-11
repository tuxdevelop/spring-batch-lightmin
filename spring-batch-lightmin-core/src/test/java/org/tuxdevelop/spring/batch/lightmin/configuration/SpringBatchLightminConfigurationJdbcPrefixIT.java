package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.ITPersistenceConfiguration;
import org.tuxdevelop.spring.batch.lightmin.dao.JdbcLightminJobExecutionDao;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITPersistenceConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SpringBatchLightminConfigurationJdbcPrefixIT {

    @Autowired
    private SpringBatchLightminConfigurator configurator;

    @Test
    public void initJdbcIT() throws Exception {
        assertThat(configurator.getLightminJobExecutionDao()).isInstanceOf(JdbcLightminJobExecutionDao.class);
        assertThat(configurator.getRepositoryTablePrefix()).isEqualTo("BATCH_");
    }

}
