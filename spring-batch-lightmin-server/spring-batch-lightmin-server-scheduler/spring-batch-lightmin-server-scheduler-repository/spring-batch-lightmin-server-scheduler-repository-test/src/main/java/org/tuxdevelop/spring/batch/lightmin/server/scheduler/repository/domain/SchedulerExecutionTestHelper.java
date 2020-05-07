package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain;

import java.util.Date;

public final class SchedulerExecutionTestHelper {

    private SchedulerExecutionTestHelper() {
    }

    public static SchedulerExecution createSchedulerExecution() {
        return createSchedulerExecution(new Date(System.currentTimeMillis() + 10000L));
    }

    public static SchedulerExecution createSchedulerExecution(final Date nextFireDate) {
        final SchedulerExecution schedulerExecution = new SchedulerExecution();
        schedulerExecution.setExecutionCount(0);
        schedulerExecution.setNextFireTime(nextFireDate);
        schedulerExecution.setState(ExecutionStatus.NEW);
        schedulerExecution.setSchedulerConfigurationId(1L);
        schedulerExecution.setLastUpdate(new Date());
        return schedulerExecution;
    }

}
