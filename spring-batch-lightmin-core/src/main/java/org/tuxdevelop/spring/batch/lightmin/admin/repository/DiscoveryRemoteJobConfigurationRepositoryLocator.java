package org.tuxdevelop.spring.batch.lightmin.admin.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

@Slf4j
public class DiscoveryRemoteJobConfigurationRepositoryLocator implements RemoteJobConfigurationRepositoryLocator {

    private final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;
    private final DiscoveryClient discoveryClient;

    public DiscoveryRemoteJobConfigurationRepositoryLocator(final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties,
                                                            final DiscoveryClient discoveryClient) {
        this.springBatchLightminConfigurationProperties = springBatchLightminConfigurationProperties;
        this.discoveryClient = discoveryClient;
    }

    @Override
    public String getRemoteUrl() {
        return this.getUrl(this.springBatchLightminConfigurationProperties.getRemoteRepositoryServerDiscoveryName());
    }

    private String getUrl(final String serverName) {
        final List<ServiceInstance> instances = this.discoveryClient.getInstances(serverName);
        final String url;
        if (instances != null && !instances.isEmpty()) {
            url = this.determineUrl(instances);
        } else {
            throw new SpringBatchLightminConfigurationException("Could not find RemoteRepositoryServer with the serviceId: " + serverName);
        }
        return url;
    }

    private String determineUrl(final List<ServiceInstance> instances) {
        final int count = instances.size();
        final String url;
        if (!instances.isEmpty()) {
            if (instances.size() == 1) {
                url = instances.get(0).getUri().toString();
            } else {
                final Random random = new Random();
                final int pos = random.nextInt(count);
                url = instances.get(pos).getUri().toString();
            }
        } else {
            url = null;
        }
        return url;
    }

    @PostConstruct
    public void init() {
        final String serverName = this.springBatchLightminConfigurationProperties.getRemoteRepositoryServerDiscoveryName();
        String url;
        int retries = 0;
        final Long waitTime = this.springBatchLightminConfigurationProperties.getRemoteRepositoryServerStartupDiscoveryRetryWaitTime();
        do {
            final List<ServiceInstance> instances = this.discoveryClient.getInstances(serverName);
            url = this.determineUrl(instances);
            if (url == null) {
                try {
                    log.info("Waiting {} milliseconds to get remote repository server by service discovery", waitTime);
                    Thread.sleep(waitTime);
                } catch (final InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
            retries++;
        }
        while (url == null && retries < this.springBatchLightminConfigurationProperties.getRemoteRepositoryServerStartupDiscoveryRetry());

        if (url == null) {
            throw new SpringBatchLightminConfigurationException("Could not find remote repository with the serviceId: " + serverName);
        }
    }
}
