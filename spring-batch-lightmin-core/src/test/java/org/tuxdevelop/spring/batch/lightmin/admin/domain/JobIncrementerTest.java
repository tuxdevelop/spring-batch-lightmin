package org.tuxdevelop.spring.batch.lightmin.admin.domain;


import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import static org.assertj.core.api.Assertions.assertThat;

public class JobIncrementerTest {

    @Test
    public void getByIdentifierDATETest() {
        final JobIncrementer jobIncrementer = JobIncrementer.getByIdentifier("DATE_INCREMENTER");
        assertThat(jobIncrementer).isEqualTo(JobIncrementer.DATE);
    }

    @Test
    public void getByIdentifierNONETest() {
        final JobIncrementer jobIncrementer = JobIncrementer.getByIdentifier("NONE");
        assertThat(jobIncrementer).isEqualTo(JobIncrementer.NONE);
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void getByIdentifierExecptionTest() {
        JobIncrementer.getByIdentifier("EXCEPTION");
    }
}
