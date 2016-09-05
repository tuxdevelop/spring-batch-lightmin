package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;


import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SchedulerStatusTest {

    @Test
    public void testGetByValueINITIALIZED() {
        final SchedulerStatus schedulerStatus = SchedulerStatus.getByValue("INITIALIZED");
        Assertions.assertThat(schedulerStatus).isEqualTo(SchedulerStatus.INITIALIZED);
    }

    @Test
    public void testGetByValueRUNNING() {
        final SchedulerStatus schedulerStatus = SchedulerStatus.getByValue("RUNNING");
        Assertions.assertThat(schedulerStatus).isEqualTo(SchedulerStatus.RUNNING);
    }

    @Test
    public void testGetByValueSTOPPED() {
        final SchedulerStatus schedulerStatus = SchedulerStatus.getByValue("STOPPED");
        Assertions.assertThat(schedulerStatus).isEqualTo(SchedulerStatus.STOPPED);
    }

    @Test
    public void testGetByValueINTERMINATION() {
        final SchedulerStatus schedulerStatus = SchedulerStatus.getByValue("IN TERMINATION");
        Assertions.assertThat(schedulerStatus).isEqualTo(SchedulerStatus.IN_TERMINATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByValueUnknown() {
        SchedulerStatus.getByValue("UNKNOWN");
    }
}
