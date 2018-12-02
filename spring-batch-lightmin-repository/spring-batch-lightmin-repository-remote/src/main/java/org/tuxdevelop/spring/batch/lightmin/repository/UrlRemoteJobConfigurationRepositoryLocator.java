package org.tuxdevelop.spring.batch.lightmin.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.repository.configuration.RemoteJobConfigurationRepositoryConfigurationProperties;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class UrlRemoteJobConfigurationRepositoryLocator implements RemoteJobConfigurationRepositoryLocator {

    private final RemoteJobConfigurationRepositoryConfigurationProperties properties;

    public UrlRemoteJobConfigurationRepositoryLocator(final RemoteJobConfigurationRepositoryConfigurationProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getRemoteUrl() {
        final String remoteUrl = this.properties.getServerUrl();
        if (!StringUtils.hasText(remoteUrl)) {
            throw new SpringBatchLightminConfigurationException("Remote url for Remote Repository Server must not be null or empty");
        } else {
            log.debug("Remote Repository Url {} configured", remoteUrl);
        }
        return remoteUrl;
    }
}
