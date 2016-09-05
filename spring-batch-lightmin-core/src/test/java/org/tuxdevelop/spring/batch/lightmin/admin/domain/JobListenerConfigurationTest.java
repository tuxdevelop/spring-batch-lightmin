package org.tuxdevelop.spring.batch.lightmin.admin.domain;


import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;

public class JobListenerConfigurationTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testEquals(JobListenerConfiguration.class);
        testStructureAndBehavior(JobListenerConfiguration.class);
    }
}
