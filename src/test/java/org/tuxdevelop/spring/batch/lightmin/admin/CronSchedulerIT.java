package org.tuxdevelop.spring.batch.lightmin.admin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.ITConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITConfiguration.class)
public class CronSchedulerIT {

	@Autowired
	private CronScheduler cronScheduler;

	@Test
	public void scheduleIT() {
		cronScheduler.schedule();
		assertThat(cronScheduler.getStatus()).isEqualTo(SchedulerStatus.RUNNING);
	}

	@Test
	public void terminateIT() {
		cronScheduler.schedule();
		assertThat(cronScheduler.getStatus()).isEqualTo(SchedulerStatus.RUNNING);
		cronScheduler.terminate();
		assertThat(cronScheduler.getStatus()).isEqualTo(SchedulerStatus.STOPPED);
	}
}
