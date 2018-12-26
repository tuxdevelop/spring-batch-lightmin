package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;


public class JobExecutionTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testStructureAndBehavior(JobExecution.class);
        this.testEquals(JobExecution.class);
    }
}
