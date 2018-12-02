package org.tuxdevelop.spring.batch.lightmin.util;

import org.junit.Test;
import org.springframework.batch.core.Job;
import org.tuxdevelop.spring.batch.lightmin.test.domain.DomainTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonJobFactoryTest {

    @Test
    public void constructorTest() {
        final String jobName = "sampleJob";
        final Job job = DomainTestHelper.createJob(jobName);
        final CommonJobFactory commonJobFactory = new CommonJobFactory(job, jobName);
        assertThat(commonJobFactory).isNotNull();
        assertThat(commonJobFactory.getJobName()).isEqualTo(jobName);
        assertThat(commonJobFactory.createJob()).isEqualTo(job);
    }
}
