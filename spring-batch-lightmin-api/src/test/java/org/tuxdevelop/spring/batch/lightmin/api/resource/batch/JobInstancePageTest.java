package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;

public class JobInstancePageTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testStructureAndBehavior(JobInstancePage.class);
        this.testEquals(JobInstancePage.class);
    }
}
