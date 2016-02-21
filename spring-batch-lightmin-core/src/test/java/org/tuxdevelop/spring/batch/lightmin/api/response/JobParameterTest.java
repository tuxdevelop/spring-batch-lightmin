package org.tuxdevelop.spring.batch.lightmin.api.response;

import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;


public class JobParameterTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobParameter.class);
        testEquals(JobParameter.class);
    }
}
