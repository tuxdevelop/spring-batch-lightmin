package org.tuxdevelop.spring.batch.lightmin.model;


import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;

public class JobInfoModelTest extends PojoTestBase {
    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobInfoModel.class);
        testEquals(JobInfoModel.class);
    }
}
