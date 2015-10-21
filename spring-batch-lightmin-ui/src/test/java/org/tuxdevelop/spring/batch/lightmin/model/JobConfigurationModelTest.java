package org.tuxdevelop.spring.batch.lightmin.model;

import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;


public class JobConfigurationModelTest extends PojoTestBase {
    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobConfigurationModel.class);
        testEquals(JobConfigurationModel.class);
    }
}
