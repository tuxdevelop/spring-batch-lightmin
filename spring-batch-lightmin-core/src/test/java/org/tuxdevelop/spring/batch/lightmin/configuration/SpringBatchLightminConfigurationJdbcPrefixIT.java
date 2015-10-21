package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.repository.dao.JdbcJobExecutionDao;
import org.springframework.batch.core.repository.dao.JdbcJobInstanceDao;
import org.springframework.batch.core.repository.dao.JdbcStepExecutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.ITConfigurationSetup;
import org.tuxdevelop.spring.batch.lightmin.dao.JdbcLightminJobExecutionDao;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles(value = {"jdbc", "prefix"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringBatchLightminConfiguration.class, ITConfigurationSetup.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SpringBatchLightminConfigurationJdbcPrefixIT {

    @Autowired
    private SpringBatchLightminConfigurator configurator;

    @Test
    public void initJdbcIT() throws Exception {
        assertThat(configurator.getJobExplorer()).isInstanceOf(SimpleJobExplorer.class);
        assertThat(configurator.getJobExecutionDao()).isInstanceOf(JdbcJobExecutionDao.class);
        assertThat(configurator.getLightminJobExecutionDao()).isInstanceOf(JdbcLightminJobExecutionDao.class);
        assertThat(configurator.getJobInstanceDao()).isInstanceOf(JdbcJobInstanceDao.class);
        assertThat(configurator.getStepExecutionDao()).isInstanceOf(JdbcStepExecutionDao.class);
        assertThat(configurator.getTransactionManager()).isInstanceOf(DataSourceTransactionManager.class);
        assertThat(configurator.getTablePrefix()).isEqualTo("IT_");
    }

}
