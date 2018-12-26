package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;


import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;

public class JobSchedulerConfigurationTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testEquals(JobSchedulerConfiguration.class);
        this.testStructureAndBehavior(JobSchedulerConfiguration.class);
    }
}
