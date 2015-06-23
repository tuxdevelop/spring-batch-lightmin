package org.tuxdevelop.spring.batch.lightmin.admin.scheduler;

/**
 * Created by marbecker on 2/23/15.
 */
public interface Scheduler {

    void schedule();

    void terminate();
}
