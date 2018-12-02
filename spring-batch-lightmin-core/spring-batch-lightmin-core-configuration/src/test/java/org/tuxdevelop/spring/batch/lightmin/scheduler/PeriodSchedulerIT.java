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
public class PeriodSchedulerIT {

    @Autowired
    private PeriodScheduler periodScheduler;

    @Test
    public void scheduleIT() {
        this.periodScheduler.schedule();
        assertThat(this.periodScheduler.getSchedulerStatus()).isEqualTo(SchedulerStatus.RUNNING);
        this.periodScheduler.terminate();
    }

    @Test
    public void terminateIT() {
        this.periodScheduler.schedule();
        assertThat(this.periodScheduler.getSchedulerStatus()).isEqualTo(SchedulerStatus.RUNNING);
        this.periodScheduler.terminate();
        assertThat(this.periodScheduler.getSchedulerStatus()).isEqualTo(SchedulerStatus.STOPPED);
    }
}
