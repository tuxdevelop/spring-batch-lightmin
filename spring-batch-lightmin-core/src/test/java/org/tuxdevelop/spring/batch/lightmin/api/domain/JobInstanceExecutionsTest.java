package org.tuxdevelop.spring.batch.lightmin.api.domain;


import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;

public class JobInstanceExecutionsTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobInstanceExecutions.class);
        testEquals(JobInstanceExecutions.class);
    }
}
