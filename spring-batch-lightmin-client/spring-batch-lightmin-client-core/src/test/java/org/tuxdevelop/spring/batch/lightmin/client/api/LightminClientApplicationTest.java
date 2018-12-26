package org.tuxdevelop.spring.batch.lightmin.client.api;

import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;

public class LightminClientApplicationTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testStructureAndBehavior(LightminClientApplication.class);
        this.testEquals(LightminClientApplication.class);
    }
}
