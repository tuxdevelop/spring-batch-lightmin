package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfigurationTestHelper;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.test.configuration.SchedulerCoreITConfiguration;

import static org.assertj.core.api.Fail.fail;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SchedulerCoreITConfiguration.class})
public class SchedulerConfigurationServiceIT {

    @Autowired
    private SchedulerConfigurationService schedulerConfigurationService;
    @MockBean
    private JobServerService jobServerService;

    @Test
    public void testSave() {
        final SchedulerConfiguration schedulerConfiguration =
                SchedulerConfigurationTestHelper.createSchedulerConfiguration();

        final SchedulerConfiguration result = this.schedulerConfigurationService.save(schedulerConfiguration);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getId()).isNotNull();
    }

    @Test
    public void testDelete() {
        final SchedulerConfiguration schedulerConfiguration =
                SchedulerConfigurationTestHelper.createSchedulerConfiguration();

        final SchedulerConfiguration result = this.schedulerConfigurationService.save(schedulerConfiguration);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getId()).isNotNull();

        try {
            this.schedulerConfigurationService.findById(result.getId());
        } catch (final SchedulerConfigurationNotFoundException e) {
            fail(e.getMessage());
        }

        this.schedulerConfigurationService.delete(result);

        try {
            this.schedulerConfigurationService.findById(result.getId());
            fail("SchedulerConfiguration not deleted");
        } catch (final SchedulerConfigurationNotFoundException e) {
            log.debug("SchedulerConfiguration deleted, everything is fine");
        }

    }

}
