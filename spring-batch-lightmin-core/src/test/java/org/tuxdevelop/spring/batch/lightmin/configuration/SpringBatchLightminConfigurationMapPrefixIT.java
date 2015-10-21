package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.repository.dao.MapJobExecutionDao;
import org.springframework.batch.core.repository.dao.MapJobInstanceDao;
import org.springframework.batch.core.repository.dao.MapStepExecutionDao;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.ITConfigurationSetup;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("prefix")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringBatchLightminConfiguration.class, ITConfigurationSetup.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SpringBatchLightminConfigurationMapPrefixIT {

    @Autowired
    private SpringBatchLightminConfigurator configurator;

    @Test
    public void initMapIT() throws Exception {
        assertThat(configurator.getJobExecutionDao()).isInstanceOf(MapJobExecutionDao.class);
        assertThat(configurator.getJobInstanceDao()).isInstanceOf(MapJobInstanceDao.class);
        assertThat(configurator.getStepExecutionDao()).isInstanceOf(MapStepExecutionDao.class);
        assertThat(configurator.getTransactionManager()).isInstanceOf(ResourcelessTransactionManager.class);
        assertThat(configurator.getJobExplorer()).isNotNull();
        assertThat(configurator.getLightminJobExecutionDao()).isNotNull();
        assertThat(configurator.getTablePrefix()).isEqualTo("IT_");
    }

}
