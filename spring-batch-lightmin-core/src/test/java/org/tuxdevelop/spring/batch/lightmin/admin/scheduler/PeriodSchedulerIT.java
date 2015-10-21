package org.tuxdevelop.spring.batch.lightmin.admin.scheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.ITConfiguration;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.SchedulerStatus;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PeriodSchedulerIT {

    @Autowired
    private PeriodScheduler periodScheduler;

    @Test
    public void scheduleIT() {
        periodScheduler.schedule();
        assertThat(periodScheduler.getSchedulerStatus()).isEqualTo(SchedulerStatus.RUNNING);
        periodScheduler.terminate();
    }

    @Test
    public void terminateIT() {
        periodScheduler.schedule();
        assertThat(periodScheduler.getSchedulerStatus()).isEqualTo(SchedulerStatus.RUNNING);
        periodScheduler.terminate();
        assertThat(periodScheduler.getSchedulerStatus()).isEqualTo(SchedulerStatus.STOPPED);
    }
}
