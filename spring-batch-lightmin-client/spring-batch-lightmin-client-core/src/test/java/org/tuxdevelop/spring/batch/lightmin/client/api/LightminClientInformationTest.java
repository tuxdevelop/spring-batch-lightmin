package org.tuxdevelop.spring.batch.lightmin.client.api;

import org.tuxdevelop.spring.batch.lightmin.test.PojoTestBase;

public class LightminClientInformationTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        this.testStructureAndBehavior(LightminClientInformation.class);
        this.testEquals(LightminClientInformation.class);
    }
}
