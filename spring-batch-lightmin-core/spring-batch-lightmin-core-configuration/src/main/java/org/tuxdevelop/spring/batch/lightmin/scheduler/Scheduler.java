package org.tuxdevelop.spring.batch.lightmin.scheduler;

import org.tuxdevelop.spring.batch.lightmin.domain.SchedulerStatus;

/**
 * @author Marcel Becker
 * @see AbstractScheduler
 * @since 0.1
 */
public interface Scheduler {

    /**
     * triggers the scheduler
     */
    void schedule();

    /**
     * terminates the scheduler
     */
    void terminate();

    /**
     * retrieves the current {@link SchedulerStatus}
     *
     * @return {@link SchedulerStatus}
     */
    SchedulerStatus getSchedulerStatus();
}
