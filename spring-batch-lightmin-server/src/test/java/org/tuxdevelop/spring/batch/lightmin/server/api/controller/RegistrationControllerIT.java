package org.tuxdevelop.spring.batch.lightmin.server.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientInformation;
import org.tuxdevelop.spring.batch.lightmin.server.ITServerConfigurationApplication;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.test.configuration.ITServerConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
@SpringApplicationConfiguration(classes = {ITServerConfigurationApplication.class, ITServerConfiguration.class})
public class RegistrationControllerIT {

    private static final String LOCALHOST = "http://localhost";
    private static final String PORT = "8080";

    @Autowired
    private EmbeddedWebApplicationContext embeddedWebApplicationContext;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RegistrationBean registrationBean;

    @Test
    public void testRegister() {
        final LightminClientApplication lightminClientApplication = createLightminClientApplication(
                "registerApplicationName");

        final ResponseEntity<LightminClientApplication> response = restTemplate.postForEntity(
                LOCALHOST + ":" + getServerPort() + "/api/applications",
                lightminClientApplication, LightminClientApplication.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        log.info("Registered LightminClientApplication {}", response.getBody());
    }

    @Test
    public void testUnregister() {
        final LightminClientApplication lightminClientApplication = createLightminClientApplication(
                "unregisterApplicationName");
        final ResponseEntity<LightminClientApplication> response = restTemplate
                .postForEntity(LOCALHOST + ":" + getServerPort() +
                                "/api/applications",
                        lightminClientApplication, LightminClientApplication.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final LightminClientApplication registeredClientApplication = response.getBody();
        log.info("Registered LightminClientApplication {}", registeredClientApplication);
        final String applicationId = registeredClientApplication.getId();
        assertThat(applicationId).isNotNull();
        final LightminClientApplication localLightminClientApplication = registrationBean.get(applicationId);
        assertThat(localLightminClientApplication).isNotNull();
        final String uri = LOCALHOST + ":" + getServerPort() + "/api/applications/" + applicationId;
        restTemplate.delete(uri);
        final LightminClientApplication deletedLightminApplicationClient = registrationBean.get(applicationId);
        assertThat(deletedLightminApplicationClient).isNull();
    }

    @Test
    public void testGetAll() {
        final LightminClientApplication lightminClientApplication = createLightminClientApplication(
                "unregisterApplicationName");
        final ResponseEntity<LightminClientApplication> responseCreated = restTemplate.postForEntity(
                LOCALHOST + ":" + getServerPort() + "/api/applications",
                lightminClientApplication, LightminClientApplication.class);
        assertThat(responseCreated.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        log.info("Registered LightminClientApplication {}", responseCreated.getBody());
        final ResponseEntity<LightminClientApplication[]> response = restTemplate
                .getForEntity(LOCALHOST + ":" + getServerPort() +
                        "/api/applications", LightminClientApplication[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final List<LightminClientApplication> lightminClientApplications = Arrays.asList(response.getBody());
        assertThat(lightminClientApplications).hasSize(1);
        final LightminClientApplication fetchedLightminClientApplication = lightminClientApplications.get(0);
        assertThat(fetchedLightminClientApplication).isEqualTo(responseCreated.getBody());
    }

    @After
    public void afterTest() {
        registrationBean.clear();
    }

    private int getServerPort() {
        return embeddedWebApplicationContext.getEmbeddedServletContainer().getPort();
    }

    private LightminClientApplication createLightminClientApplication(final String applicationName) {
        final LightminClientApplication lightminClientApplication = new LightminClientApplication();
        lightminClientApplication.setName(applicationName);
        lightminClientApplication.setServiceUrl(LOCALHOST + ":" + PORT);
        lightminClientApplication.setManagementUrl(LOCALHOST + ":" + PORT + "/management");
        lightminClientApplication.setHealthUrl(LOCALHOST + ":" + PORT + "/health");
        lightminClientApplication.setLightminClientInformation(new LightminClientInformation());
        return lightminClientApplication;
    }

}
