package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;


public class JobExecutionTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobExecution.class);
        testEquals(JobExecution.class);
    }
}
