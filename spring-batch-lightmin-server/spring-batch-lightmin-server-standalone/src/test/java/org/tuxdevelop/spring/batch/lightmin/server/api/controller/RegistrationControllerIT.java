package org.tuxdevelop.spring.batch.lightmin.server.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientInformation;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.test.configuration.ITServerConfiguration;
import org.tuxdevelop.test.configuration.ITServerConfigurationApplication;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ITServerConfigurationApplication.class, ITServerConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationControllerIT {

    private static final String LOCALHOST = "http://localhost";
    private static final String PORT = "8080";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RegistrationBean registrationBean;
    @LocalServerPort
    private Integer serverPort;

    @Test
    public void testRegister() {
        final LightminClientApplication lightminClientApplication = this.createLightminClientApplication(
                "registerApplicationName");

        final ResponseEntity<LightminClientApplication> response = this.restTemplate.postForEntity(
                LOCALHOST + ":" + this.getServerPort() + "/api/applications",
                lightminClientApplication, LightminClientApplication.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        log.info("Registered LightminClientApplication {}", response.getBody());
    }

    @Test
    public void testUnregister() {
        final LightminClientApplication lightminClientApplication = this.createLightminClientApplication(
                "unregisterApplicationName");
        final ResponseEntity<LightminClientApplication> response = this.restTemplate
                .postForEntity(LOCALHOST + ":" + this.getServerPort() +
                                "/api/applications",
                        lightminClientApplication, LightminClientApplication.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final LightminClientApplication registeredClientApplication = response.getBody();
        log.info("Registered LightminClientApplication {}", registeredClientApplication);
        final String applicationId = registeredClientApplication.getId();
        assertThat(applicationId).isNotNull();
        final LightminClientApplication localLightminClientApplication = this.registrationBean.findById(applicationId);
        assertThat(localLightminClientApplication).isNotNull();
        final String uri = LOCALHOST + ":" + this.getServerPort() + "/api/applications/" + applicationId;
        this.restTemplate.delete(uri);
        final LightminClientApplication deletedLightminApplicationClient = this.registrationBean.findById(applicationId);
        assertThat(deletedLightminApplicationClient).isNull();
    }

    @Test
    public void testGetAll() {
        final LightminClientApplication lightminClientApplication = this.createLightminClientApplication(
                "unregisterApplicationName");
        final ResponseEntity<LightminClientApplication> responseCreated = this.restTemplate.postForEntity(
                LOCALHOST + ":" + this.getServerPort() + "/api/applications",
                lightminClientApplication, LightminClientApplication.class);
        assertThat(responseCreated.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        log.info("Registered LightminClientApplication {}", responseCreated.getBody());
        final ResponseEntity<LightminClientApplication[]> response = this.restTemplate
                .getForEntity(LOCALHOST + ":" + this.getServerPort() +
                        "/api/applications", LightminClientApplication[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final List<LightminClientApplication> lightminClientApplications = Arrays.asList(response.getBody());
        assertThat(lightminClientApplications).hasSize(1);
        final LightminClientApplication fetchedLightminClientApplication = lightminClientApplications.get(0);
        assertThat(fetchedLightminClientApplication).isEqualTo(responseCreated.getBody());
    }

    @After
    public void afterTest() {
        this.registrationBean.clear();
    }

    private int getServerPort() {
        return this.serverPort;
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
