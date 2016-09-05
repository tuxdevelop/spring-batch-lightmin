package org.tuxdevelop.spring.batch.lightmin.admin.domain;


import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenerStatusTest {


    @Test
    public void testGetByValueACTIVE() {
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.ListenerStatus result = org.tuxdevelop.spring.batch.lightmin.api.resource.admin.ListenerStatus.getByValue("ACTIVE");
        assertThat(result).isEqualTo(org.tuxdevelop.spring.batch.lightmin.api.resource.admin.ListenerStatus.ACTIVE);
    }

    @Test
    public void testGetByValueSTOPPED() {
        final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.ListenerStatus result = org.tuxdevelop.spring.batch.lightmin.api.resource.admin.ListenerStatus.getByValue("STOPPED");
        assertThat(result).isEqualTo(org.tuxdevelop.spring.batch.lightmin.api.resource.admin.ListenerStatus.STOPPED);
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void getByValueUnknownTest() {
        ListenerStatus.getByValue("UNKNOWN");
    }
}
