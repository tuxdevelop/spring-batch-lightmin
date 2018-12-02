package org.tuxdevelop.spring.batch.lightmin.domain;


import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;

public class JobConfigurationTest extends PojoTestBase {
    @Override
    public void performPojoTest() {
        this.testStructureAndBehavior(JobConfiguration.class);
        this.testEquals(JobConfiguration.class);
    }
}
