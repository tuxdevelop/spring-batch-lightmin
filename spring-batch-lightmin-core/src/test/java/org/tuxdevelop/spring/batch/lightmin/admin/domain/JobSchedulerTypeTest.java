package org.tuxdevelop.spring.batch.lightmin.admin.domain;


import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import static org.assertj.core.api.Assertions.assertThat;

public class JobSchedulerTypeTest {

    @Test
    public void getByIdCronTest() {
        final JobSchedulerType jobSchedulerType = JobSchedulerType.getById(1L);
        assertThat(jobSchedulerType).isEqualTo(JobSchedulerType.CRON);
    }

    @Test
    public void getByIdPeriodTest() {
        final JobSchedulerType jobSchedulerType = JobSchedulerType.getById(2L);
        assertThat(jobSchedulerType).isEqualTo(JobSchedulerType.PERIOD);
    }

    @Test(expected = SpringBatchLightminConfigurationException.class)
    public void getByIdUnknownTest() {
        JobSchedulerType.getById(-1000L);
    }
}
