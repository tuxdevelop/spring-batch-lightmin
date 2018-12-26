package org.tuxdevelop.spring.batch.lightmin.scheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.domain.SchedulerStatus;
import org.tuxdevelop.test.configuration.ITConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ITConfiguration.class)
public class CronSchedulerIT {

    @Autowired
    private CronScheduler cronScheduler;

    @Test
    public void scheduleIT() {
        this.cronScheduler.schedule();
        assertThat(this.cronScheduler.getSchedulerStatus()).isEqualTo(SchedulerStatus.RUNNING);
    }

    @Test
    public void terminateIT() {
        this.cronScheduler.schedule();
        assertThat(this.cronScheduler.getSchedulerStatus()).isEqualTo(SchedulerStatus.RUNNING);
        this.cronScheduler.terminate();
        assertThat(this.cronScheduler.getSchedulerStatus()).isEqualTo(SchedulerStatus.STOPPED);
    }
}
