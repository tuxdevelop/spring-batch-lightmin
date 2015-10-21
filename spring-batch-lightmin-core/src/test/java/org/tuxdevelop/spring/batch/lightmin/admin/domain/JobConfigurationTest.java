package org.tuxdevelop.spring.batch.lightmin.admin.domain;

import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;


public class JobConfigurationTest extends PojoTestBase {
    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobConfiguration.class);
        testEquals(JobConfiguration.class);
    }
}
