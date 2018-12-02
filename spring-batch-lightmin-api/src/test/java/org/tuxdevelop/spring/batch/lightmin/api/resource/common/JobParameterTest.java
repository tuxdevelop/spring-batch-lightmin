package org.tuxdevelop.spring.batch.lightmin.api.resource.common;

import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;


public class JobParameterTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testStructureAndBehavior(JobParameter.class);
        this.testEquals(JobParameter.class);
    }
}
