package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.test.configuration.ITMapConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ITMapConfiguration.class)
public class SpringBatchLightminConfigurationMapIT {

    @Autowired
    private SpringBatchLightminConfigurator configurator;

    @Test
    public void initMapIT() throws Exception {
        assertThat(this.configurator.getLightminJobExecutionDao()).isNotNull();
        assertThat(this.configurator.getJobOperator()).isNotNull();
        assertThat(this.configurator.getJobRegistry()).isNotNull();
        assertThat(this.configurator.getJobService()).isNotNull();
        assertThat(this.configurator.getStepService()).isNotNull();
    }

}
