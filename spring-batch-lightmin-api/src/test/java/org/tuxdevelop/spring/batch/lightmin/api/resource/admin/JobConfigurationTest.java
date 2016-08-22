package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;


import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;

public class JobConfigurationTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testEquals(JobConfiguration.class);
        testStructureAndBehavior(JobConfiguration.class);
    }
}
