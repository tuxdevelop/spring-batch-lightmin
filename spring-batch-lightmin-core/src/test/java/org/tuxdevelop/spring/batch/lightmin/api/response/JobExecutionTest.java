package org.tuxdevelop.spring.batch.lightmin.api.response;

import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;


public class JobExecutionTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobExecution.class);
        testEquals(JobExecution.class);
    }
}
