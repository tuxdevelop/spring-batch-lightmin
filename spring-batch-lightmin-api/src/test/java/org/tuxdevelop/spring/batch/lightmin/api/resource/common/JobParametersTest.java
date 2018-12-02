package org.tuxdevelop.spring.batch.lightmin.api.resource.common;

import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;


public class JobParametersTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testStructureAndBehavior(JobParameters.class);
        this.testEquals(JobParameters.class);
    }
}
