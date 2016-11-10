package org.tuxdevelop.spring.batch.lightmin.admin.scheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus;
import org.tuxdevelop.test.configuration.ITConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITConfiguration.class)
public class CronSchedulerIT {

    @Autowired
    private CronScheduler cronScheduler;

    @Test
    public void scheduleIT() {
        cronScheduler.schedule();
        assertThat(cronScheduler.getSchedulerStatus()).isEqualTo(SchedulerStatus.RUNNING);
    }

    @Test
    public void terminateIT() {
        cronScheduler.schedule();
        assertThat(cronScheduler.getSchedulerStatus()).isEqualTo(SchedulerStatus.RUNNING);
        cronScheduler.terminate();
        assertThat(cronScheduler.getSchedulerStatus()).isEqualTo(SchedulerStatus.STOPPED);
    }
}
