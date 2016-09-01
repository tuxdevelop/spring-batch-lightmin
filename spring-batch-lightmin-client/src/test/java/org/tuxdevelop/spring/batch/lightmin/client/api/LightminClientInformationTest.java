package org.tuxdevelop.spring.batch.lightmin.client.api;

import org.tuxdevelop.spring.batch.lightmin.client.PojoTestBase;

public class LightminClientInformationTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(LightminClientInformation.class);
        testEquals(LightminClientInformation.class);
    }
}
