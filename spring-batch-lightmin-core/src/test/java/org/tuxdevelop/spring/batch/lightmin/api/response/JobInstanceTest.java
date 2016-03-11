package org.tuxdevelop.spring.batch.lightmin.api.response;

import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;


public class JobInstanceTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobInstance.class);
        testEquals(JobInstance.class);
    }
}
