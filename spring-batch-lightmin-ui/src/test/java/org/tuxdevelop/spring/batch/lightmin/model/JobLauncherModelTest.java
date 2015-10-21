package org.tuxdevelop.spring.batch.lightmin.model;


import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;

public class JobLauncherModelTest extends PojoTestBase {
    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobLauncherModel.class);
        testEquals(JobLauncherModel.class);
    }
}
