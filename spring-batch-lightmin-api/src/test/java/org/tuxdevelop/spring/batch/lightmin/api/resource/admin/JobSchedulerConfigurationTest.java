package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;


import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;

public class JobSchedulerConfigurationTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testEquals(JobSchedulerConfiguration.class);
        testStructureAndBehavior(JobSchedulerConfiguration.class);
    }
}
