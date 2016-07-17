package org.tuxdevelop.spring.batch.lightmin.api.resource;

import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;


public class JobParametersTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobParameters.class);
        testEquals(JobParameters.class);
    }
}
