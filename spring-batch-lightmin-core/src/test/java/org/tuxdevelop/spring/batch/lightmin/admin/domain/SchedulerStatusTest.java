package org.tuxdevelop.spring.batch.lightmin.admin.domain;


import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import static org.assertj.core.api.Assertions.assertThat;

public class SchedulerStatusTest {

    @Test
    public void getByValueINITIALIZEDTest() {
        final SchedulerStatus schedulerStatus = SchedulerStatus.getByValue("INITIALIZED");
        assertThat(schedulerStatus).isEqualTo(SchedulerStatus.INITIALIZED);
    }

    @Test
    public void getByValueRUNNINGTest() {
        final SchedulerStatus schedulerStatus = SchedulerStatus.getByValue("RUNNING");
        assertThat(schedulerStatus).isEqualTo(SchedulerStatus.RUNNING);
    }

    @Test
    public void getByValueSTOPPEDTest() {
        final SchedulerStatus schedulerStatus = SchedulerStatus.getByValue("STOPPED");
        assertThat(schedulerStatus).isEqualTo(SchedulerStatus.STOPPED);
    }

    @Test
    public void getByValueINTERMINATIONTest() {
        final SchedulerStatus schedulerStatus = SchedulerStatus.getByValue("IN TERMINATION");
        assertThat(schedulerStatus).isEqualTo(SchedulerStatus.IN_TERMINATION);
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void getByValueUnknownTest() {
        SchedulerStatus.getByValue("UNKNOWN");
    }
}
