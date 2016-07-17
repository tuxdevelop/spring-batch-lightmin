package org.tuxdevelop.spring.batch.lightmin.model;


import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;

public class JobConfigurationAddModelTest extends PojoTestBase {
    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobConfigurationAddModel.class);
        testEquals(JobConfigurationAddModel.class);
    }
}
