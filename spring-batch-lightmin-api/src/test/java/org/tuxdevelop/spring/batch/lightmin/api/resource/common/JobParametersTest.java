package org.tuxdevelop.spring.batch.lightmin.api.resource.common;

import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;


public class JobParametersTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobParameters.class);
        testEquals(JobParameters.class);
    }
}
