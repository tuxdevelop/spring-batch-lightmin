package org.tuxdevelop.spring.batch.lightmin.server.scheduler.service;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ExecutionStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecutionTestHelper;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.test.configuration.SchedulerCoreITConfiguration;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SchedulerCoreITConfiguration.class})
public class SchedulerExecutionServiceIT {

    @Autowired
    private SchedulerExecutionService schedulerExecutionService;

    @MockBean
    private JobServerService jobServerService;

    @Test
    public void testSave() {

        final SchedulerExecution schedulerExecution = SchedulerExecutionTestHelper.createSchedulerExecution();
        this.schedulerExecutionService.save(schedulerExecution);

        final SchedulerExecution result = this.schedulerExecutionService.save(schedulerExecution);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getId()).isNotNull();

    }

    @Test
    public void testCreateNextExecution() {
        final SchedulerExecution schedulerExecution = SchedulerExecutionTestHelper.createSchedulerExecution();
        final String cron = "0 0/2 0 ? * * *";
        final List<SchedulerExecution> beforeCreation =
                this.schedulerExecutionService.findScheduledExecutions(ExecutionStatus.NEW);
        this.schedulerExecutionService.createNextExecution(schedulerExecution, cron);
        final List<SchedulerExecution> result =
                this.schedulerExecutionService.findScheduledExecutions(ExecutionStatus.NEW);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.size()).isEqualTo(beforeCreation.size() + 1);
    }
}