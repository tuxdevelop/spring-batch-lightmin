package org.tuxdevelop.spring.batch.lightmin.client.server;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.StringUtils;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientProperties;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public class DiscoveryLightminServerLocator implements LightminServerLocator {

    private final LightminClientProperties lightminClientProperties;
    private final DiscoveryClient discoveryClient;

    public DiscoveryLightminServerLocator(final LightminClientProperties lightminClientProperties,
                                          final DiscoveryClient discoveryClient) {
        this.lightminClientProperties = lightminClientProperties;
        this.discoveryClient = discoveryClient;
    }

    @Override
    public List<String> getRemoteUrls() {
        final String serviceId = this.lightminClientProperties.getServerDiscoveryName();
        final List<String> urls;
        if (StringUtils.hasText(serviceId)) {
            urls = this.getServerUrls(serviceId);
        } else {
            throw new SpringBatchLightminConfigurationException("The serviceId for the lightmin server must not be null or empty!");
        }
        return urls;
    }

    private List<String> getServerUrls(final String serviceId) {
        final List<ServiceInstance> instances = this.discoveryClient.getInstances(serviceId);
        final List<String> urls = new ArrayList<>();
        for (final ServiceInstance instance : instances) {
            final String url = instance.getUri().toString();
            urls.add(url);
        }
        return urls;
    }


}
