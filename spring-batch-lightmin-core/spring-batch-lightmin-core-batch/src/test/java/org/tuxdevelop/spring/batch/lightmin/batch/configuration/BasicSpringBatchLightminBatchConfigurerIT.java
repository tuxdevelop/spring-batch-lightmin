package org.tuxdevelop.spring.batch.lightmin.batch.configuration;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.test.configuration.ITPersistenceConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ITPersistenceConfiguration.class})
public class BasicSpringBatchLightminBatchConfigurerIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testBatchConfigurer() {
        final BatchConfigurer batchConfigurer = this.applicationContext.getBean(BatchConfigurer.class);
        BDDAssertions.then(batchConfigurer instanceof BasicSpringBatchLightminBatchConfigurer).isTrue();
    }
}
