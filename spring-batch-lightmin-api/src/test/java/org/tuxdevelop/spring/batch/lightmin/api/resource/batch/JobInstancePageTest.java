package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;

public class JobInstancePageTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobInstancePage.class);
        testEquals(JobInstancePage.class);
    }
}
