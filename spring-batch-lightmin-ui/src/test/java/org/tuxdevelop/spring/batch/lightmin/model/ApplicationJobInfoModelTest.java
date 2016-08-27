package org.tuxdevelop.spring.batch.lightmin.model;


import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;

public class ApplicationJobInfoModelTest extends PojoTestBase {
    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobInfoModel.class);
        testEquals(JobInfoModel.class);
    }
}
