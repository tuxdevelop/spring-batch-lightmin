package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;


import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;

public class JobConfigurationTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testEquals(JobConfiguration.class);
        this.testStructureAndBehavior(JobConfiguration.class);
    }
}
