package org.tuxdevelop.spring.batch.lightmin.server.discovery;


import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.util.ResponseUtil;

import java.net.URI;

public class DiscoveryRegistrationBean {

    private final RegistrationBean registrationBean;
    private final RestTemplate restTemplate;

    public DiscoveryRegistrationBean(final RegistrationBean registrationBean, final RestTemplate restTemplate) {
        this.registrationBean = registrationBean;
        this.restTemplate = restTemplate;
    }

    public void register(final ServiceInstance serviceInstance) {
        final LightminClientApplication lightminClientApplication = this.getLightminClientApplication(serviceInstance);
        this.registrationBean.register(lightminClientApplication);
    }

    private LightminClientApplication getLightminClientApplication(final ServiceInstance serviceInstance) {

        final URI uri = serviceInstance.getUri();
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUri(uri)
                .path("/api/lightminclientapplications");
        final String uriString = uriComponentsBuilder.toUriString();
        final ResponseEntity<LightminClientApplication> response =
                this.restTemplate.getForEntity(uriString, LightminClientApplication.class);
        ResponseUtil.checkHttpOk(response);
        return response.getBody();
    }

}
