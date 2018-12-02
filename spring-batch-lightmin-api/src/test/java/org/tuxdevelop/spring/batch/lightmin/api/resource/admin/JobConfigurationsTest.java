package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;

import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;


public class JobConfigurationsTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testStructureAndBehavior(JobConfigurations.class);
        this.testEquals(JobConfigurations.class);
    }
}
