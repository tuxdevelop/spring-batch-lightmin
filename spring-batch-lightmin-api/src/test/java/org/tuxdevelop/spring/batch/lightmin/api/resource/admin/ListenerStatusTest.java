package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenerStatusTest {


    @Test
    public void testGetByValueACTIVE() {
        final ListenerStatus result = ListenerStatus.getByValue("ACTIVE");
        assertThat(result).isEqualTo(ListenerStatus.ACTIVE);
    }

    @Test
    public void testGetByValueSTOPPED() {
        final ListenerStatus result = ListenerStatus.getByValue("STOPPED");
        assertThat(result).isEqualTo(ListenerStatus.STOPPED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getByValueUnknownTest() {
        ListenerStatus.getByValue("UNKNOWN");
    }
}
