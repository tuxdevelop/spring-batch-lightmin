package org.tuxdevelop.spring.batch.lightmin.server.discovery;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.util.ResponseUtil;

import java.net.URI;
import java.util.Map;

@Slf4j
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
                .path(this.getContextPath(serviceInstance))
                .path("/api/lightminclientapplications");
        final String uriString = uriComponentsBuilder.toUriString();
        final ResponseEntity<LightminClientApplication> response =
                this.restTemplate.getForEntity(uriString, LightminClientApplication.class);
        ResponseUtil.checkHttpOk(response);
        return response.getBody();
    }

    private String getContextPath(final ServiceInstance serviceInstance) {
        final Map<String, String> metaData = serviceInstance.getMetadata();
        final String contextPath;
        if (metaData != null) {
            contextPath = metaData.getOrDefault("contextPath", "");
        } else {
            log.debug("No meta data available, nothing todo");
            contextPath = "";
        }
        return contextPath;
    }

}
