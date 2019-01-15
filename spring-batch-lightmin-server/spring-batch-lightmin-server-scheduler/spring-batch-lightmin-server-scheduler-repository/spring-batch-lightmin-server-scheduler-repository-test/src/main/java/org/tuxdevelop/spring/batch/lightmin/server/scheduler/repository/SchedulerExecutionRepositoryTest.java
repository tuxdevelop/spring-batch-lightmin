package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import org.assertj.core.api.BDDAssertions;
import org.junit.After;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;

import java.util.Date;

public abstract class SchedulerExecutionRepositoryTest extends SchedulerTest {

    @Test
    public void testSave() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("saveExecutionApp");
        this.createSchedulerExecution(schedulerConfiguration.getId());
    }


    @After
    public void cleanUp() {
        getCleanUpRepository().cleanUp();
    }

    protected SchedulerExecution createSchedulerExecution(final Long schedulerConfigurationId) {
        final SchedulerExecution schedulerExecution = new SchedulerExecution();
        schedulerExecution.setExecutionCount(0);
        schedulerExecution.setState(1);
        schedulerExecution.setNextFireTime(new Date());
        schedulerExecution.setSchedulerConfigurationId(schedulerConfigurationId);

        final SchedulerExecution result = this.getSchedulerExecutionRepository().save(schedulerExecution);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getId()).isNotNull();

        return result;
    }

    public abstract SchedulerExecutionRepository getSchedulerExecutionRepository();

    @Override
    public abstract CleanUpRepository getCleanUpRepository();
}
