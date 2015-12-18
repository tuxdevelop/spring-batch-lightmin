package org.tuxdevelop.spring.batch.lightmin.controller;


import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexControllerTest {

    private InetAddress inetAddress;

    private final IndexController indexController = new IndexController(inetAddress, "/");

    @Test
    public void determinIndexNullTest() {
        final String result = indexController.determinIndex(null);
        assertThat(result).isEqualTo("/");
    }

    @Test
    public void determinIndexSlashTest() {
        final String result = indexController.determinIndex("/");
        assertThat(result).isEqualTo("/");
    }

    @Test
    public void determinIndexCompletedTest() {
        final String result = indexController.determinIndex("/testServletPath/");
        assertThat(result).isEqualTo("/testServletPath/");
    }

    @Test
    public void determinIndexTest() {
        final String result = indexController.determinIndex("/testServletPath");
        assertThat(result).isEqualTo("/testServletPath/");
    }

    @Before
    public void init() throws UnknownHostException {
        inetAddress = InetAddress.getLocalHost();
    }
}
