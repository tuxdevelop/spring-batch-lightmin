package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;


public class JobInstanceTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testStructureAndBehavior(JobInstance.class);
        this.testEquals(JobInstance.class);
    }
}
