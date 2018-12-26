package org.tuxdevelop.spring.batch.lightmin.client.api;

import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;

public class LightminClientApplicationStatusTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testStructureAndBehavior(LightminClientApplicationStatus.class);
        this.testEquals(LightminClientApplicationStatus.class);
    }
}
