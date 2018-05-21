package org.tuxdevelop.spring.batch.lightmin.admin.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class UrlRemoteJobConfigurationRepositoryLocator implements RemoteJobConfigurationRepositoryLocator {

    private final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;

    public UrlRemoteJobConfigurationRepositoryLocator(final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties) {
        this.springBatchLightminConfigurationProperties = springBatchLightminConfigurationProperties;
    }

    @Override
    public String getRemoteUrl() {
        final String remoteUrl = this.springBatchLightminConfigurationProperties.getRemoteRepositoryServerUrl();
        if (!StringUtils.hasText(remoteUrl)) {
            throw new SpringBatchLightminConfigurationException("Remote url for Remote Repository Server must not be null or empty");
        } else {
            log.debug("Remote Repository Url {} configured", remoteUrl);
        }
        return remoteUrl;
    }
}
