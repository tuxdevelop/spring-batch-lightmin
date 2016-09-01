package org.tuxdevelop.spring.batch.lightmin.client.registration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminProperties;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LightminClientRegistratorTest {

    @Mock
    private LightminClientProperties lightminClientProperties;
    @Mock
    private LightminProperties lightminProperties;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private JobRegistry jobRegistry;

    @InjectMocks
    private LightminClientRegistrator lightminClientRegistrator;

    @Test
    public void testRegister() {
        final Map map = new HashMap<>();
        map.put("id", "12345");
        final ResponseEntity<Map> responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(map);
        when(lightminClientProperties.getServiceUrl()).thenReturn("http://localhost:8080");
        when(lightminProperties.getLightminUrl()).thenReturn(new String[]{"http://localhost:8080"});
        when(jobRegistry.getJobNames()).thenReturn(new LinkedList<String>());
        when(restTemplate.postForEntity(anyString(), any(Object.class), any(Class.class))).thenReturn(responseEntity);

        final Boolean result = lightminClientRegistrator.register();
        assertThat(result).isTrue();
    }

    @Test
    public void testRegisterResponseError() {
        final Map map = new HashMap<>();
        map.put("id", "12345");
        final ResponseEntity<Map> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        when(lightminClientProperties.getServiceUrl()).thenReturn("http://localhost:8080");
        when(lightminProperties.getLightminUrl()).thenReturn(new String[]{"http://localhost:8080"});
        when(jobRegistry.getJobNames()).thenReturn(new LinkedList<String>());
        when(restTemplate.postForEntity(anyString(), any(Object.class), any(Class.class))).thenReturn(responseEntity);

        final Boolean result = lightminClientRegistrator.register();
        assertThat(result).isFalse();
    }

    @Test
    public void testDeregister() {

        try {
            when(lightminProperties.getLightminUrl()).thenReturn(new String[]{"http://localhost:8080"});
            lightminClientRegistrator.deregister();
        } catch (final Exception e) {
            fail(e.getMessage());
        }

    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        lightminClientRegistrator = new LightminClientRegistrator(lightminClientProperties, lightminProperties, restTemplate, jobRegistry);
    }

}
