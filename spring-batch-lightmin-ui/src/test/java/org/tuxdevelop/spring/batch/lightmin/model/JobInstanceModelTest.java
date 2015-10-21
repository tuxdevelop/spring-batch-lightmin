package org.tuxdevelop.spring.batch.lightmin.model;


import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;

public class JobInstanceModelTest extends PojoTestBase {
    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobInstanceModel.class);
        testEquals(JobInstanceModel.class);
    }
}
