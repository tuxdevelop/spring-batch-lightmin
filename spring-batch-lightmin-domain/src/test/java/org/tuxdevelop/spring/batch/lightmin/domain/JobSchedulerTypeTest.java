package org.tuxdevelop.spring.batch.lightmin.domain;


import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

public class JobSchedulerTypeTest {

    @Test
    public void getByIdCronTest() {
        final JobSchedulerType jobSchedulerType = JobSchedulerType.getById(1L);
        Assertions.assertThat(jobSchedulerType).isEqualTo(JobSchedulerType.CRON);
    }

    @Test
    public void getByIdPeriodTest() {
        final JobSchedulerType jobSchedulerType = JobSchedulerType.getById(2L);
        Assertions.assertThat(jobSchedulerType).isEqualTo(JobSchedulerType.PERIOD);
    }

    @Test(expected = SpringBatchLightminConfigurationException.class)
    public void getByIdUnknownTest() {
        JobSchedulerType.getById(-1000L);
    }
}
