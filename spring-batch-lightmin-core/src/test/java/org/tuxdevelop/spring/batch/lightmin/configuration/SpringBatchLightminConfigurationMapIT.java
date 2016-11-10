package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.test.configuration.ITMapConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITMapConfiguration.class)
public class SpringBatchLightminConfigurationMapIT {

    @Autowired
    private SpringBatchLightminConfigurator configurator;

    @Test
    public void initMapIT() throws Exception {
        assertThat(configurator.getLightminJobExecutionDao()).isNotNull();
        assertThat(configurator.getJobOperator()).isNotNull();
        assertThat(configurator.getJobRegistry()).isNotNull();
        assertThat(configurator.getJobService()).isNotNull();
        assertThat(configurator.getStepService()).isNotNull();
    }

}
