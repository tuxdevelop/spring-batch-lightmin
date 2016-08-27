package org.tuxdevelop.spring.batch.lightmin.server.web;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class IndexControllerTest {

    @Mock
    private RegistrationBean registrationBean;

    @InjectMocks
    private IndexController indexController;

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
    public void init() {
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController("/", registrationBean);
    }
}
