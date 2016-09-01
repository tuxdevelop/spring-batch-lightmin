package org.tuxdevelop.spring.batch.lightmin.client.api;

import org.tuxdevelop.spring.batch.lightmin.client.PojoTestBase;

public class LightminClientApplicationTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(LightminClientApplication.class);
        testEquals(LightminClientApplication.class);
    }
}
