package org.tuxdevelop.spring.batch.lightmin.client.classic.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.client.classic.configuration.LightminClientClassicConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.client.service.LightminServerLocatorService;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class UrlLightminServerLocatorService implements LightminServerLocatorService {

    private final LightminClientClassicConfigurationProperties lightminClientClassicConfigurationProperties;

    public UrlLightminServerLocatorService(
            final LightminClientClassicConfigurationProperties lightminClientClassicConfigurationProperties) {
        this.lightminClientClassicConfigurationProperties = lightminClientClassicConfigurationProperties;
    }

    @Override
    public List<String> getRemoteUrls() {
        final List<String> urls;
        if (this.lightminClientClassicConfigurationProperties.getServer().getLightminUrl() == null
                || this.lightminClientClassicConfigurationProperties.getServer().getLightminUrl().length == 0) {
            throw new SpringBatchLightminConfigurationException("The lightmin server urls must not be null or empty");
        } else {
            urls = Arrays.asList(this.lightminClientClassicConfigurationProperties.getServer().getLightminUrl());
            log.debug("Configured lightmin server urls {}", urls);
        }
        return urls;
    }
}
