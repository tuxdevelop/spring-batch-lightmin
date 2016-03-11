package org.tuxdevelop.spring.batch.lightmin.api.response;


import org.junit.Ignore;
import org.tuxdevelop.spring.batch.lightmin.PojoTestBase;

@Ignore
//TODO: fixme Reflection problem
public class JobInstanceExecutionsTest extends PojoTestBase {

    @Override
    public void performPojoTest() {
        testStructureAndBehavior(JobInstanceExecutions.class);
        testEquals(JobInstanceExecutions.class);
    }
}
