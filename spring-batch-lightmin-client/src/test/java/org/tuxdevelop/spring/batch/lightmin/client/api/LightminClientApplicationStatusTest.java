package org.tuxdevelop.spring.batch.lightmin.client.api;

import org.tuxdevelop.spring.batch.lightmin.client.PojoTestBase;

public class LightminClientApplicationStatusTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(LightminClientApplicationStatus.class);
        testEquals(LightminClientApplicationStatus.class);
    }
}
