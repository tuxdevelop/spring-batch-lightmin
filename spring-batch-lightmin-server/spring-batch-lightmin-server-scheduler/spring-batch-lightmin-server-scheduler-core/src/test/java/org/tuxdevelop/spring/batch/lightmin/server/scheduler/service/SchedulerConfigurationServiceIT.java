package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.CleanUpRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.*;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.test.configuration.SchedulerCoreITConfiguration;

import java.util.List;

import static org.assertj.core.api.Fail.fail;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SchedulerCoreITConfiguration.class})
public class SchedulerConfigurationServiceIT extends CommonServiceIT {

    @Autowired
    private SchedulerConfigurationService schedulerConfigurationService;

    @Autowired
    private SchedulerExecutionService schedulerExecutionService;

    @Autowired
    private CleanUpRepository cleanUpRepository;

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

    @Test(expected = SchedulerValidationException.class)
    public void testSaveNull() {
        this.schedulerConfigurationService.save(null);
    }


    @Test
    public void testDelete() {
        final SchedulerConfiguration schedulerConfiguration =
                SchedulerConfigurationTestHelper.createSchedulerConfiguration();
        schedulerConfiguration.setStatus(ServerSchedulerStatus.STOPPED);
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

    @Test
    public void testDeleteValidationConfigurationStatus() {
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
        BDDAssertions.assertThatThrownBy(() -> this.schedulerConfigurationService.delete(result))
                .isInstanceOf(SchedulerValidationException.class)
                .hasMessageContaining("schedulerConfiguration must be STOPPED");
        try {
            this.schedulerConfigurationService.findById(result.getId());
            log.debug("SchedulerConfiguration not deleted");
        } catch (final SchedulerConfigurationNotFoundException e) {
            fail("SchedulerConfiguration was deleted. If there is a Validation Error the entry shouldn't be deleted.");
        }
    }

    @Test
    public void testDeleteValidationExecutionStatus() {
        final SchedulerConfiguration schedulerConfiguration =
                SchedulerConfigurationTestHelper.createSchedulerConfiguration();
        schedulerConfiguration.setStatus(ServerSchedulerStatus.STOPPED);
        SchedulerExecution schedulerExecution = SchedulerExecutionTestHelper.createSchedulerExecution();
        schedulerExecution.setState(ExecutionStatus.RUNNING);
        final SchedulerConfiguration result = this.schedulerConfigurationService.save(schedulerConfiguration);
        schedulerExecution.setSchedulerConfigurationId(result.getId());
        this.schedulerExecutionService.save(schedulerExecution);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getId()).isNotNull();

        try {
            this.schedulerConfigurationService.findById(result.getId());
        } catch (final SchedulerConfigurationNotFoundException e) {
            fail(e.getMessage());
        }
        BDDAssertions.assertThatThrownBy(() -> this.schedulerConfigurationService.delete(result))
                .isInstanceOf(SchedulerValidationException.class)
                .hasMessageContaining("schedulerExecution is RUNNING")
                .overridingErrorMessage("The Server Configuration has an Exectuion which is RUNNING. Running Executions should be stopped or finished before deleting the configuration.");
        try {
            this.schedulerConfigurationService.findById(result.getId());
            log.debug("SchedulerConfiguration not deleted");
        } catch (final SchedulerConfigurationNotFoundException e) {
            fail("SchedulerConfiguration was deleted. If there is a Validation Error the entry shouldn't be deleted.");
        }
    }


    @Test(expected = SchedulerValidationException.class)
    public void testDeleteNull() {
        this.schedulerConfigurationService.delete(null);
    }

    @Test
    public void testFindAll() {
        final SchedulerConfiguration config1 = SchedulerConfigurationTestHelper.createSchedulerConfiguration("test", "test-app");
        final SchedulerConfiguration config2 = SchedulerConfigurationTestHelper.createSchedulerConfiguration("test", "test-app");

        final SchedulerConfiguration savedConfig1 = this.schedulerConfigurationService.save(config1);
        final SchedulerConfiguration savedConfig2 = this.schedulerConfigurationService.save(config2);


        final List<SchedulerConfiguration> found = this.schedulerConfigurationService.findAll();
        BDDAssertions.then(found).hasSize(2);
        BDDAssertions.then(found).contains(savedConfig1);
        BDDAssertions.then(found).contains(savedConfig2);
    }

    @Test
    public void testFindById() {
        final SchedulerConfiguration config1 = SchedulerConfigurationTestHelper.createSchedulerConfiguration("test", "test-app");
        final SchedulerConfiguration savedConfig1 = this.schedulerConfigurationService.save(config1);

        try {
            final SchedulerConfiguration found = this.schedulerConfigurationService.findById(savedConfig1.getId());
            BDDAssertions.then(found).isEqualTo(savedConfig1);
        } catch (final SchedulerConfigurationNotFoundException e) {
            fail(e.getMessage());
        }

    }

    @Override
    protected CleanUpRepository cleanUpRepository() {
        return this.cleanUpRepository;
    }
}
