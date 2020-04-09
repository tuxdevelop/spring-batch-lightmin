package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.exception.SchedulerRuntimException;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.CleanUpRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ExecutionStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecutionTestHelper;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerValidationException;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.test.configuration.SchedulerCoreITConfiguration;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Fail.fail;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SchedulerCoreITConfiguration.class})
public class SchedulerExecutionServiceIT extends CommonServiceIT {

    @Autowired
    private SchedulerExecutionService schedulerExecutionService;
    @Autowired
    private CleanUpRepository cleanUpRepository;

    @MockBean
    private JobServerService jobServerService;

    @Test
    public void testSave() {

        final SchedulerExecution schedulerExecution = this.getNewSchedulerExecution();
        final SchedulerExecution result = this.schedulerExecutionService.save(schedulerExecution);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getId()).isNotNull();

    }

    @Test(expected = SchedulerValidationException.class)
    public void testSaveNull() {
        this.schedulerExecutionService.save(null);
    }

    @Test
    public void testCreateNextExecution() {
        final SchedulerExecution schedulerExecution = SchedulerExecutionTestHelper.createSchedulerExecution();
        final String cron = "0 0/2 0 ? * * ";
        final List<SchedulerExecution> beforeCreation =
                this.schedulerExecutionService.findScheduledExecutions(ExecutionStatus.NEW);
        this.schedulerExecutionService.createNextExecution(schedulerExecution, cron);
        final List<SchedulerExecution> result =
                this.schedulerExecutionService.findScheduledExecutions(ExecutionStatus.NEW);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.size()).isEqualTo(beforeCreation.size() + 1);
    }

    @Test(expected = SchedulerValidationException.class)
    public void testCreateNextExecutionNull() {
        this.schedulerExecutionService.createNextExecution(null, "0 0/2 0 ? * *");
    }

    @Test
    public void testDelete() {
        final SchedulerExecution saved = this.getNewSchedulerExecution();
        this.schedulerExecutionService.deleteExecution(saved.getId());
        try {
            this.schedulerExecutionService.findById(saved.getId());
            fail("SchedulerExecution did not get deleted");
        } catch (final SchedulerRuntimException e) {
            log.debug("Deleted SchedulerExecution, everything fine!");
        }
    }


    @Test(expected = SchedulerRuntimException.class)
    public void testDeleteExecutionStateRunning() {
        final SchedulerExecution schedulerExecution = SchedulerExecutionTestHelper.createSchedulerExecution();
        schedulerExecution.setState(ExecutionStatus.RUNNING);
        final SchedulerExecution saved = this.schedulerExecutionService.save(schedulerExecution);
        this.schedulerExecutionService.deleteExecution(saved.getId());
    }

    @Test
    public void testFindById() {
        final SchedulerExecution saved = this.getNewSchedulerExecution();
        final SchedulerExecution found = this.schedulerExecutionService.findById(saved.getId());
        BDDAssertions.then(found).isEqualTo(saved);
    }

    @Test(expected = SchedulerRuntimException.class)
    public void testFindByIdNotFound() {
        this.schedulerExecutionService.findById(-1L);
    }

    @Test
    public void testGetNextFireTime() {
        final String cron = "0 0/2 0 ? * * ";
        final Date now = new Date();
        final Date fireDate = this.schedulerExecutionService.getNextFireTime(cron);
        BDDAssertions.then(fireDate).isAfter(now);
    }

    @Test(expected = SchedulerRuntimException.class)
    public void testGetNextFireTimeUnparsableExpression() {
        final String cron = "0 0/2 0 ?";
        this.schedulerExecutionService.getNextFireTime(cron);
    }

    @Override
    protected CleanUpRepository cleanUpRepository() {
        return this.cleanUpRepository;
    }

    private SchedulerExecution getNewSchedulerExecution() {
        final SchedulerExecution schedulerExecution = SchedulerExecutionTestHelper.createSchedulerExecution();
        return this.schedulerExecutionService.save(schedulerExecution);
    }
}