package org.tuxdevelop.spring.batch.lightmin.api.resource;

import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;


public class JobParameterTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobParameter.class);
        testEquals(JobParameter.class);
    }
}
