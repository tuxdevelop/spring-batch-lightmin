package org.tuxdevelop.spring.batch.lightmin.api.response;

import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;


public class StepExecutionTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(StepExecution.class);
        testEquals(StepExecution.class);
    }
}
