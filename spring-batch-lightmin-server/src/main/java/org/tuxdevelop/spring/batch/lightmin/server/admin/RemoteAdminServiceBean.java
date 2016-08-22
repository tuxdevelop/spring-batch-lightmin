package org.tuxdevelop.spring.batch.lightmin.server.admin;


import org.springframework.web.client.RestTemplate;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class RemoteAdminServiceBean implements AdminServerService {

    private final RestTemplate restTemplate;

    public RemoteAdminServiceBean(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void saveJobConfiguration(final JobConfiguration jobConfiguration, final LightminClientApplication lightminClientApplication) {
        restTemplate.postForEntity(getClientUri(lightminClientApplication), jobConfiguration, Void.class);
    }

    private String getClientUri(final LightminClientApplication lightminClientApplication) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(lightminClientApplication.getServiceUrl());
        stringBuilder.append("/api/jobconfigurations");
        return stringBuilder.toString();
    }

}
