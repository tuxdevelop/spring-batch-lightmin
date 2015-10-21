package org.tuxdevelop.spring.batch.lightmin.admin.domain;


import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;

public class SchedulerContructorWrapperTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(SchedulerConstructorWrapper.class);
        testEquals(SchedulerConstructorWrapper.class);
    }
}
