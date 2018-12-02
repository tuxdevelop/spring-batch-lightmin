package org.tuxdevelop.spring.batch.lightmin.domain;


import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

public class JobIncrementerTest {

    @Test
    public void getByIdentifierDATETest() {
        final JobIncrementer jobIncrementer = JobIncrementer.getByIdentifier("DATE_INCREMENTER");
        Assertions.assertThat(jobIncrementer).isEqualTo(JobIncrementer.DATE);
    }

    @Test
    public void getByIdentifierNONETest() {
        final JobIncrementer jobIncrementer = JobIncrementer.getByIdentifier("NONE");
        Assertions.assertThat(jobIncrementer).isEqualTo(JobIncrementer.NONE);
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void getByIdentifierExecptionTest() {
        JobIncrementer.getByIdentifier("EXCEPTION");
    }
}
