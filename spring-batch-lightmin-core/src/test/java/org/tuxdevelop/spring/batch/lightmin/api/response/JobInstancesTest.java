package org.tuxdevelop.spring.batch.lightmin.api.response;

import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;

public class JobInstancesTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobInstances.class);
        testEquals(JobInstances.class);
    }
}
