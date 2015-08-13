package org.tuxdevelop.spring.batch.lightmin.admin.scheduler;

import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus;

/**
 * Created by marbecker on 2/23/15.
 */
public interface Scheduler {

    void schedule();

    void terminate();

    SchedulerStatus getSchedulerStatus();
}
