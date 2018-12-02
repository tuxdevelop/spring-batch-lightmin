package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;


public class StepExecutionTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testStructureAndBehavior(StepExecution.class);
        this.testEquals(StepExecution.class);
    }
}
