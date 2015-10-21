package org.tuxdevelop.spring.batch.lightmin.controller;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexControllerTest {

    private final IndexController indexController = new IndexController();

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
}
